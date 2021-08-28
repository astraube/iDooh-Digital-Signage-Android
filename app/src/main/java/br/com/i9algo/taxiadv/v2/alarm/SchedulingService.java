package br.com.i9algo.taxiadv.v2.alarm;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.v2.CustomApplication;

public class SchedulingService extends IntentService {
	
	private final String LOG = "Scheduling";

    AlarmReceiver mAlarm;

    public SchedulingService() {
        super("SchedulingService");
    }

    @Inject
    public SchedulingService(AlarmReceiver alarm) {
        super("SchedulingService");
        mAlarm = alarm;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((CustomApplication) getApplicationContext()).getSchedulerComponent().inject(this);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mAlarm.setResultExtras(intent.getExtras());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        mAlarm.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        mAlarm.setAlarm(this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
