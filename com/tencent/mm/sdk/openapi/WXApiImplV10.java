package com.tencent.mm.sdk.openapi;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.constants.ConstantsAPI.Token;
import com.tencent.mm.sdk.constants.ConstantsAPI.WXApp;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelbiz.AddCardToWXCardPackage;
import com.tencent.mm.sdk.modelbiz.CreateChatroom;
import com.tencent.mm.sdk.modelbiz.JoinChatroom;
import com.tencent.mm.sdk.modelbiz.OpenWebview;
import com.tencent.mm.sdk.modelmsg.GetMessageFromWX.Req;
import com.tencent.mm.sdk.modelmsg.LaunchFromWX;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage.IMediaObject;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.p061a.C0586a;
import com.tencent.mm.sdk.p061a.C0586a.C0582a;
import com.tencent.mm.sdk.p061a.p062a.C0584a;
import com.tencent.mm.sdk.p061a.p062a.C0584a.C0583a;
import com.tencent.mm.sdk.p061a.p062a.C0585b;
import com.tencent.mm.sdk.p063b.C0589b;
import com.tencent.wxop.stat.MtaSDkException;
import com.tencent.wxop.stat.StatAccount;
import com.tencent.wxop.stat.StatConfig;
import com.tencent.wxop.stat.StatReportStrategy;
import com.tencent.wxop.stat.StatService;
import com.tencent.wxop.stat.common.StatConstants;

final class WXApiImplV10 implements IWXAPI {
    private static final String TAG = "MicroMsg.SDK.WXApiImplV10";
    private static ActivityLifecycleCb activityCb;
    private static String wxappPayEntryClassname;
    private String appId;
    private boolean checkSignature;
    private Context context;
    private boolean detached;

    private static final class ActivityLifecycleCb implements ActivityLifecycleCallbacks {
        private static final int DELAYED = 800;
        private static final String TAG = "MicroMsg.SDK.WXApiImplV10.ActivityLifecycleCb";
        private Context context;
        private Handler handler;
        private boolean isForeground;
        private Runnable onPausedRunnable;
        private Runnable onResumedRunnable;

        /* renamed from: com.tencent.mm.sdk.openapi.WXApiImplV10.ActivityLifecycleCb.1 */
        class C06111 implements Runnable {
            C06111() {
            }

            public void run() {
                if (WXApiImplV10.activityCb != null && ActivityLifecycleCb.this.isForeground) {
                    Log.v(ActivityLifecycleCb.TAG, "WXStat trigger onBackground");
                    StatService.trackCustomKVEvent(ActivityLifecycleCb.this.context, "onBackground_WX", null);
                    ActivityLifecycleCb.this.isForeground = false;
                }
            }
        }

        /* renamed from: com.tencent.mm.sdk.openapi.WXApiImplV10.ActivityLifecycleCb.2 */
        class C06122 implements Runnable {
            C06122() {
            }

            public void run() {
                if (WXApiImplV10.activityCb != null && !ActivityLifecycleCb.this.isForeground) {
                    Log.v(ActivityLifecycleCb.TAG, "WXStat trigger onForeground");
                    StatService.trackCustomKVEvent(ActivityLifecycleCb.this.context, "onForeground_WX", null);
                    ActivityLifecycleCb.this.isForeground = true;
                }
            }
        }

        private ActivityLifecycleCb(Context context) {
            this.isForeground = false;
            this.handler = new Handler(Looper.getMainLooper());
            this.onPausedRunnable = new C06111();
            this.onResumedRunnable = new C06122();
            this.context = context;
        }

        public final void detach() {
            this.handler.removeCallbacks(this.onResumedRunnable);
            this.handler.removeCallbacks(this.onPausedRunnable);
            this.context = null;
        }

        public final void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public final void onActivityDestroyed(Activity activity) {
        }

        public final void onActivityPaused(Activity activity) {
            Log.v(TAG, activity.getComponentName().getClassName() + "  onActivityPaused");
            this.handler.removeCallbacks(this.onResumedRunnable);
            this.handler.postDelayed(this.onPausedRunnable, 800);
        }

        public final void onActivityResumed(Activity activity) {
            Log.v(TAG, activity.getComponentName().getClassName() + "  onActivityResumed");
            this.handler.removeCallbacks(this.onPausedRunnable);
            this.handler.postDelayed(this.onResumedRunnable, 800);
        }

        public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public final void onActivityStarted(Activity activity) {
        }

        public final void onActivityStopped(Activity activity) {
        }
    }

    static {
        activityCb = null;
        wxappPayEntryClassname = null;
    }

    WXApiImplV10(Context context, String str, boolean z) {
        this.checkSignature = false;
        this.detached = false;
        C0589b.m2021e(TAG, "<init>, appId = " + str + ", checkSignature = " + z);
        this.context = context;
        this.appId = str;
        this.checkSignature = z;
    }

    private boolean checkSumConsistent(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length == 0 || bArr2 == null || bArr2.length == 0) {
            C0589b.m2018b(TAG, "checkSumConsistent fail, invalid arguments");
            return false;
        } else if (bArr.length != bArr2.length) {
            C0589b.m2018b(TAG, "checkSumConsistent fail, length is different");
            return false;
        } else {
            for (int i = 0; i < bArr.length; i++) {
                if (bArr[i] != bArr2[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean createChatroom(Context context, Bundle bundle) {
        Cursor query = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/createChatroom"), null, null, new String[]{this.appId, bundle.getString("_wxapi_basereq_transaction"), bundle.getString("_wxapi_create_chatroom_group_id"), bundle.getString("_wxapi_create_chatroom_chatroom_name"), bundle.getString("_wxapi_create_chatroom_chatroom_nickname"), bundle.getString("_wxapi_create_chatroom_ext_msg")}, null);
        if (query != null) {
            query.close();
        }
        return true;
    }

    private void initMta(Context context, String str) {
        String str2 = "AWXOP" + str;
        StatConfig.setAppKey(context, str2);
        StatConfig.setEnableSmartReporting(true);
        StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
        StatConfig.setSendPeriodMinutes(60);
        StatConfig.setInstallChannel(context, "Wechat_Sdk");
        try {
            StatService.startStatService(context, str2, StatConstants.VERSION);
        } catch (MtaSDkException e) {
            e.printStackTrace();
        }
    }

    private boolean joinChatroom(Context context, Bundle bundle) {
        Cursor query = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/joinChatroom"), null, null, new String[]{this.appId, bundle.getString("_wxapi_basereq_transaction"), bundle.getString("_wxapi_join_chatroom_group_id"), bundle.getString("_wxapi_join_chatroom_chatroom_nickname"), bundle.getString("_wxapi_join_chatroom_ext_msg")}, null);
        if (query != null) {
            query.close();
        }
        return true;
    }

    private boolean sendAddCardToWX(Context context, Bundle bundle) {
        Cursor query = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/addCardToWX"), null, null, new String[]{this.appId, bundle.getString("_wxapi_add_card_to_wx_card_list"), bundle.getString("_wxapi_basereq_transaction")}, null);
        if (query != null) {
            query.close();
        }
        return true;
    }

    private boolean sendJumpToBizProfileReq(Context context, Bundle bundle) {
        Cursor query = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/jumpToBizProfile"), null, null, new String[]{this.appId, bundle.getString("_wxapi_jump_to_biz_profile_req_to_user_name"), bundle.getString("_wxapi_jump_to_biz_profile_req_ext_msg"), bundle.getInt("_wxapi_jump_to_biz_profile_req_scene"), bundle.getInt("_wxapi_jump_to_biz_profile_req_profile_type")}, null);
        if (query != null) {
            query.close();
        }
        return true;
    }

    private boolean sendJumpToBizTempSessionReq(Context context, Bundle bundle) {
        Cursor query = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/jumpToBizTempSession"), null, null, new String[]{this.appId, bundle.getString("_wxapi_jump_to_biz_webview_req_to_user_name"), bundle.getString("_wxapi_jump_to_biz_webview_req_session_from"), bundle.getInt("_wxapi_jump_to_biz_webview_req_show_type")}, null);
        if (query != null) {
            query.close();
        }
        return true;
    }

    private boolean sendJumpToBizWebviewReq(Context context, Bundle bundle) {
        Cursor query = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/jumpToBizProfile"), null, null, new String[]{this.appId, bundle.getString("_wxapi_jump_to_biz_webview_req_to_user_name"), bundle.getString("_wxapi_jump_to_biz_webview_req_ext_msg"), bundle.getInt("_wxapi_jump_to_biz_webview_req_scene")}, null);
        if (query != null) {
            query.close();
        }
        return true;
    }

    private boolean sendOpenBusiLuckyMoney(Context context, Bundle bundle) {
        Cursor query = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/openBusiLuckyMoney"), null, null, new String[]{this.appId, bundle.getString("_wxapi_open_busi_lucky_money_timeStamp"), bundle.getString("_wxapi_open_busi_lucky_money_nonceStr"), bundle.getString("_wxapi_open_busi_lucky_money_signType"), bundle.getString("_wxapi_open_busi_lucky_money_signature"), bundle.getString("_wxapi_open_busi_lucky_money_package")}, null);
        if (query != null) {
            query.close();
        }
        return true;
    }

    private boolean sendOpenRankListReq(Context context, Bundle bundle) {
        Cursor query = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/openRankList"), null, null, new String[0], null);
        if (query != null) {
            query.close();
        }
        return true;
    }

    private boolean sendOpenWebview(Context context, Bundle bundle) {
        Cursor query = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/openWebview"), null, null, new String[]{this.appId, bundle.getString("_wxapi_jump_to_webview_url"), bundle.getString("_wxapi_basereq_transaction")}, null);
        if (query != null) {
            query.close();
        }
        return true;
    }

    private boolean sendPayReq(Context context, Bundle bundle) {
        if (wxappPayEntryClassname == null) {
            wxappPayEntryClassname = new MMSharedPreferences(context).getString("_wxapp_pay_entry_classname_", null);
            C0589b.m2021e(TAG, "pay, set wxappPayEntryClassname = " + wxappPayEntryClassname);
            if (wxappPayEntryClassname == null) {
                C0589b.m2018b(TAG, "pay fail, wxappPayEntryClassname is null");
                return false;
            }
        }
        C0582a c0582a = new C0582a();
        c0582a.f1576Z = bundle;
        c0582a.f1573W = WXApp.WXAPP_PACKAGE_NAME;
        c0582a.f1574X = wxappPayEntryClassname;
        return C0586a.m2010a(context, c0582a);
    }

    public final void detach() {
        C0589b.m2021e(TAG, "detach");
        this.detached = true;
        if (activityCb != null && VERSION.SDK_INT >= 14) {
            if (this.context instanceof Activity) {
                ((Activity) this.context).getApplication().unregisterActivityLifecycleCallbacks(activityCb);
            } else if (this.context instanceof Service) {
                ((Service) this.context).getApplication().unregisterActivityLifecycleCallbacks(activityCb);
            }
            activityCb.detach();
        }
        this.context = null;
    }

    public final int getWXAppSupportAPI() {
        if (this.detached) {
            throw new IllegalStateException("getWXAppSupportAPI fail, WXMsgImpl has been detached");
        } else if (isWXAppInstalled()) {
            return new MMSharedPreferences(this.context).getInt("_build_info_sdk_int_", 0);
        } else {
            C0589b.m2018b(TAG, "open wx app failed, not installed or signature check failed");
            return 0;
        }
    }

    public final boolean handleIntent(Intent intent, IWXAPIEventHandler iWXAPIEventHandler) {
        try {
            if (!WXApiImplComm.isIntentFromWx(intent, Token.WX_TOKEN_VALUE_MSG)) {
                C0589b.m2020d(TAG, "handleIntent fail, intent not from weixin msg");
                return false;
            } else if (this.detached) {
                throw new IllegalStateException("handleIntent fail, WXMsgImpl has been detached");
            } else {
                String stringExtra = intent.getStringExtra(ConstantsAPI.CONTENT);
                int intExtra = intent.getIntExtra(ConstantsAPI.SDK_VERSION, 0);
                String stringExtra2 = intent.getStringExtra(ConstantsAPI.APP_PACKAGE);
                if (stringExtra2 == null || stringExtra2.length() == 0) {
                    C0589b.m2018b(TAG, "invalid argument");
                    return false;
                } else if (checkSumConsistent(intent.getByteArrayExtra(ConstantsAPI.CHECK_SUM), C0585b.m2009a(stringExtra, intExtra, stringExtra2))) {
                    int intExtra2 = intent.getIntExtra("_wxapi_command_type", 0);
                    switch (intExtra2) {
                        case StatConstants.XG_PRO_VERSION /*1*/:
                            iWXAPIEventHandler.onResp(new Resp(intent.getExtras()));
                            return true;
                        case StatConstants.USER_ONLINE_CONFIG_TYPE /*2*/:
                            iWXAPIEventHandler.onResp(new SendMessageToWX.Resp(intent.getExtras()));
                            return true;
                        case StatConstants.STAT_DB_VERSION /*3*/:
                            iWXAPIEventHandler.onReq(new Req(intent.getExtras()));
                            return true;
                        case StatAccount.WECHAT_OPENID_TYPE /*4*/:
                            iWXAPIEventHandler.onReq(new ShowMessageFromWX.Req(intent.getExtras()));
                            return true;
                        case StatAccount.PHONE_NUM_TYPE /*5*/:
                            iWXAPIEventHandler.onResp(new PayResp(intent.getExtras()));
                            return true;
                        case StatAccount.EMAIL_TYPE /*6*/:
                            iWXAPIEventHandler.onReq(new LaunchFromWX.Req(intent.getExtras()));
                            return true;
                        case ConstantsAPI.COMMAND_ADD_CARD_TO_EX_CARD_PACKAGE /*9*/:
                            iWXAPIEventHandler.onResp(new AddCardToWXCardPackage.Resp(intent.getExtras()));
                            return true;
                        case IMediaObject.TYPE_DEVICE_ACCESS /*12*/:
                            iWXAPIEventHandler.onResp(new OpenWebview.Resp(intent.getExtras()));
                            return true;
                        case IMediaObject.TYPE_OLD_TV /*14*/:
                            iWXAPIEventHandler.onResp(new CreateChatroom.Resp(intent.getExtras()));
                            return true;
                        case IMediaObject.TYPE_EMOTICON_SHARED /*15*/:
                            iWXAPIEventHandler.onResp(new JoinChatroom.Resp(intent.getExtras()));
                            return true;
                        default:
                            C0589b.m2018b(TAG, "unknown cmd = " + intExtra2);
                            return false;
                    }
                } else {
                    C0589b.m2018b(TAG, "checksum fail");
                    return false;
                }
            }
        } catch (Exception e) {
            C0589b.m2017a(TAG, "handleIntent fail, ex = %s", e.getMessage());
            return false;
        }
    }

    public final boolean isWXAppInstalled() {
        boolean z = false;
        if (this.detached) {
            throw new IllegalStateException("isWXAppInstalled fail, WXMsgImpl has been detached");
        }
        try {
            PackageInfo packageInfo = this.context.getPackageManager().getPackageInfo(WXApp.WXAPP_PACKAGE_NAME, 64);
            if (packageInfo != null) {
                z = WXApiImplComm.validateAppSignature(this.context, packageInfo.signatures, this.checkSignature);
            }
        } catch (NameNotFoundException e) {
        }
        return z;
    }

    public final boolean isWXAppSupportAPI() {
        if (!this.detached) {
            return getWXAppSupportAPI() >= Build.SDK_INT;
        } else {
            throw new IllegalStateException("isWXAppSupportAPI fail, WXMsgImpl has been detached");
        }
    }

    public final boolean openWXApp() {
        if (this.detached) {
            throw new IllegalStateException("openWXApp fail, WXMsgImpl has been detached");
        } else if (isWXAppInstalled()) {
            try {
                this.context.startActivity(this.context.getPackageManager().getLaunchIntentForPackage(WXApp.WXAPP_PACKAGE_NAME));
                return true;
            } catch (Exception e) {
                C0589b.m2018b(TAG, "startActivity fail, exception = " + e.getMessage());
                return false;
            }
        } else {
            C0589b.m2018b(TAG, "open wx app failed, not installed or signature check failed");
            return false;
        }
    }

    public final boolean registerApp(String str) {
        if (this.detached) {
            throw new IllegalStateException("registerApp fail, WXMsgImpl has been detached");
        } else if (WXApiImplComm.validateAppSignatureForPackage(this.context, WXApp.WXAPP_PACKAGE_NAME, this.checkSignature)) {
            if (activityCb == null && VERSION.SDK_INT >= 14) {
                if (this.context instanceof Activity) {
                    initMta(this.context, str);
                    activityCb = new ActivityLifecycleCb(null);
                    ((Activity) this.context).getApplication().registerActivityLifecycleCallbacks(activityCb);
                } else if (this.context instanceof Service) {
                    initMta(this.context, str);
                    activityCb = new ActivityLifecycleCb(null);
                    ((Service) this.context).getApplication().registerActivityLifecycleCallbacks(activityCb);
                } else {
                    C0589b.m2019c(TAG, "context is not instanceof Activity or Service, disable WXStat");
                }
            }
            C0589b.m2021e(TAG, "registerApp, appId = " + str);
            if (str != null) {
                this.appId = str;
            }
            C0589b.m2021e(TAG, "register app " + this.context.getPackageName());
            C0583a c0583a = new C0583a();
            c0583a.aa = WXApp.WXAPP_PACKAGE_NAME;
            c0583a.ab = ConstantsAPI.ACTION_HANDLE_APP_REGISTER;
            c0583a.f1577Y = "weixin://registerapp?appid=" + this.appId;
            return C0584a.m2008a(this.context, c0583a);
        } else {
            C0589b.m2018b(TAG, "register app failed for wechat app signature check failed");
            return false;
        }
    }

    public final boolean sendReq(BaseReq baseReq) {
        if (this.detached) {
            throw new IllegalStateException("sendReq fail, WXMsgImpl has been detached");
        } else if (!WXApiImplComm.validateAppSignatureForPackage(this.context, WXApp.WXAPP_PACKAGE_NAME, this.checkSignature)) {
            C0589b.m2018b(TAG, "sendReq failed for wechat app signature check failed");
            return false;
        } else if (baseReq.checkArgs()) {
            C0589b.m2021e(TAG, "sendReq, req type = " + baseReq.getType());
            Bundle bundle = new Bundle();
            baseReq.toBundle(bundle);
            if (baseReq.getType() == 5) {
                return sendPayReq(this.context, bundle);
            }
            if (baseReq.getType() == 7) {
                return sendJumpToBizProfileReq(this.context, bundle);
            }
            if (baseReq.getType() == 8) {
                return sendJumpToBizWebviewReq(this.context, bundle);
            }
            if (baseReq.getType() == 10) {
                return sendJumpToBizTempSessionReq(this.context, bundle);
            }
            if (baseReq.getType() == 9) {
                return sendAddCardToWX(this.context, bundle);
            }
            if (baseReq.getType() == 11) {
                return sendOpenRankListReq(this.context, bundle);
            }
            if (baseReq.getType() == 12) {
                return sendOpenWebview(this.context, bundle);
            }
            if (baseReq.getType() == 13) {
                return sendOpenBusiLuckyMoney(this.context, bundle);
            }
            if (baseReq.getType() == 14) {
                return createChatroom(this.context, bundle);
            }
            if (baseReq.getType() == 15) {
                return joinChatroom(this.context, bundle);
            }
            C0582a c0582a = new C0582a();
            c0582a.f1576Z = bundle;
            c0582a.f1575Y = "weixin://sendreq?appid=" + this.appId;
            c0582a.f1573W = WXApp.WXAPP_PACKAGE_NAME;
            c0582a.f1574X = WXApp.WXAPP_MSG_ENTRY_CLASSNAME;
            return C0586a.m2010a(this.context, c0582a);
        } else {
            C0589b.m2018b(TAG, "sendReq checkArgs fail");
            return false;
        }
    }

    public final boolean sendResp(BaseResp baseResp) {
        if (this.detached) {
            throw new IllegalStateException("sendResp fail, WXMsgImpl has been detached");
        } else if (!WXApiImplComm.validateAppSignatureForPackage(this.context, WXApp.WXAPP_PACKAGE_NAME, this.checkSignature)) {
            C0589b.m2018b(TAG, "sendResp failed for wechat app signature check failed");
            return false;
        } else if (baseResp.checkArgs()) {
            Bundle bundle = new Bundle();
            baseResp.toBundle(bundle);
            C0582a c0582a = new C0582a();
            c0582a.f1576Z = bundle;
            c0582a.f1575Y = "weixin://sendresp?appid=" + this.appId;
            c0582a.f1573W = WXApp.WXAPP_PACKAGE_NAME;
            c0582a.f1574X = WXApp.WXAPP_MSG_ENTRY_CLASSNAME;
            return C0586a.m2010a(this.context, c0582a);
        } else {
            C0589b.m2018b(TAG, "sendResp checkArgs fail");
            return false;
        }
    }

    public final void unregisterApp() {
        if (this.detached) {
            throw new IllegalStateException("unregisterApp fail, WXMsgImpl has been detached");
        } else if (WXApiImplComm.validateAppSignatureForPackage(this.context, WXApp.WXAPP_PACKAGE_NAME, this.checkSignature)) {
            C0589b.m2021e(TAG, "unregisterApp, appId = " + this.appId);
            if (this.appId == null || this.appId.length() == 0) {
                C0589b.m2018b(TAG, "unregisterApp fail, appId is empty");
                return;
            }
            C0589b.m2021e(TAG, "unregister app " + this.context.getPackageName());
            C0583a c0583a = new C0583a();
            c0583a.aa = WXApp.WXAPP_PACKAGE_NAME;
            c0583a.ab = ConstantsAPI.ACTION_HANDLE_APP_UNREGISTER;
            c0583a.f1577Y = "weixin://unregisterapp?appid=" + this.appId;
            C0584a.m2008a(this.context, c0583a);
        } else {
            C0589b.m2018b(TAG, "unregister app failed for wechat app signature check failed");
        }
    }
}
