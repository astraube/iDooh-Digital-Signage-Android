package br.com.i9algo.taxiadv.v2.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import br.com.i9algo.taxiadv.libs.utilcode.util.DeviceUtils;

/**
 * @author andre straube
 * @version 2.0 - 15/12/2015
 * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 */
@SuppressLint("InlinedApi")
public final class IDManagement {

    private IDManagement() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static float getBatteryLevel(Context context) {
        Intent batteryIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if (level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float) level / (float) scale) * 100.0f;
    }

}