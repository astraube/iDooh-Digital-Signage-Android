package br.com.i9algo.taxiadv.v2.alarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.data.receiver.BootReceiver;
import br.com.i9algo.taxiadv.v2.helpers.Logger;

/**
 * When the alarm fires, this BroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class ScreenLockReceiver extends BroadcastReceiver {

    private final String LOG_TAG = "ScreenLock_UnLock";
    //private final String LOG_TAG = getClass().getSimpleName();


    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;

    private static final long twentyminutesinmillis = TimeUnit.MINUTES.toMillis(20);
    //private static final long twentyminutesinmillis = TimeUnit.MINUTES.toMillis(1);

    @Inject
    public ScreenLockReceiver() {
        super();
    }


    @Override
     public void onReceive(Context context, Intent intent) {
        ((CustomApplication) context.getApplicationContext()).getSchedulerComponent().inject(this);
        Logger.e(LOG_TAG, "onreceive");

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Logger.i(LOG_TAG,"Screen went OFF");
            Toast.makeText(context, "screen OFF", Toast.LENGTH_LONG).show();
            setAlarm(context);

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Logger.i(LOG_TAG,"Screen went ON");
            cancelAlarm(context);
        } else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Logger.i(LOG_TAG, "In Method:  ACTION_USER_PRESENT");
            cancelAlarm(context);
        }
    }

    public void setAlarm(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ScreenUnLockReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Logger.e(LOG_TAG, "setting alarm");

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + twentyminutesinmillis,
                twentyminutesinmillis, alarmIntent);

        Logger.e(LOG_TAG, alarmMgr.toString());
        Logger.e(LOG_TAG, "setting AlarmReceiver to : " + new Date(System.currentTimeMillis() + twentyminutesinmillis).toString());

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
        Logger.e(LOG_TAG, "ScreenLockReceiver.cancelAlarm");

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
}
