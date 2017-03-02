package com.tencent.open;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.BaseApi;
import com.tencent.connect.common.Constants;
import com.tencent.connect.common.UIListenerManager;
import com.tencent.open.utils.Global;
import com.tencent.open.utils.OpenConfig;
import com.tencent.open.utils.ServerSetting;
import com.tencent.open.utils.SystemUtils;
import com.tencent.open.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ProGuard */
public class SocialApiIml extends BaseApi {
    private Activity f1586a;

    /* renamed from: com.tencent.open.SocialApiIml.a */
    private class ProGuard implements IUiListener {
        final /* synthetic */ SocialApiIml f1580a;
        private IUiListener f1581b;
        private String f1582c;
        private String f1583d;
        private Bundle f1584e;
        private Activity f1585f;

        ProGuard(SocialApiIml socialApiIml, Activity activity, IUiListener iUiListener, String str, String str2, Bundle bundle) {
            this.f1580a = socialApiIml;
            this.f1581b = iUiListener;
            this.f1582c = str;
            this.f1583d = str2;
            this.f1584e = bundle;
        }

        public void onComplete(Object obj) {
            CharSequence string;
            CharSequence charSequence = null;
            try {
                string = ((JSONObject) obj).getString(SocialConstants.PARAM_ENCRY_EOKEN);
            } catch (Throwable e) {
                e.printStackTrace();
                com.tencent.open.p067a.ProGuard.m2118b("openSDK_LOG.SocialApiIml", "OpenApi, EncrytokenListener() onComplete error", e);
                string = charSequence;
            }
            this.f1584e.putString("encrytoken", string);
            this.f1580a.m2051a(this.f1580a.f1586a, this.f1582c, this.f1584e, this.f1583d, this.f1581b);
            if (TextUtils.isEmpty(string)) {
                com.tencent.open.p067a.ProGuard.m2117b("openSDK_LOG.SocialApiIml", "The token get from qq or qzone is empty. Write temp token to localstorage.");
                this.f1580a.writeEncryToken(this.f1585f);
            }
        }

        public void onError(UiError uiError) {
            com.tencent.open.p067a.ProGuard.m2117b("openSDK_LOG.SocialApiIml", "OpenApi, EncryptTokenListener() onError" + uiError.errorMessage);
            this.f1581b.onError(uiError);
        }

        public void onCancel() {
            this.f1581b.onCancel();
        }
    }

    public SocialApiIml(QQToken qQToken) {
        super(qQToken);
    }

    public SocialApiIml(QQAuth qQAuth, QQToken qQToken) {
        super(qQAuth, qQToken);
    }

    public void gift(Activity activity, Bundle bundle, IUiListener iUiListener) {
        m2049a(activity, SocialConstants.ACTION_GIFT, bundle, iUiListener);
    }

    public void ask(Activity activity, Bundle bundle, IUiListener iUiListener) {
        m2049a(activity, SocialConstants.ACTION_ASK, bundle, iUiListener);
    }

    private void m2049a(Activity activity, String str, Bundle bundle, IUiListener iUiListener) {
        this.f1586a = activity;
        Intent agentIntentWithTarget = getAgentIntentWithTarget(SocialConstants.ACTIVITY_FRIEND_CHOOSER);
        if (agentIntentWithTarget == null) {
            com.tencent.open.p067a.ProGuard.m2120c("openSDK_LOG.SocialApiIml", "--askgift--friend chooser not found");
            agentIntentWithTarget = getAgentIntentWithTarget(SocialConstants.ACTIVITY_ASK_GIFT);
        }
        bundle.putAll(composeActivityParams());
        if (SocialConstants.ACTION_ASK.equals(str)) {
            bundle.putString(SocialConstants.PARAM_TYPE, SocialConstants.TYPE_REQUEST);
        } else if (SocialConstants.ACTION_GIFT.equals(str)) {
            bundle.putString(SocialConstants.PARAM_TYPE, SocialConstants.TYPE_FREEGIFT);
        }
        m2048a(activity, agentIntentWithTarget, str, bundle, ServerSetting.getInstance().getEnvUrl(Global.getContext(), ServerSetting.DEFAULT_URL_GIFT), iUiListener, false);
    }

    public void invite(Activity activity, Bundle bundle, IUiListener iUiListener) {
        this.f1586a = activity;
        Intent agentIntentWithTarget = getAgentIntentWithTarget(SocialConstants.ACTIVITY_FRIEND_CHOOSER);
        if (agentIntentWithTarget == null) {
            com.tencent.open.p067a.ProGuard.m2120c("openSDK_LOG.SocialApiIml", "--invite--friend chooser not found");
            agentIntentWithTarget = getAgentIntentWithTarget(SocialConstants.ACTIVITY_INVITE);
        }
        bundle.putAll(composeActivityParams());
        Activity activity2 = activity;
        m2048a(activity2, agentIntentWithTarget, SocialConstants.ACTION_INVITE, bundle, ServerSetting.getInstance().getEnvUrl(Global.getContext(), ServerSetting.DEFAULT_URL_INVITE), iUiListener, false);
    }

    public void story(Activity activity, Bundle bundle, IUiListener iUiListener) {
        this.f1586a = activity;
        Intent agentIntentWithTarget = getAgentIntentWithTarget(SocialConstants.ACTIVITY_STORY);
        bundle.putAll(composeActivityParams());
        Activity activity2 = activity;
        m2048a(activity2, agentIntentWithTarget, SocialConstants.ACTION_STORY, bundle, ServerSetting.getInstance().getEnvUrl(Global.getContext(), ServerSetting.DEFAULT_URL_SEND_STORY), iUiListener, false);
    }

    private void m2048a(Activity activity, Intent intent, String str, Bundle bundle, String str2, IUiListener iUiListener, boolean z) {
        Object obj = null;
        com.tencent.open.p067a.ProGuard.m2120c("openSDK_LOG.SocialApiIml", "-->handleIntent action = " + str + ", activityIntent = null ? " + (intent == null));
        if (intent != null) {
            m2047a(activity, intent, str, bundle, iUiListener);
            return;
        }
        OpenConfig instance = OpenConfig.getInstance(Global.getContext(), this.mToken.getAppId());
        if (z || instance.getBoolean("C_LoginH5")) {
            obj = 1;
        }
        if (obj != null) {
            m2050a(activity, str, bundle, str2, iUiListener);
        } else {
            handleDownloadLastestQQ(activity, bundle, iUiListener);
        }
    }

    private void m2047a(Activity activity, Intent intent, String str, Bundle bundle, IUiListener iUiListener) {
        com.tencent.open.p067a.ProGuard.m2120c("openSDK_LOG.SocialApiIml", "-->handleIntentWithAgent action = " + str);
        intent.putExtra(Constants.KEY_ACTION, str);
        intent.putExtra(Constants.KEY_PARAMS, bundle);
        UIListenerManager.getInstance().setListenerWithRequestcode(Constants.REQUEST_SOCIAL_API, iUiListener);
        startAssitActivity(activity, intent, (int) Constants.REQUEST_SOCIAL_API);
    }

    private void m2050a(Activity activity, String str, Bundle bundle, String str2, IUiListener iUiListener) {
        com.tencent.open.p067a.ProGuard.m2120c("openSDK_LOG.SocialApiIml", "-->handleIntentWithH5 action = " + str);
        Intent targetActivityIntent = getTargetActivityIntent("com.tencent.open.agent.AgentActivity");
        Object proGuard = new ProGuard(this, activity, iUiListener, str, str2, bundle);
        Intent targetActivityIntent2 = getTargetActivityIntent("com.tencent.open.agent.EncryTokenActivity");
        if (targetActivityIntent2 == null || targetActivityIntent == null || targetActivityIntent.getComponent() == null || targetActivityIntent2.getComponent() == null || !targetActivityIntent.getComponent().getPackageName().equals(targetActivityIntent2.getComponent().getPackageName())) {
            com.tencent.open.p067a.ProGuard.m2120c("openSDK_LOG.SocialApiIml", "-->handleIntentWithH5--token activity not found");
            String encrypt = Util.encrypt("tencent&sdk&qazxc***14969%%" + this.mToken.getAccessToken() + this.mToken.getAppId() + this.mToken.getOpenId() + "qzone3.4");
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(SocialConstants.PARAM_ENCRY_EOKEN, encrypt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            proGuard.onComplete(jSONObject);
            return;
        }
        targetActivityIntent2.putExtra(SocialConstants.PARAM_CONSUMER_KEY, this.mToken.getAppId());
        targetActivityIntent2.putExtra(SocialConstants.PARAM_OPEN_ID, this.mToken.getOpenId());
        targetActivityIntent2.putExtra(Constants.PARAM_ACCESS_TOKEN, this.mToken.getAccessToken());
        targetActivityIntent2.putExtra(Constants.KEY_ACTION, SocialConstants.ACTION_CHECK_TOKEN);
        if (hasActivityForIntent(targetActivityIntent2)) {
            com.tencent.open.p067a.ProGuard.m2120c("openSDK_LOG.SocialApiIml", "-->handleIntentWithH5--found token activity");
            UIListenerManager.getInstance().setListenerWithRequestcode(Constants.REQUEST_SOCIAL_H5, proGuard);
            startAssitActivity(activity, targetActivityIntent2, (int) Constants.REQUEST_SOCIAL_H5);
        }
    }

    private void m2051a(Context context, String str, Bundle bundle, String str2, IUiListener iUiListener) {
        com.tencent.open.p067a.ProGuard.m2114a("openSDK_LOG.SocialApiIml", "OpenUi, showDialog --start");
        CookieSyncManager.createInstance(context);
        bundle.putString(SocialConstants.PARAM_CONSUMER_KEY, this.mToken.getAppId());
        if (this.mToken.isSessionValid()) {
            bundle.putString(Constants.PARAM_ACCESS_TOKEN, this.mToken.getAccessToken());
        }
        String openId = this.mToken.getOpenId();
        if (openId != null) {
            bundle.putString(SocialConstants.PARAM_OPEN_ID, openId);
        }
        try {
            bundle.putString(Constants.PARAM_PLATFORM_ID, Global.getContext().getSharedPreferences(Constants.PREFERENCE_PF, 0).getString(Constants.PARAM_PLATFORM_ID, Constants.DEFAULT_PF));
        } catch (Exception e) {
            e.printStackTrace();
            bundle.putString(Constants.PARAM_PLATFORM_ID, Constants.DEFAULT_PF);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str2);
        stringBuilder.append(Util.encodeUrl(bundle));
        String stringBuilder2 = stringBuilder.toString();
        com.tencent.open.p067a.ProGuard.m2117b("openSDK_LOG.SocialApiIml", "OpenUi, showDialog TDialog");
        if (SocialConstants.ACTION_CHALLENGE.equals(str) || SocialConstants.ACTION_BRAG.equals(str)) {
            com.tencent.open.p067a.ProGuard.m2117b("openSDK_LOG.SocialApiIml", "OpenUi, showDialog PKDialog");
            new PKDialog(this.f1586a, str, stringBuilder2, iUiListener, this.mToken).show();
            return;
        }
        new TDialog(this.f1586a, str, stringBuilder2, iUiListener, this.mToken).show();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public void writeEncryToken(Context context) {
        String str = "tencent&sdk&qazxc***14969%%";
        String accessToken = this.mToken.getAccessToken();
        String appId = this.mToken.getAppId();
        String openId = this.mToken.getOpenId();
        String str2 = "qzone3.4";
        if (accessToken == null || accessToken.length() <= 0 || appId == null || appId.length() <= 0 || openId == null || openId.length() <= 0) {
            str = null;
        } else {
            str = Util.encrypt(str + accessToken + appId + openId + str2);
        }
        com.tencent.open.p066c.ProGuard proGuard = new com.tencent.open.p066c.ProGuard(context);
        WebSettings settings = proGuard.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        accessToken = "<!DOCTYPE HTML><html lang=\"en-US\"><head><meta charset=\"UTF-8\"><title>localStorage Test</title><script type=\"text/javascript\">document.domain = 'qq.com';localStorage[\"" + this.mToken.getOpenId() + "_" + this.mToken.getAppId() + "\"]=\"" + str + "\";</script></head><body></body></html>";
        str = ServerSetting.getInstance().getEnvUrl(context, ServerSetting.DEFAULT_LOCAL_STORAGE_URI);
        proGuard.loadDataWithBaseURL(str, accessToken, "text/html", "utf-8", str);
    }

    protected Intent getTargetActivityIntent(String str) {
        Intent intent = new Intent();
        intent.setClassName(Constants.PACKAGE_QZONE, str);
        Intent intent2 = new Intent();
        intent2.setClassName(Constants.PACKAGE_QQ, str);
        if (SystemUtils.isActivityExist(Global.getContext(), intent2) && SystemUtils.compareQQVersion(Global.getContext(), "4.7") >= 0) {
            return intent2;
        }
        if (!SystemUtils.isActivityExist(Global.getContext(), intent) || SystemUtils.compareVersion(SystemUtils.getAppVersionName(Global.getContext(), Constants.PACKAGE_QZONE), "4.2") < 0) {
            return null;
        }
        if (SystemUtils.isAppSignatureValid(Global.getContext(), intent.getComponent().getPackageName(), Constants.SIGNATRUE_QZONE)) {
            return intent;
        }
        return null;
    }
}
