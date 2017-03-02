package com.igexin.p022a.p023a.p027b;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.tencent.connect.common.Constants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/* renamed from: com.igexin.a.a.b.g */
public final class C0246g {
    public static final int m819a(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) ((i >> 24) & MotionEventCompat.ACTION_MASK);
        bArr[i2 + 1] = (byte) ((i >> 16) & MotionEventCompat.ACTION_MASK);
        bArr[i2 + 2] = (byte) ((i >> 8) & MotionEventCompat.ACTION_MASK);
        bArr[i2 + 3] = (byte) (i & MotionEventCompat.ACTION_MASK);
        return 4;
    }

    public static final int m820a(long j, byte[] bArr, int i) {
        bArr[i] = (byte) ((int) ((j >> 56) & 255));
        bArr[i + 1] = (byte) ((int) ((j >> 48) & 255));
        bArr[i + 2] = (byte) ((int) ((j >> 40) & 255));
        bArr[i + 3] = (byte) ((int) ((j >> 32) & 255));
        bArr[i + 4] = (byte) ((int) ((j >> 24) & 255));
        bArr[i + 5] = (byte) ((int) ((j >> 16) & 255));
        bArr[i + 6] = (byte) ((int) ((j >> 8) & 255));
        bArr[i + 7] = (byte) ((int) (255 & j));
        return 8;
    }

    public static final int m821a(byte[] bArr, int i) {
        return bArr[i] & MotionEventCompat.ACTION_MASK;
    }

    public static final int m822a(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        System.arraycopy(bArr, i, bArr2, i2, i3);
        return i3;
    }

    public static final String m823a(String[] strArr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (!strArr[0].equals(Constants.STR_EMPTY)) {
            stringBuffer.append(strArr[0]).append("://");
        }
        if (!strArr[1].equals(Constants.STR_EMPTY)) {
            stringBuffer.append(strArr[1]);
        }
        if (!strArr[2].equals(Constants.STR_EMPTY)) {
            stringBuffer.append(':').append(strArr[2]);
        }
        if (!strArr[3].equals(Constants.STR_EMPTY)) {
            stringBuffer.append(strArr[3]);
            if (!strArr[3].equals("/")) {
                stringBuffer.append('/');
            }
        }
        if (!strArr[4].equals(Constants.STR_EMPTY)) {
            stringBuffer.append(strArr[4]);
        }
        if (!strArr[5].equals(Constants.STR_EMPTY)) {
            stringBuffer.append('?').append(strArr[5]);
        }
        return stringBuffer.toString();
    }

    private static void m824a(InputStream inputStream, OutputStream outputStream) {
        byte[] bArr = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    public static void m825a(InputStream inputStream, OutputStream outputStream, int i) {
        OutputStream c0241a = new C0241a(outputStream, i);
        C0246g.m824a(inputStream, c0241a);
        c0241a.m783a();
    }

    public static final byte[] m826a(int i) {
        int i2;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        do {
            i2 = ((i & 127) << 24) | i4;
            i >>>= 7;
            i5++;
            if (i > 0) {
                i4 = (i2 >>> 8) | Integer.MIN_VALUE;
                continue;
            } else {
                i4 = i2;
                continue;
            }
        } while (i > 0);
        byte[] bArr = new byte[i5];
        i2 = 24;
        while (i3 < i5) {
            bArr[i3] = (byte) (i4 >>> i2);
            i2 -= 8;
            i3++;
        }
        return bArr;
    }

    public static byte[] m827a(byte[] bArr) {
        GZIPOutputStream gZIPOutputStream;
        Throwable th;
        byte[] bArr2 = null;
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            try {
                gZIPOutputStream.write(bArr);
                gZIPOutputStream.finish();
                bArr2 = byteArrayOutputStream.toByteArray();
                if (gZIPOutputStream != null) {
                    try {
                        gZIPOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e2) {
                if (gZIPOutputStream != null) {
                    try {
                        gZIPOutputStream.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                return bArr2;
            } catch (Throwable th2) {
                th = th2;
                if (gZIPOutputStream != null) {
                    try {
                        gZIPOutputStream.close();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                        throw th;
                    }
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            gZIPOutputStream = bArr2;
            if (gZIPOutputStream != null) {
                gZIPOutputStream.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            return bArr2;
        } catch (Throwable th3) {
            Throwable th4 = th3;
            gZIPOutputStream = bArr2;
            th = th4;
            if (gZIPOutputStream != null) {
                gZIPOutputStream.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            throw th;
        }
        return bArr2;
    }

    public static final String[] m828a(String str) {
        int i;
        int indexOf;
        StringBuffer stringBuffer = new StringBuffer(str.toLowerCase());
        String[] strArr = new String[6];
        for (i = 0; i < 6; i++) {
            strArr[i] = Constants.STR_EMPTY;
        }
        i = str.indexOf(":");
        if (i > 0) {
            strArr[0] = str.substring(0, i);
            stringBuffer.delete(0, i + 1);
        } else if (i == 0) {
            throw new IllegalArgumentException("url format error - protocol");
        }
        if (stringBuffer.length() >= 2 && stringBuffer.charAt(0) == '/' && stringBuffer.charAt(1) == '/') {
            stringBuffer.delete(0, 2);
            indexOf = stringBuffer.toString().indexOf(47);
            if (indexOf < 0) {
                indexOf = stringBuffer.length();
            }
            if (indexOf != 0) {
                i = stringBuffer.toString().indexOf(58);
                if (i < 0) {
                    i = indexOf;
                } else if (i > indexOf) {
                    throw new IllegalArgumentException("url format error - port");
                } else {
                    strArr[2] = stringBuffer.toString().substring(i + 1, indexOf);
                }
                strArr[1] = stringBuffer.toString().substring(0, i);
                stringBuffer.delete(0, indexOf);
            }
        }
        if (stringBuffer.length() > 0) {
            String stringBuffer2 = stringBuffer.toString();
            indexOf = stringBuffer2.lastIndexOf(47);
            if (indexOf > 0) {
                strArr[3] = stringBuffer2.substring(0, indexOf);
            } else if (indexOf == 0) {
                if (stringBuffer2.indexOf(63) > 0) {
                    throw new IllegalArgumentException("url format error - path");
                }
                strArr[3] = stringBuffer2;
                return strArr;
            }
            if (indexOf < stringBuffer2.length() - 1) {
                stringBuffer2 = stringBuffer2.substring(indexOf + 1, stringBuffer2.length());
                indexOf = stringBuffer2.indexOf(63);
                if (indexOf >= 0) {
                    strArr[4] = stringBuffer2.substring(0, indexOf);
                    strArr[5] = stringBuffer2.substring(indexOf + 1);
                } else {
                    strArr[4] = stringBuffer2;
                }
            }
        } else {
            strArr[3] = "/";
        }
        return strArr;
    }

    public static final int m829b(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) ((i >> 8) & MotionEventCompat.ACTION_MASK);
        bArr[i2 + 1] = (byte) (i & MotionEventCompat.ACTION_MASK);
        return 2;
    }

    public static final int m830b(byte[] bArr, int i) {
        return ((bArr[i] & MotionEventCompat.ACTION_MASK) << 8) | (bArr[i + 1] & MotionEventCompat.ACTION_MASK);
    }

    public static byte[] m831b(byte[] bArr) {
        GZIPInputStream gZIPInputStream;
        Throwable th;
        byte[] bArr2 = null;
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    try {
                        int read = gZIPInputStream.read();
                        if (read == -1) {
                            break;
                        }
                        byteArrayOutputStream.write(read);
                    } catch (IOException e) {
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
                bArr2 = byteArrayOutputStream.toByteArray();
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e2) {
                    }
                }
                if (gZIPInputStream != null) {
                    gZIPInputStream.close();
                }
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            } catch (IOException e3) {
                byteArrayOutputStream = bArr2;
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e4) {
                    }
                }
                if (gZIPInputStream != null) {
                    gZIPInputStream.close();
                }
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                return bArr2;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                byteArrayOutputStream = bArr2;
                th = th4;
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e5) {
                        throw th;
                    }
                }
                if (gZIPInputStream != null) {
                    gZIPInputStream.close();
                }
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                throw th;
            }
        } catch (IOException e6) {
            byteArrayOutputStream = bArr2;
            gZIPInputStream = bArr2;
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            if (gZIPInputStream != null) {
                gZIPInputStream.close();
            }
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
            return bArr2;
        } catch (Throwable th32) {
            gZIPInputStream = bArr2;
            byte[] bArr3 = bArr2;
            th = th32;
            byteArrayOutputStream = bArr3;
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            if (gZIPInputStream != null) {
                gZIPInputStream.close();
            }
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
            throw th;
        }
        return bArr2;
    }

    public static final int m832c(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) i;
        return 1;
    }

    public static final int m833c(byte[] bArr, int i) {
        return ((((bArr[i] & MotionEventCompat.ACTION_MASK) << 24) | ((bArr[i + 1] & MotionEventCompat.ACTION_MASK) << 16)) | ((bArr[i + 2] & MotionEventCompat.ACTION_MASK) << 8)) | (bArr[i + 3] & MotionEventCompat.ACTION_MASK);
    }

    public static final long m834d(byte[] bArr, int i) {
        return ((((((((((long) bArr[i]) & 255) << 56) | ((((long) bArr[i + 1]) & 255) << 48)) | ((((long) bArr[i + 2]) & 255) << 40)) | ((((long) bArr[i + 3]) & 255) << 32)) | ((((long) bArr[i + 4]) & 255) << 24)) | ((((long) bArr[i + 5]) & 255) << 16)) | ((((long) bArr[i + 6]) & 255) << 8)) | (((long) bArr[i + 7]) & 255);
    }

    public static byte[] m835e(byte[] bArr, int i) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            C0246g.m825a(byteArrayInputStream, byteArrayOutputStream, i);
            try {
                byteArrayInputStream.close();
            } catch (Throwable th) {
            }
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th2) {
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable e) {
            throw new RuntimeException("Unexpected I/O error", e);
        } catch (Throwable th3) {
            try {
                byteArrayInputStream.close();
            } catch (Throwable th4) {
            }
            byteArrayOutputStream.close();
        }
    }
}
