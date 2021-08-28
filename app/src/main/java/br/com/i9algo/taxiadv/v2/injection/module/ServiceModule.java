package br.com.i9algo.taxiadv.v2.injection.module;

import javax.inject.Singleton;

import br.com.i9algo.taxiadv.data.receiver.NetworkChangeReceiver;
import br.com.i9algo.taxiadv.data.receiver.PowerConnectionReceiver;
import br.com.i9algo.taxiadv.v2.alarm.AlarmReceiver;
import br.com.i9algo.taxiadv.data.receiver.BatteryLowReceiver;
import br.com.i9algo.taxiadv.v2.alarm.KeepAliveAlarmReceiver;
import br.com.i9algo.taxiadv.v2.alarm.KeepAliveSchedulingService;
import br.com.i9algo.taxiadv.v2.alarm.SchedulingService;
import br.com.i9algo.taxiadv.v2.alarm.ScreenLockReceiver;
import br.com.i9algo.taxiadv.v2.download.BatchDownloadFileReceiver;
import br.com.i9algo.taxiadv.v2.download.BatchDownloadService;
import br.com.i9algo.taxiadv.v2.geo.GeofenceBroadcastReceiver;
import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    SchedulingService mService;

    AlarmReceiver mReceiver;

    GeofenceBroadcastReceiver mGeofenceReceiver;

    BatteryLowReceiver mDeviceInfoReceiver;

    KeepAliveSchedulingService mKeepAliveSchedulingService;

    KeepAliveAlarmReceiver mKeepAliveAlarmReceiver;

    BatchDownloadService mBatchDownloadService;

    BatchDownloadFileReceiver mBatchDownloadReceiver;

    NetworkChangeReceiver mNetworkChangeReceiver;

    ScreenLockReceiver mScreenLockReceiver;

    PowerConnectionReceiver mPowerConnectionReceiver;

    public ServiceModule(SchedulingService service, AlarmReceiver receiver, GeofenceBroadcastReceiver geofenceBroadcastReceiver, BatteryLowReceiver deviceInfoReceiver, KeepAliveSchedulingService keepAliveSchedulingService, KeepAliveAlarmReceiver keepAliveAlarmReceiver, BatchDownloadService batchDownloadService, BatchDownloadFileReceiver batchDownloadFileReceiver, NetworkChangeReceiver networkChangeReceiver, ScreenLockReceiver screenLockReceiver, PowerConnectionReceiver powerConnectionReceiver) {
        mService = service;
        mReceiver = receiver;
        mGeofenceReceiver = geofenceBroadcastReceiver;
        mDeviceInfoReceiver = deviceInfoReceiver;
        mKeepAliveSchedulingService = keepAliveSchedulingService;
        mKeepAliveAlarmReceiver = keepAliveAlarmReceiver;
        mBatchDownloadService = batchDownloadService;
        mBatchDownloadReceiver = batchDownloadFileReceiver;
        mNetworkChangeReceiver = networkChangeReceiver;
        mScreenLockReceiver = screenLockReceiver;
        mPowerConnectionReceiver = powerConnectionReceiver;
    }

    @Provides
    SchedulingService provideSchedulingService() {
        return mService;
    }

    @Provides
    BatteryLowReceiver provideBatteryLevelReceiver() {
        return mDeviceInfoReceiver;
    }

    @Provides
    GeofenceBroadcastReceiver provideGeofenceBroadcastReceiver() {
        return mGeofenceReceiver;
    }
//
//    @Provides
//    KeepAliveAlarmReceiver provideKeepAliveAlarmReceiver() {
//        return mKeepAliveAlarmReceiver;
//    }
//
//    @Provides
//    KeepAliveSchedulingService provideKeepAliveSchedulingService() { return mKeepAliveSchedulingService; }

    @Provides    @Singleton
    PowerConnectionReceiver providePowerConnectionReceiver() { return mPowerConnectionReceiver; }

}
