package android.support.v7.widget;

import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.util.Log;
import com.texaspoker.moment.TexasPoker.PushMsg;
import java.lang.reflect.Field;
import net.sqlcipher.database.SQLiteDatabase;

@RestrictTo({Scope.LIBRARY_GROUP})
public class DrawableUtils {
    public static final Rect INSETS_NONE;
    private static final String TAG = "DrawableUtils";
    private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
    private static Class<?> sInsetsClazz;

    static {
        INSETS_NONE = new Rect();
        if (VERSION.SDK_INT >= 18) {
            try {
                sInsetsClazz = Class.forName("android.graphics.Insets");
            } catch (ClassNotFoundException e) {
            }
        }
    }

    private DrawableUtils() {
    }

    public static Rect getOpticalBounds(Drawable drawable) {
        if (sInsetsClazz != null) {
            try {
                Drawable unwrap = DrawableCompat.unwrap(drawable);
                Object invoke = unwrap.getClass().getMethod("getOpticalInsets", new Class[0]).invoke(unwrap, new Object[0]);
                if (invoke != null) {
                    Rect rect = new Rect();
                    for (Field field : sInsetsClazz.getFields()) {
                        String name = field.getName();
                        Object obj = -1;
                        switch (name.hashCode()) {
                            case -1383228885:
                                if (name.equals("bottom")) {
                                    obj = 3;
                                    break;
                                }
                                break;
                            case 115029:
                                if (name.equals("top")) {
                                    obj = 1;
                                    break;
                                }
                                break;
                            case 3317767:
                                if (name.equals("left")) {
                                    obj = null;
                                    break;
                                }
                                break;
                            case 108511772:
                                if (name.equals("right")) {
                                    obj = 2;
                                    break;
                                }
                                break;
                        }
                        switch (obj) {
                            case SQLiteDatabase.OPEN_READWRITE /*0*/:
                                rect.left = field.getInt(invoke);
                                break;
                            case SQLiteDatabase.OPEN_READONLY /*1*/:
                                rect.top = field.getInt(invoke);
                                break;
                            case SQLiteDatabase.CONFLICT_ABORT /*2*/:
                                rect.right = field.getInt(invoke);
                                break;
                            case SQLiteDatabase.CONFLICT_FAIL /*3*/:
                                rect.bottom = field.getInt(invoke);
                                break;
                            default:
                                break;
                        }
                    }
                    return rect;
                }
            } catch (Exception e) {
                Log.e(TAG, "Couldn't obtain the optical insets. Ignoring.");
            }
        }
        return INSETS_NONE;
    }

    static void fixDrawable(@NonNull Drawable drawable) {
        if (VERSION.SDK_INT == 21 && VECTOR_DRAWABLE_CLAZZ_NAME.equals(drawable.getClass().getName())) {
            fixVectorDrawableTinting(drawable);
        }
    }

    public static boolean canSafelyMutateDrawable(@NonNull Drawable drawable) {
        Drawable drawable2 = drawable;
        while (true) {
            if (VERSION.SDK_INT < 15 && (drawable2 instanceof InsetDrawable)) {
                return false;
            }
            if (VERSION.SDK_INT < 15 && (drawable2 instanceof GradientDrawable)) {
                return false;
            }
            if (VERSION.SDK_INT >= 17 || !(drawable2 instanceof LayerDrawable)) {
                if (!(drawable2 instanceof DrawableContainer)) {
                    if (!(drawable2 instanceof DrawableWrapper)) {
                        if (!(drawable2 instanceof android.support.v7.graphics.drawable.DrawableWrapper)) {
                            if (!(drawable2 instanceof ScaleDrawable)) {
                                break;
                            }
                            drawable2 = ((ScaleDrawable) drawable2).getDrawable();
                        } else {
                            drawable2 = ((android.support.v7.graphics.drawable.DrawableWrapper) drawable2).getWrappedDrawable();
                        }
                    } else {
                        drawable2 = ((DrawableWrapper) drawable2).getWrappedDrawable();
                    }
                } else {
                    break;
                }
            }
            return false;
        }
        ConstantState constantState = drawable2.getConstantState();
        if (constantState instanceof DrawableContainerState) {
            for (Drawable canSafelyMutateDrawable : ((DrawableContainerState) constantState).getChildren()) {
                if (!canSafelyMutateDrawable(canSafelyMutateDrawable)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void fixVectorDrawableTinting(Drawable drawable) {
        int[] state = drawable.getState();
        if (state == null || state.length == 0) {
            drawable.setState(ThemeUtils.CHECKED_STATE_SET);
        } else {
            drawable.setState(ThemeUtils.EMPTY_STATE_SET);
        }
        drawable.setState(state);
    }

    static Mode parseTintMode(int i, Mode mode) {
        switch (i) {
            case SQLiteDatabase.CONFLICT_FAIL /*3*/:
                return Mode.SRC_OVER;
            case SQLiteDatabase.CONFLICT_REPLACE /*5*/:
                return Mode.SRC_IN;
            case PushMsg.STCLUBASEBINFO_FIELD_NUMBER /*9*/:
                return Mode.SRC_ATOP;
            case PushMsg.STRPICURL_FIELD_NUMBER /*14*/:
                return Mode.MULTIPLY;
            case PushMsg.LCREATETIME_FIELD_NUMBER /*15*/:
                return Mode.SCREEN;
            case SQLiteDatabase.NO_LOCALIZED_COLLATORS /*16*/:
                if (VERSION.SDK_INT >= 11) {
                    return Mode.valueOf("ADD");
                }
                return mode;
            default:
                return mode;
        }
    }
}