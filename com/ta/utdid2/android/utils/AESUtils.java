package com.ta.utdid2.android.utils;

import com.tencent.connect.common.Constants;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    public static final String TAG = "AESUtils";
    public static byte[] f1451t;

    static {
        f1451t = new byte[]{(byte) 48, (byte) 48, (byte) 49, (byte) 55, (byte) 68, (byte) 67, (byte) 49, (byte) 66, (byte) 69, (byte) 50, (byte) 50, (byte) 53, (byte) 56, (byte) 53, (byte) 53, (byte) 52, (byte) 67, (byte) 70, (byte) 48, (byte) 50, (byte) 67, (byte) 53, (byte) 55, (byte) 66, (byte) 55, (byte) 56, (byte) 69, (byte) 55, (byte) 52, (byte) 48, (byte) 65, (byte) 53};
    }

    public static String encrypt(String seed, String clearText) {
        byte[] result = null;
        try {
            result = encrypt(getRawKey(seed.getBytes()), clearText.getBytes());
        } catch (Exception e) {
        }
        if (result != null) {
            return toHex(result);
        }
        return null;
    }

    public static String decrypt(String seed, String encrypted) {
        try {
            return new String(decrypt(getRawKey(seed.getBytes()), toByte(encrypted)));
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        return toByte(new String(f1451t, 0, 32));
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(clear);
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(encrypted);
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(i * 2, (i * 2) + 2), 16).byteValue();
        }
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null) {
            return Constants.STR_EMPTY;
        }
        StringBuffer result = new StringBuffer(buf.length * 2);
        for (byte appendHex : buf) {
            appendHex(result, appendHex);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        String HEX = "0123456789ABCDEF";
        sb.append("0123456789ABCDEF".charAt((b >> 4) & 15)).append("0123456789ABCDEF".charAt(b & 15));
    }
}
