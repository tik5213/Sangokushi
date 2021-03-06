package com.ta.utdid2.device;

import android.content.Context;
import android.provider.Settings.System;
import com.ta.utdid2.android.utils.Base64;
import com.ta.utdid2.android.utils.IntUtils;
import com.ta.utdid2.android.utils.PhoneInfoUtils;
import com.ta.utdid2.android.utils.StringUtils;
import com.ta.utdid2.core.persistent.PersistentConfiguration;
import com.tencent.connect.common.Constants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class UTUtdid {
    private static final Object CREATE_LOCK;
    private static final String HMAC_KEY = "d6fc3a4a06adbde89223bvefedc24fecde188aaa9161";
    private static final String S_GLOBAL_PERSISTENT_CONFIG_DIR;
    private static final String S_GLOBAL_PERSISTENT_CONFIG_KEY = "Alvin2";
    private static final String S_LOCAL_STORAGE_KEY = "ContextData";
    private static final String S_LOCAL_STORAGE_NAME = ".DataStorage";
    static final String UM_SETTINGS_STORAGE = "dxCRMxhQkdGePGnp";
    static final String UM_SETTINGS_STORAGE_NEW = "mqBRboGZkQPcAkyk";
    private static UTUtdid s_umutdid;
    private String mCBDomain;
    private String mCBKey;
    private Context mContext;
    private PersistentConfiguration mPC;
    private Pattern mPattern;
    private PersistentConfiguration mTaoPC;
    private String mUtdid;
    private UTUtdidHelper mUtdidHelper;

    static {
        CREATE_LOCK = new Object();
        s_umutdid = null;
        S_GLOBAL_PERSISTENT_CONFIG_DIR = ".UTSystemConfig" + File.separator + "Global";
    }

    public UTUtdid(Context context) {
        this.mContext = null;
        this.mUtdid = null;
        this.mUtdidHelper = null;
        this.mCBKey = "xx_utdid_key";
        this.mCBDomain = "xx_utdid_domain";
        this.mPC = null;
        this.mTaoPC = null;
        this.mPattern = Pattern.compile("[^0-9a-zA-Z=/+]+");
        this.mContext = context;
        this.mTaoPC = new PersistentConfiguration(context, S_GLOBAL_PERSISTENT_CONFIG_DIR, S_GLOBAL_PERSISTENT_CONFIG_KEY, false, true);
        this.mPC = new PersistentConfiguration(context, S_LOCAL_STORAGE_NAME, S_LOCAL_STORAGE_KEY, false, true);
        this.mUtdidHelper = new UTUtdidHelper();
        this.mCBKey = String.format("K_%d", new Object[]{Integer.valueOf(StringUtils.hashCode(this.mCBKey))});
        this.mCBDomain = String.format("D_%d", new Object[]{Integer.valueOf(StringUtils.hashCode(this.mCBDomain))});
    }

    private void _removeIllegalKeys() {
        if (this.mTaoPC != null) {
            if (StringUtils.isEmpty(this.mTaoPC.getString("UTDID2"))) {
                String lUtdid = this.mTaoPC.getString("UTDID");
                if (!StringUtils.isEmpty(lUtdid)) {
                    saveUtdidToTaoPPC(lUtdid);
                }
            }
            boolean lNeedSync = false;
            if (!StringUtils.isEmpty(this.mTaoPC.getString("DID"))) {
                this.mTaoPC.remove("DID");
                lNeedSync = true;
            }
            if (!StringUtils.isEmpty(this.mTaoPC.getString("EI"))) {
                this.mTaoPC.remove("EI");
                lNeedSync = true;
            }
            if (!StringUtils.isEmpty(this.mTaoPC.getString("SI"))) {
                this.mTaoPC.remove("SI");
                lNeedSync = true;
            }
            if (lNeedSync) {
                this.mTaoPC.commit();
            }
        }
    }

    public static UTUtdid instance(Context context) {
        if (context != null && s_umutdid == null) {
            synchronized (CREATE_LOCK) {
                if (s_umutdid == null) {
                    s_umutdid = new UTUtdid(context);
                    s_umutdid._removeIllegalKeys();
                }
            }
        }
        return s_umutdid;
    }

    private void saveUtdidToTaoPPC(String pUtdid) {
        if (isValidUTDID(pUtdid)) {
            if (pUtdid.endsWith("\n")) {
                pUtdid = pUtdid.substring(0, pUtdid.length() - 1);
            }
            if (pUtdid.length() == 24 && this.mTaoPC != null) {
                this.mTaoPC.putString("UTDID2", pUtdid);
                this.mTaoPC.commit();
            }
        }
    }

    private void saveUtdidToLocalStorage(String pPackedUtdid) {
        if (pPackedUtdid != null && this.mPC != null && !pPackedUtdid.equals(this.mPC.getString(this.mCBKey))) {
            this.mPC.putString(this.mCBKey, pPackedUtdid);
            this.mPC.commit();
        }
    }

    private void saveUtdidToNewSettings(String lUtdid) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SETTINGS") == 0 && isValidUTDID(lUtdid)) {
            if (lUtdid.endsWith("\n")) {
                lUtdid = lUtdid.substring(0, lUtdid.length() - 1);
            }
            if (24 == lUtdid.length()) {
                String data = null;
                try {
                    data = System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE_NEW);
                } catch (Exception e) {
                }
                if (!isValidUTDID(data)) {
                    try {
                        System.putString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE_NEW, lUtdid);
                    } catch (Exception e2) {
                    }
                }
            }
        }
    }

    private void syncUTDIDToSettings(String pPackedUtdid) {
        String data = null;
        try {
            data = System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE);
        } catch (Exception e) {
        }
        if (!pPackedUtdid.equals(data)) {
            try {
                System.putString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE, pPackedUtdid);
            } catch (Exception e2) {
            }
        }
    }

    private void saveUtdidToSettings(String lPackedUtdid) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SETTINGS") == 0 && lPackedUtdid != null) {
            syncUTDIDToSettings(lPackedUtdid);
        }
    }

    private String getUtdidFromTaoPPC() {
        if (this.mTaoPC != null) {
            String lUTDID = this.mTaoPC.getString("UTDID2");
            if (!(StringUtils.isEmpty(lUTDID) || this.mUtdidHelper.packUtdidStr(lUTDID) == null)) {
                return lUTDID;
            }
        }
        return null;
    }

    private boolean isValidUTDID(String pUTDID) {
        if (pUTDID == null) {
            return false;
        }
        if (pUTDID.endsWith("\n")) {
            pUTDID = pUTDID.substring(0, pUTDID.length() - 1);
        }
        if (24 != pUTDID.length() || this.mPattern.matcher(pUTDID).find()) {
            return false;
        }
        return true;
    }

    public synchronized String getValue() {
        String str;
        if (this.mUtdid != null) {
            str = this.mUtdid;
        } else {
            str = Constants.STR_EMPTY;
            try {
                str = System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE_NEW);
            } catch (Exception e) {
            }
            if (!isValidUTDID(str)) {
                UTUtdidHelper2 lHelper2 = new UTUtdidHelper2();
                boolean lNeedUpdateSettings = false;
                String data = null;
                try {
                    data = System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE);
                } catch (Exception e2) {
                }
                if (StringUtils.isEmpty(data)) {
                    lNeedUpdateSettings = true;
                } else {
                    String lTmpUtdidBase64 = lHelper2.dePackWithBase64(data);
                    if (isValidUTDID(lTmpUtdidBase64)) {
                        saveUtdidToNewSettings(lTmpUtdidBase64);
                        str = lTmpUtdidBase64;
                    } else {
                        String lTmpUtdid = lHelper2.dePack(data);
                        if (isValidUTDID(lTmpUtdid)) {
                            String lPTmpUtdid = this.mUtdidHelper.packUtdidStr(lTmpUtdid);
                            if (!StringUtils.isEmpty(lPTmpUtdid)) {
                                saveUtdidToSettings(lPTmpUtdid);
                                try {
                                    data = System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE);
                                } catch (Exception e3) {
                                }
                            }
                        }
                        String lDePackedUtdid = this.mUtdidHelper.dePack(data);
                        if (isValidUTDID(lDePackedUtdid)) {
                            this.mUtdid = lDePackedUtdid;
                            saveUtdidToTaoPPC(lDePackedUtdid);
                            saveUtdidToLocalStorage(data);
                            saveUtdidToNewSettings(this.mUtdid);
                            str = this.mUtdid;
                        }
                    }
                }
                String lSUtdid = getUtdidFromTaoPPC();
                String lPackedUtdid;
                if (isValidUTDID(lSUtdid)) {
                    lPackedUtdid = this.mUtdidHelper.packUtdidStr(lSUtdid);
                    if (lNeedUpdateSettings) {
                        saveUtdidToSettings(lPackedUtdid);
                    }
                    saveUtdidToNewSettings(lSUtdid);
                    saveUtdidToLocalStorage(lPackedUtdid);
                    this.mUtdid = lSUtdid;
                    str = lSUtdid;
                } else {
                    String lContent = this.mPC.getString(this.mCBKey);
                    if (!StringUtils.isEmpty(lContent)) {
                        String lUtdid = lHelper2.dePack(lContent);
                        if (!isValidUTDID(lUtdid)) {
                            lUtdid = this.mUtdidHelper.dePack(lContent);
                        }
                        if (isValidUTDID(lUtdid)) {
                            String lBUtdid = this.mUtdidHelper.packUtdidStr(lUtdid);
                            if (!StringUtils.isEmpty(lUtdid)) {
                                this.mUtdid = lUtdid;
                                if (lNeedUpdateSettings) {
                                    saveUtdidToSettings(lBUtdid);
                                }
                                saveUtdidToTaoPPC(this.mUtdid);
                                str = this.mUtdid;
                            }
                        }
                    }
                    try {
                        byte[] lUtdid2 = _generateUtdid();
                        if (lUtdid2 != null) {
                            this.mUtdid = Base64.encodeToString(lUtdid2, 2);
                            saveUtdidToTaoPPC(this.mUtdid);
                            lPackedUtdid = this.mUtdidHelper.pack(lUtdid2);
                            if (lPackedUtdid != null) {
                                if (lNeedUpdateSettings) {
                                    saveUtdidToSettings(lPackedUtdid);
                                }
                                saveUtdidToLocalStorage(lPackedUtdid);
                            }
                            str = this.mUtdid;
                        }
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    str = null;
                }
            }
        }
        return str;
    }

    private final byte[] _generateUtdid() throws Exception {
        String imei;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        int uniqueID = new Random().nextInt();
        byte[] bTimestamp = IntUtils.getBytes(timestamp);
        byte[] bUniqueID = IntUtils.getBytes(uniqueID);
        baos.write(bTimestamp, 0, 4);
        baos.write(bUniqueID, 0, 4);
        baos.write((byte) 3);
        baos.write((byte) 0);
        try {
            imei = PhoneInfoUtils.getImei(this.mContext);
        } catch (Exception e) {
            imei = new Random().nextInt();
        }
        baos.write(IntUtils.getBytes(StringUtils.hashCode(imei)), 0, 4);
        String hmac = Constants.STR_EMPTY;
        baos.write(IntUtils.getBytes(StringUtils.hashCode(_calcHmac(baos.toByteArray()))));
        return baos.toByteArray();
    }

    private static String _calcHmac(byte[] src) throws Exception {
        String key = HMAC_KEY;
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(key.getBytes(), mac.getAlgorithm()));
        return Base64.encodeToString(mac.doFinal(src), 2);
    }
}
