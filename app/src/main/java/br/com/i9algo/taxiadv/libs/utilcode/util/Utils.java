package br.com.i9algo.taxiadv.libs.utilcode.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * <pre>
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  : utils about initialization
 * </pre>
 */
public final class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param context context
     */
    public static void init(final Context context) {
        if (context == null) {
            init(getApplicationByReflect());
            return;
        }
        init((Application) context.getApplicationContext());
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param app application
     */
    public static void init(final Application app) {
        if (sApplication == null) {
            if (app == null) {
                sApplication = getApplicationByReflect();
            } else {
                sApplication = app;
            }
            //sApplication.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
        } else {
            if (app != null && app.getClass() != sApplication.getClass()) {
                //sApplication.unregisterActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
                //ACTIVITY_LIFECYCLE.mActivityList.clear();
                sApplication = app;
                //sApplication.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
            }
        }
    }

    public static int checkSelfPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(getApplicationByReflect().getApplicationContext(), permission);
    }

    /**
     * Return the context of Application object.
     *
     * @return the context of Application object
     */
    public static Application getApp() {
        if (sApplication != null) return sApplication;
        Application app = getApplicationByReflect();
        init(app);
        return app;
    }

    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }

    static boolean isAppForeground() {
        ActivityManager am = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) return false;
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return aInfo.processName.equals(Utils.getApp().getPackageName());
            }
        }
        return false;
    }

    /** Returns true if the application has the given permission. */
    public static boolean hasPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == PERMISSION_GRANTED;
    }

    /** Returns true if the application has the given feature. */
    public static boolean hasFeature(Context context, String feature) {
        return context.getPackageManager().hasSystemFeature(feature);
    }

    public static boolean isNullOrEmpty(CharSequence text) {
        return isEmpty(text) || getTrimmedLength(text) == 0;
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * <p>Copied from {@link TextUtils#isEmpty(CharSequence)}
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    private static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * Returns the length that the specified CharSequence would have if spaces and control characters
     * were trimmed from the start and end, as by {@link String#trim}.
     *
     * <p>Copied from {@link TextUtils#getTrimmedLength(CharSequence)}
     */
    private static int getTrimmedLength(@NonNull CharSequence s) {
        int len = s.length();

        int start = 0;
        while (start < len && s.charAt(start) <= ' ') {
            start++;
        }

        int end = len;
        while (end > start && s.charAt(end - 1) <= ' ') {
            end--;
        }

        return end - start;
    }

    /** Returns true if the collection is null or has a size of 0. */
    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    /** Returns true if the array is null or has a size of 0. */
    public static <T> boolean isNullOrEmpty(T[] data) {
        return data == null || data.length == 0;
    }

    /** Returns true if the map is null or empty, false otherwise. */
    public static boolean isNullOrEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    public static final class FileProvider4UtilCode extends FileProvider {

        @Override
        public boolean onCreate() {
            Utils.init(getContext());
            return true;
        }
    }
}
