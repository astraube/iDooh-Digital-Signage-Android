package br.com.i9algo.taxiadv.v2.injection.component;


import br.com.i9algo.taxiadv.data.receiver.NetworkChangeReceiver;
import br.com.i9algo.taxiadv.data.receiver.PowerConnectionReceiver;
import br.com.i9algo.taxiadv.v2.alarm.AlarmReceiver;
import br.com.i9algo.taxiadv.data.receiver.BatteryLowReceiver;
import br.com.i9algo.taxiadv.v2.alarm.AlarmRestartReceiver;
import br.com.i9algo.taxiadv.v2.alarm.KeepAliveAlarmReceiver;
import br.com.i9algo.taxiadv.v2.alarm.KeepAliveSchedulingService;
import br.com.i9algo.taxiadv.v2.alarm.SchedulingService;
import br.com.i9algo.taxiadv.v2.alarm.ScreenLockReceiver;
import br.com.i9algo.taxiadv.v2.download.BatchDownloadFileReceiver;
import br.com.i9algo.taxiadv.v2.download.BatchDownloadService;
import br.com.i9algo.taxiadv.v2.geo.GeofenceBroadcastReceiver;
import br.com.i9algo.taxiadv.v2.injection.module.ServiceModule;
import dagger.Subcomponent;

@Subcomponent(modules = ServiceModule.class)
public interface SchedulerComponent {
    void inject(SchedulingService service);
    void inject(AlarmReceiver receiver);
    void inject(AlarmRestartReceiver receiver);
    void inject(GeofenceBroadcastReceiver receiver);
    void inject(BatteryLowReceiver deviceInfoReceiver);
    void inject(KeepAliveAlarmReceiver receiver);
    void inject(KeepAliveSchedulingService service);
    void inject(BatchDownloadService batchDownloadService);
    void inject(BatchDownloadFileReceiver batchDownloadFileReceiver);
    void inject(NetworkChangeReceiver networkChangeReceiver);
    void inject(ScreenLockReceiver screenLockReceiver);
    void inject(PowerConnectionReceiver powerConnectionReceiver);

}
