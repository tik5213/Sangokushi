package com.alipay.sdk.auth;

import android.webkit.SslErrorHandler;
import com.alipay.sdk.auth.AuthActivity.C0148b;
import com.alipay.sdk.widget.C0213d;

/* renamed from: com.alipay.sdk.auth.e */
final class C0154e implements Runnable {
    final /* synthetic */ SslErrorHandler f297a;
    final /* synthetic */ C0148b f298b;

    C0154e(C0148b c0148b, SslErrorHandler sslErrorHandler) {
        this.f298b = c0148b;
        this.f297a = sslErrorHandler;
    }

    public final void run() {
        C0213d.m715a(this.f298b.f282a, "\u5b89\u5168\u8b66\u544a", "\u7531\u4e8e\u60a8\u7684\u8bbe\u5907\u7f3a\u5c11\u6839\u8bc1\u4e66\uff0c\u5c06\u65e0\u6cd5\u6821\u9a8c\u8be5\u8bbf\u95ee\u7ad9\u70b9\u7684\u5b89\u5168\u6027\uff0c\u53ef\u80fd\u5b58\u5728\u98ce\u9669\uff0c\u8bf7\u9009\u62e9\u662f\u5426\u7ee7\u7eed\uff1f", "\u7ee7\u7eed", new C0155f(this), "\u9000\u51fa", new C0156g(this));
    }
}
