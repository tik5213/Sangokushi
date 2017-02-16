package com.tencent.android.tpush.encrypt;

import com.sina.weibo.sdk.register.mobile.LetterIndexBar;
import com.tencent.android.tpush.service.channel.security.TpnsSecurity;
import com.tencent.android.tpush.service.p077d.ProGuard;

/* compiled from: ProGuard */
public class Rijndael {
    public static String encrypt(String str) {
        if (!ProGuard.m5317a(str)) {
            String oiSymmetryEncrypt2 = TpnsSecurity.oiSymmetryEncrypt2(str);
            int i = 0;
            while (i < 3) {
                if (!"failed".equals(oiSymmetryEncrypt2)) {
                    return oiSymmetryEncrypt2;
                }
                i++;
                oiSymmetryEncrypt2 = TpnsSecurity.oiSymmetryEncrypt2(str);
            }
        }
        return LetterIndexBar.SEARCH_ICON_LETTER;
    }

    public static String decrypt(String str) {
        if (!ProGuard.m5317a(str)) {
            String oiSymmetryDecrypt2 = TpnsSecurity.oiSymmetryDecrypt2(str);
            int i = 0;
            while (i < 3) {
                if (!"failed".equals(oiSymmetryDecrypt2)) {
                    return oiSymmetryDecrypt2;
                }
                i++;
                oiSymmetryDecrypt2 = TpnsSecurity.oiSymmetryDecrypt2(str);
            }
        }
        return LetterIndexBar.SEARCH_ICON_LETTER;
    }
}