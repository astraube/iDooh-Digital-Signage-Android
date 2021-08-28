package br.com.i9algo.taxiadv.v2.alarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.data.receiver.BootReceiver;
import br.com.i9algo.taxiadv.domain.constants.Constants;
import br.com.i9algo.taxiadv.libs.utilcode.util.DeviceUtils;
import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.v2.helpers.DialogUtil;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.injection.provides.AppStateManager;
import br.com.i9algo.taxiadv.v2.storage.firebase.RemoteConfigs;

/**
 * When the alarm fires, this BroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class AlarmRestartReceiver extends BroadcastReceiver {

    private final String LOG_TAG = getClass().getSimpleName();

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;


    @Inject
    AppStateManager stateManager;

    @Inject
    public AlarmRestartReceiver() {
        super();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        CustomApplication app = ((CustomApplication) context.getApplicationContext());
        app.getSchedulerComponent().inject(this);

        Logger.e(LOG_TAG, "Alarm !!!!!!!!!!");

        DialogUtil.showTimerRebootDialog(app.getCurrentActivity(), 10, "Desligamento Programado", "Este equipamento será desligado em 10 segundos.");
    }

    public void setAlarm(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmRestartReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Logger.e(LOG_TAG, "setting alarm");

        long INTERVAL_MIN_REQUEST_CONTENT = Constants.INTERVAL_RESTART_DEVICE;
        long INTERVAL_MAX_REQUEST_CONTENT = INTERVAL_MIN_REQUEST_CONTENT + TimeUnit.HOURS.toMillis(1);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + INTERVAL_MIN_REQUEST_CONTENT, INTERVAL_MAX_REQUEST_CONTENT, alarmIntent);

        Logger.e(LOG_TAG, alarmMgr.toString());
        Logger.e(LOG_TAG, "setting AlarmRestartReceiver to : " + new Date(System.currentTimeMillis() + INTERVAL_MIN_REQUEST_CONTENT).toString());
    }

}
