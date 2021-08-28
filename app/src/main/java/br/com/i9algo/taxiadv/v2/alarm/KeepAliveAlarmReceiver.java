package br.com.i9algo.taxiadv.v2.alarm;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.PowerManager;

import java.util.Date;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.BuildConfig;
import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.domain.constants.Constants;
import br.com.i9algo.taxiadv.data.receiver.BootReceiver;
import br.com.i9algo.taxiadv.v2.helpers.LocationUtils;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.network.taxiadv.IdoohMediaDeviceController;
import br.com.i9algo.taxiadv.v2.injection.provides.AppStateManager;

public class KeepAliveAlarmReceiver extends BroadcastReceiver {

    private final String LOG_TAG = getClass().getSimpleName();

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Inject
    AppStateManager stateManager;

    @Inject
    public KeepAliveAlarmReceiver() {
        super();
    }


    @Override
    public void onReceive(final Context context, Intent intent) {
        Logger.v(LOG_TAG, "KeepAliveAlarmReceiver Alarm !!!!!!!!!!");
        ((CustomApplication) context.getApplicationContext()).getSchedulerComponent().inject(this);

        Logger.v(LOG_TAG, "KeepAliveAlarmReceiver Acquiring Wake Lock");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        try {
            Location location = LocationUtils.getLocationByString( LocationUtils.getLastLocation(context) );
            if (BuildConfig.DEBUG) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Logger.v(LOG_TAG, "Location latitude: " + latitude);
                Logger.v(LOG_TAG, "Location longitude: " + longitude);
            }

            // TODO - voltar de utilizar este metodo
            //appStateManager.sendPosition(this, location.getLatitude(), location.getLongitude());

            // TODO - parar de utilizar este metodo
            IdoohMediaDeviceController.sendGeopoint(location );

        } catch(Exception e) {
            e.printStackTrace();
        }

        wl.release();

    }

    public void setAlarm(Context context) {
        ((CustomApplication) context.getApplicationContext()).getSchedulerComponent().inject(this);
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, KeepAliveAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Logger.v(LOG_TAG, "setting KeepAliveAlarmReceiver");

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + Constants.INTERVAL_MIN_SEND_INFO,
                Constants.INTERVAL_MIN_SEND_INFO, alarmIntent);

        Logger.v(LOG_TAG, "setting KeepAliveAlarmReceiverAlarm to : " + new Date(System.currentTimeMillis() + Constants.INTERVAL_MIN_SEND_INFO).toString());

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context) {
        Logger.v(LOG_TAG, "KeepAliveAlarmReceiver.cancelAlarm");

        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
