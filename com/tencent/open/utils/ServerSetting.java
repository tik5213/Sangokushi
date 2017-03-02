package com.tencent.open.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;
import com.tencent.open.p067a.ProGuard;
import java.lang.ref.WeakReference;
import java.net.URL;

/* compiled from: ProGuard */
public class ServerSetting {
    public static final String APP_DETAIL_PAGE = "http://fusion.qq.com/cgi-bin/qzapps/unified_jump?appid=%1$s&from=%2$s&isOpenAppID=1";
    public static final String CGI_FETCH_QQ_URL = "http://fusion.qq.com/cgi-bin/qzapps/mapp_getappinfo.cgi";
    public static final String DEFAULT_CGI_AUTHORIZE = "http://openmobile.qq.com/oauth2.0/m_authorize?";
    public static final String DEFAULT_LOCAL_STORAGE_URI = "http://qzs.qq.com";
    public static final String DEFAULT_REDIRECT_URI = "auth://tauth.qq.com/";
    public static final String DEFAULT_URL_ASK = "http://qzs.qq.com/open/mobile/request/sdk_request.html?";
    public static final String DEFAULT_URL_BRAG = "http://qzs.qq.com/open/mobile/brag/sdk_brag.html?";
    public static final String DEFAULT_URL_GIFT = "http://qzs.qq.com/open/mobile/request/sdk_request.html?";
    public static final String DEFAULT_URL_GRAPH_BASE = "https://openmobile.qq.com/";
    public static final String DEFAULT_URL_INVITE = "http://qzs.qq.com/open/mobile/invite/sdk_invite.html?";
    public static final String DEFAULT_URL_REACTIVE = "http://qzs.qq.com/open/mobile/reactive/sdk_reactive.html?";
    public static final String DEFAULT_URL_REPORT = "http://wspeed.qq.com/w.cgi";
    public static final String DEFAULT_URL_SEND_STORY = "http://qzs.qq.com/open/mobile/sendstory/sdk_sendstory_v1.3.html?";
    public static final String DEFAULT_URL_VOICE = "http://qzs.qq.com/open/mobile/not_support.html?";
    public static final String DOWNLOAD_QQ_URL = "http://qzs.qq.com/open/mobile/login/qzsjump.html?";
    public static final String DOWNLOAD_QQ_URL_COMMON = "http://qzs.qq.com/open/mobile/sdk_common/down_qq.htm?";
    public static final int ENVIRONMENT_EXPERIENCE = 1;
    public static final int ENVIRONMENT_NORMOL = 0;
    public static final String KEY_HOST_ANALY = "analy.qq.com";
    public static final String KEY_HOST_APPIC = "appic.qq.com";
    public static final String KEY_HOST_APP_SUPPORT = "appsupport.qq.com";
    public static final String KEY_HOST_FUSION = "fusion.qq.com";
    public static final String KEY_HOST_I_GTIMG = "i.gtimg.cn";
    public static final String KEY_HOST_MAPP_QZONE = "mapp.qzone.qq.com";
    public static final String KEY_HOST_OPEN_MOBILE = "openmobile.qq.com";
    public static final String KEY_HOST_QZAPP_QLOGO = "qzapp.qlogo.cn";
    public static final String KEY_HOST_QZS_QQ = "qzs.qq.com";
    public static final String KEY_OPEN_ENV = "OpenEnvironment";
    public static final String KEY_OPEN_SETTING = "OpenSettings";
    public static final String NEED_QQ_VERSION_TIPS_URL = "http://openmobile.qq.com/oauth2.0/m_jump_by_version?";
    public static final String URL_FUSION_BASE = "http://fusion.qq.com";
    public static final String URL_FUSION_CGI_BASE = "http://fusion.qq.com/cgi-bin";
    public static final String URL_PRIZE_EXCHANGE = "http://fusion.qq.com/cgi-bin/prize_sharing/exchange_prize.cgi";
    public static final String URL_PRIZE_GET_ACTIVITY_STATE = "http://fusion.qq.com/cgi-bin/prize_sharing/get_activity_state.cgi";
    public static final String URL_PRIZE_MAKE_SHARE_URL = "http://fusion.qq.com/cgi-bin/prize_sharing/make_share_url.cgi";
    public static final String URL_PRIZE_QUERY_UNEXCHANGE = "http://fusion.qq.com/cgi-bin/prize_sharing/query_unexchange_prize.cgi";
    private static ServerSetting f1728a;
    private volatile WeakReference<SharedPreferences> f1729b;

    public ServerSetting() {
        this.f1729b = null;
    }

    static {
        f1728a = null;
    }

    public static synchronized ServerSetting getInstance() {
        ServerSetting serverSetting;
        synchronized (ServerSetting.class) {
            if (f1728a == null) {
                f1728a = new ServerSetting();
            }
            serverSetting = f1728a;
        }
        return serverSetting;
    }

    public void changeServer() {
        this.f1729b = null;
    }

    public String getEnvUrl(Context context, String str) {
        if (this.f1729b == null || this.f1729b.get() == null) {
            this.f1729b = new WeakReference(context.getSharedPreferences("ServerPrefs", ENVIRONMENT_NORMOL));
        }
        try {
            Object host = new URL(str).getHost();
            if (host == null) {
                ProGuard.m2122e("openSDK_LOG.ServerSetting", "Get host error. url=" + str);
            } else {
                Object string = ((SharedPreferences) this.f1729b.get()).getString(host, null);
                if (string == null || host.equals(string)) {
                    ProGuard.m2114a("openSDK_LOG.ServerSetting", "host=" + host + ", envHost=" + string);
                } else {
                    str = str.replace(host, string);
                    ProGuard.m2114a("openSDK_LOG.ServerSetting", "return environment url : " + str);
                }
            }
        } catch (Exception e) {
            ProGuard.m2122e("openSDK_LOG.ServerSetting", "getEnvUrl url=" + str + "error.: " + e.getMessage());
        }
        return str;
    }

    public void setEnvironment(Context context, int i) {
        if (context != null && (this.f1729b == null || this.f1729b.get() == null)) {
            this.f1729b = new WeakReference(context.getSharedPreferences("ServerPrefs", ENVIRONMENT_NORMOL));
        }
        if (i == 0 || i == ENVIRONMENT_EXPERIENCE) {
            Editor edit;
            switch (i) {
                case ENVIRONMENT_NORMOL /*0*/:
                    edit = ((SharedPreferences) this.f1729b.get()).edit();
                    if (edit != null) {
                        edit.putInt("ServerType", ENVIRONMENT_NORMOL);
                        edit.putString(KEY_OPEN_ENV, "formal");
                        edit.putString(KEY_HOST_QZS_QQ, KEY_HOST_QZS_QQ);
                        edit.putString(KEY_HOST_OPEN_MOBILE, KEY_HOST_OPEN_MOBILE);
                        edit.commit();
                        changeServer();
                        Toast.makeText(context, "\u5df2\u5207\u6362\u5230\u6b63\u5f0f\u73af\u5883", ENVIRONMENT_NORMOL).show();
                        return;
                    }
                    return;
                case ENVIRONMENT_EXPERIENCE /*1*/:
                    edit = ((SharedPreferences) this.f1729b.get()).edit();
                    if (edit != null) {
                        edit.putInt("ServerType", ENVIRONMENT_EXPERIENCE);
                        edit.putString(KEY_OPEN_ENV, "exp");
                        edit.putString(KEY_HOST_QZS_QQ, "testmobile.qq.com");
                        edit.putString(KEY_HOST_OPEN_MOBILE, "test.openmobile.qq.com");
                        edit.commit();
                        changeServer();
                        Toast.makeText(context, "\u5df2\u5207\u6362\u5230\u4f53\u9a8c\u73af\u5883", ENVIRONMENT_NORMOL).show();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
        ProGuard.m2122e("openSDK_LOG.ServerSetting", "\u5207\u6362\u73af\u5883\u53c2\u6570\u9519\u8bef\uff0c\u6b63\u5f0f\u73af\u5883\u4e3a0\uff0c\u4f53\u9a8c\u73af\u5883\u4e3a1");
    }
}
