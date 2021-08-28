package br.com.i9algo.taxiadv.v2.alarm;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import javax.inject.Inject;

/**
 * When the alarm fires, this BroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class ScreenUnLockReceiver extends BroadcastReceiver {

    private final String LOG_TAG = "ScreenLock_UnLock";
    //private final String LOG_TAG = "ScreenUnLockReceiver";


    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;


    @Inject
    public ScreenUnLockReceiver() {
        super();
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        Log.e(LOG_TAG, "Alarm !!!!!!!!!!");
        turnOnScreen(context);
        wl.release();
    }

    @SuppressLint({"LongLogTag", "InvalidWakeLockTag"})
    public void turnOnScreen(Context c){
        Log.e(LOG_TAG, "ON!");
        mPowerManager = ((PowerManager)c.getSystemService(Context.POWER_SERVICE));
        mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "tag");
        mWakeLock.acquire();
        mWakeLock.release();
    }

}
