package com.unity3d.player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;

/* renamed from: com.unity3d.player.o */
public final class C0711o {
    private final Bundle f2286a;

    public C0711o(Activity activity) {
        Bundle bundle = Bundle.EMPTY;
        PackageManager packageManager = activity.getPackageManager();
        ComponentName componentName = activity.getComponentName();
        try {
            ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            if (!(activityInfo == null || activityInfo.metaData == null)) {
                bundle = activityInfo.metaData;
            }
        } catch (NameNotFoundException e) {
            C0708m.Log(6, "Unable to retreive meta data for activity '" + componentName + "'");
        }
        this.f2286a = new Bundle(bundle);
    }

    private static String m2593a(String str) {
        return String.format("%s.%s", new Object[]{"unityplayer", str});
    }

    public final boolean m2594a() {
        return this.f2286a.getBoolean(C0711o.m2593a("ForwardNativeEventsToDalvik"));
    }
}
