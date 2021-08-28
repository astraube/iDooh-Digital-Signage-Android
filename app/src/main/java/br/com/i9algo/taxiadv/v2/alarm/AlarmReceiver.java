package br.com.i9algo.taxiadv.v2.alarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.data.receiver.BootReceiver;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.injection.provides.AppStateManager;
import br.com.i9algo.taxiadv.v2.storage.firebase.RemoteConfigs;

/**
 * When the alarm fires, this BroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private final String LOG_TAG = getClass().getSimpleName() + " adv";

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;


    @Inject
    AppStateManager stateManager;

    @Inject
    public AlarmReceiver() {
        super();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        ((CustomApplication) context.getApplicationContext()).getSchedulerComponent().inject(this);
        //PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //@SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        //wl.acquire();

        Logger.e(LOG_TAG, "Alarm !!!!!!!!!!");

        // Servico de atualizacao automatica.
        // libraryAndroidUpdateChecker
        // CustomApplication.get(context.getApplicationContext()).startUpdater();

        stateManager.handleAvailableDiskSpace(context);
        stateManager.initProgrammingState();

        /**
         * 05/07/2019
         * desabilitado, dados estao no FIREBASE
         *
         stateManager.initCategories();
         */

        //stateManager.initialize();

        //wl.release();

    }

    // BEGIN_INCLUDE(set_alarm)

    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this BroadcastReceiver.
     *
     * @param context
     */
    public void setAlarm(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Logger.e(LOG_TAG, "setting alarm");


        long INTERVAL_MIN_REQUEST_CONTENT = TimeUnit.MINUTES.toMillis(RemoteConfigs.getTimeMinUpdateContent());
        long INTERVAL_MAX_REQUEST_CONTENT = TimeUnit.MINUTES.toMillis(RemoteConfigs.getTimeMaxUpdateContent());
        //long INTERVAL_MIN_REQUEST_CONTENT = TimeUnit.MINUTES.toMillis(1);
        //long INTERVAL_MAX_REQUEST_CONTENT = TimeUnit.MINUTES.toMillis(1);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + INTERVAL_MIN_REQUEST_CONTENT, INTERVAL_MAX_REQUEST_CONTENT, alarmIntent);

        Logger.e(LOG_TAG, alarmMgr.toString());
        Logger.e(LOG_TAG, "setting AlarmReceiver to : " + new Date(System.currentTimeMillis() + INTERVAL_MIN_REQUEST_CONTENT).toString());

        // Habilitar {@code SampleBootReceiver} para reiniciar automaticamente o alarme quando o
        // dispositivo eh reiniciado.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    // END_INCLUDE(set_alarm)

    /**
     * Cancels the alarm.
     *
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAlarm(Context context) {
        Logger.v(LOG_TAG, "SampleAlarmReceiver.cancelAlarm");

        // If the alarm has been set, cancel it.
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }

        // Desativar {code SampleBootReceiver} para que ele nao reiniciar automaticamente o
        // Alarme quando o dispositivo eh reiniciado.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    // END_INCLUDE(cancel_alarm)

}
