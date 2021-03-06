package com.tencent.wxop.stat.common;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Base64;

/* renamed from: com.tencent.wxop.stat.common.f */
public class C0634f {
    static byte[] m2374a() {
        return Base64.decode("MDNhOTc2NTExZTJjYmUzYTdmMjY4MDhmYjdhZjNjMDU=", 0);
    }

    public static byte[] m2375a(byte[] bArr) {
        return C0634f.m2376a(bArr, C0634f.m2374a());
    }

    static byte[] m2376a(byte[] bArr, byte[] bArr2) {
        int i = 0;
        int[] iArr = new int[AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY];
        int[] iArr2 = new int[AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY];
        int length = bArr2.length;
        if (length <= 0 || length > AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY) {
            throw new IllegalArgumentException("key must be between 1 and 256 bytes");
        }
        int i2;
        for (i2 = 0; i2 < AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY; i2++) {
            iArr[i2] = i2;
            iArr2[i2] = bArr2[i2 % length];
        }
        i2 = 0;
        for (length = 0; length < AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY; length++) {
            i2 = ((i2 + iArr[length]) + iArr2[length]) & MotionEventCompat.ACTION_MASK;
            int i3 = iArr[length];
            iArr[length] = iArr[i2];
            iArr[i2] = i3;
        }
        byte[] bArr3 = new byte[bArr.length];
        i2 = 0;
        length = 0;
        while (i < bArr.length) {
            i2 = (i2 + 1) & MotionEventCompat.ACTION_MASK;
            length = (length + iArr[i2]) & MotionEventCompat.ACTION_MASK;
            i3 = iArr[i2];
            iArr[i2] = iArr[length];
            iArr[length] = i3;
            bArr3[i] = (byte) (iArr[(iArr[i2] + iArr[length]) & MotionEventCompat.ACTION_MASK] ^ bArr[i]);
            i++;
        }
        return bArr3;
    }

    public static byte[] m2377b(byte[] bArr) {
        return C0634f.m2378b(bArr, C0634f.m2374a());
    }

    static byte[] m2378b(byte[] bArr, byte[] bArr2) {
        return C0634f.m2376a(bArr, bArr2);
    }
}
