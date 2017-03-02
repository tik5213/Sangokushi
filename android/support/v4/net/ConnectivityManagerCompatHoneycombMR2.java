package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.wxop.stat.StatAccount;
import com.tencent.wxop.stat.common.StatConstants;

class ConnectivityManagerCompatHoneycombMR2 {
    ConnectivityManagerCompatHoneycombMR2() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return true;
        }
        switch (info.getType()) {
            case StatAccount.DEFAULT_TYPE /*0*/:
            case StatConstants.USER_ONLINE_CONFIG_TYPE /*2*/:
            case StatConstants.STAT_DB_VERSION /*3*/:
            case StatAccount.WECHAT_OPENID_TYPE /*4*/:
            case StatAccount.PHONE_NUM_TYPE /*5*/:
            case StatAccount.EMAIL_TYPE /*6*/:
                return true;
            case StatConstants.XG_PRO_VERSION /*1*/:
            case StatAccount.CUSTOM_TYPE /*7*/:
            case ConstantsAPI.COMMAND_ADD_CARD_TO_EX_CARD_PACKAGE /*9*/:
                return false;
            default:
                return true;
        }
    }
}