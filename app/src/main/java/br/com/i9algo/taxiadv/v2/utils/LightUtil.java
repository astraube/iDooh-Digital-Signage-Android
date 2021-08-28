package br.com.i9algo.taxiadv.v2.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by Taxi ADV on 24/03/2016.
 */
public class LightUtil {


    private static void setBrightness(Activity activity, float screenBrightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = screenBrightness;
        activity.getWindow().setAttributes(lp);
    }


    public static void setLightOff(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        setBrightness(activity, lp.BRIGHTNESS_OVERRIDE_OFF);
    }

    public static void setLightNormalize(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        setBrightness(activity, lp.BRIGHTNESS_OVERRIDE_FULL);
    }

    /**
     * "soundLevel" deve ser entre de 0 a 100
     * @param activity
     * @param soundLevel
     */
    public static void setLightLevel(Activity activity, int soundLevel) {
        setBrightness(activity, (soundLevel / 100.0F));
    }

    public static void setLightLevelOffset(Activity activity, float soundLevel) {
        setBrightness(activity, soundLevel);
    }

    public static float getLightLevel(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        return lp.screenBrightness;
    }

    public static float getLightMaxLevel(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        return lp.BRIGHTNESS_OVERRIDE_FULL;
    }
}
