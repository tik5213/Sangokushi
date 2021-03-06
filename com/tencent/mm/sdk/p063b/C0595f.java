package com.tencent.mm.sdk.p063b;

import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI.WXApp;

/* renamed from: com.tencent.mm.sdk.b.f */
public final class C0595f {
    public final String toString() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace == null || stackTrace.length < 4) {
            return Constants.STR_EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int i = 3;
        while (i < stackTrace.length) {
            if (stackTrace[i].getClassName().contains(WXApp.WXAPP_PACKAGE_NAME) && !stackTrace[i].getClassName().contains("sdk.platformtools.Log")) {
                stringBuilder.append("[");
                stringBuilder.append(stackTrace[i].getClassName().substring(15));
                stringBuilder.append(":");
                stringBuilder.append(stackTrace[i].getMethodName());
                stringBuilder.append("(" + stackTrace[i].getLineNumber() + ")]");
            }
            i++;
        }
        return stringBuilder.toString();
    }
}
