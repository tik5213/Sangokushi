package com.alipay.sdk.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alipay.sdk.encrypt.C0172a;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/* renamed from: com.alipay.sdk.widget.a */
public final class C0210a {
    public static final String f530a = "\u6b63\u5728\u52a0\u8f7d";
    public static final String f531b = "\u53bb\u652f\u4ed8\u5b9d\u4ed8\u6b3e";
    public static final String f532c = "\u53bb\u652f\u4ed8\u5b9d\u6388\u6743";
    public static String f533d;
    private C0209a f534e;
    private Activity f535f;
    private String f536g;

    /* renamed from: com.alipay.sdk.widget.a.a */
    public class C0209a extends AlertDialog {
        final /* synthetic */ C0210a f529a;

        protected C0209a(C0210a c0210a, Context context) {
            this.f529a = c0210a;
            super(context);
        }

        protected final void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            Context context = getContext();
            View linearLayout = new LinearLayout(context);
            LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, C0209a.m702a(context, 50.0f));
            layoutParams.gravity = 17;
            linearLayout.setOrientation(0);
            linearLayout.setLayoutParams(layoutParams);
            Drawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(-450944201);
            gradientDrawable.setCornerRadius((float) C0209a.m702a(context, 5.0f));
            linearLayout.setBackgroundDrawable(gradientDrawable);
            View imageView = new ImageView(context);
            layoutParams = new LinearLayout.LayoutParams(C0209a.m702a(context, 20.0f), C0209a.m702a(context, 20.0f));
            layoutParams.gravity = 16;
            layoutParams.setMargins(C0209a.m702a(this.f529a.f535f, 17.0f), C0209a.m702a(this.f529a.f535f, 10.0f), C0209a.m702a(this.f529a.f535f, 8.0f), C0209a.m702a(this.f529a.f535f, 10.0f));
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ScaleType.FIT_CENTER);
            imageView.setImageDrawable(C0209a.m703a(context, C0210a.f533d));
            Animation rotateAnimation = new RotateAnimation(0.0f, 359.0f, 1, 0.5f, 1, 0.5f);
            rotateAnimation.setRepeatCount(-1);
            rotateAnimation.setDuration(900);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            imageView.startAnimation(rotateAnimation);
            View textView = new TextView(context);
            textView.setText(TextUtils.isEmpty(this.f529a.f536g) ? C0210a.f530a : this.f529a.f536g);
            textView.setTextSize(16.0f);
            textView.setTextColor(-1);
            layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.gravity = 16;
            layoutParams.setMargins(0, 0, C0209a.m702a(context, 17.0f), 0);
            textView.setLayoutParams(layoutParams);
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            setContentView(linearLayout);
            setCancelable(false);
        }

        private View m704a(Context context) {
            View linearLayout = new LinearLayout(context);
            LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, C0209a.m702a(context, 50.0f));
            layoutParams.gravity = 17;
            linearLayout.setOrientation(0);
            linearLayout.setLayoutParams(layoutParams);
            Drawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(-450944201);
            gradientDrawable.setCornerRadius((float) C0209a.m702a(context, 5.0f));
            linearLayout.setBackgroundDrawable(gradientDrawable);
            View imageView = new ImageView(context);
            layoutParams = new LinearLayout.LayoutParams(C0209a.m702a(context, 20.0f), C0209a.m702a(context, 20.0f));
            layoutParams.gravity = 16;
            layoutParams.setMargins(C0209a.m702a(this.f529a.f535f, 17.0f), C0209a.m702a(this.f529a.f535f, 10.0f), C0209a.m702a(this.f529a.f535f, 8.0f), C0209a.m702a(this.f529a.f535f, 10.0f));
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ScaleType.FIT_CENTER);
            imageView.setImageDrawable(C0209a.m703a(context, C0210a.f533d));
            Animation rotateAnimation = new RotateAnimation(0.0f, 359.0f, 1, 0.5f, 1, 0.5f);
            rotateAnimation.setRepeatCount(-1);
            rotateAnimation.setDuration(900);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            imageView.startAnimation(rotateAnimation);
            View textView = new TextView(context);
            textView.setText(TextUtils.isEmpty(this.f529a.f536g) ? C0210a.f530a : this.f529a.f536g);
            textView.setTextSize(16.0f);
            textView.setTextColor(-1);
            layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.gravity = 16;
            layoutParams.setMargins(0, 0, C0209a.m702a(context, 17.0f), 0);
            textView.setLayoutParams(layoutParams);
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            return linearLayout;
        }

        private static Drawable m703a(Context context, String str) {
            InputStream inputStream;
            Throwable th;
            InputStream byteArrayInputStream;
            try {
                byteArrayInputStream = new ByteArrayInputStream(C0172a.m498a(str));
                try {
                    Options options = new Options();
                    options.inDensity = 480;
                    options.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
                    Drawable bitmapDrawable = new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(byteArrayInputStream, null, options));
                    try {
                        byteArrayInputStream.close();
                        return bitmapDrawable;
                    } catch (Exception e) {
                        return bitmapDrawable;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (byteArrayInputStream != null) {
                        try {
                            byteArrayInputStream.close();
                        } catch (Exception e2) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                byteArrayInputStream = null;
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                throw th;
            }
        }

        private static int m702a(Context context, float f) {
            return (int) ((context == null ? Resources.getSystem() : context.getResources()).getDisplayMetrics().density * f);
        }
    }

    static {
        f533d = "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";
    }

    private C0210a(Activity activity) {
        this.f535f = activity;
    }

    public C0210a(Activity activity, String str) {
        this.f535f = activity;
        this.f536g = str;
    }

    private void m707a(String str) {
        this.f536g = str;
    }

    private String m710c() {
        return this.f536g;
    }

    public final void m712a() {
        if (this.f535f != null) {
            this.f535f.runOnUiThread(new C0211b(this));
        }
    }

    public final void m713b() {
        if (this.f535f != null) {
            this.f535f.runOnUiThread(new C0212c(this));
        }
    }

    private void m711d() {
        this.f535f = null;
        this.f534e = null;
    }
}
