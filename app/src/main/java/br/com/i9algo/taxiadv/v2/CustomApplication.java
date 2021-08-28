package br.com.i9algo.taxiadv.v2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.segment.analytics.Analytics;
import com.segment.analytics.Traits;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.security.Security;

import android.app.ActivityManager;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import org.conscrypt.Conscrypt;

import br.com.i9algo.taxiadv.BuildConfig;
import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.data.receiver.PowerConnectionReceiver;
import br.com.i9algo.taxiadv.domain.constants.Constants;
import br.com.i9algo.taxiadv.domain.models.Device;
import br.com.i9algo.taxiadv.data.receiver.NetworkChangeReceiver;
import br.com.i9algo.taxiadv.libs.utilcode.util.DeviceUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.StringUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.Utils;
import br.com.i9algo.taxiadv.v2.alarm.AlarmReceiver;
import br.com.i9algo.taxiadv.data.receiver.BatteryLowReceiver;
import br.com.i9algo.taxiadv.v2.alarm.AlarmRestartReceiver;
import br.com.i9algo.taxiadv.v2.alarm.KeepAliveAlarmReceiver;
import br.com.i9algo.taxiadv.v2.alarm.KeepAliveSchedulingService;
import br.com.i9algo.taxiadv.v2.alarm.SchedulingService;
import br.com.i9algo.taxiadv.v2.alarm.ScreenLockReceiver;
import br.com.i9algo.taxiadv.v2.db.AppDatabase;
import br.com.i9algo.taxiadv.v2.download.BatchDownloadFileReceiver;
import br.com.i9algo.taxiadv.v2.download.BatchDownloadService;
import br.com.i9algo.taxiadv.v2.event.SetIsDemoEvent;
import br.com.i9algo.taxiadv.v2.geo.GeofenceBroadcastReceiver;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.injection.component.ActivityComponent;
import br.com.i9algo.taxiadv.v2.injection.component.DaggerActivityComponent;
import br.com.i9algo.taxiadv.v2.injection.module.AppModule;
import br.com.i9algo.taxiadv.v2.injection.module.ContextModule;
import br.com.i9algo.taxiadv.v2.injection.module.DatabaseModule;
import br.com.i9algo.taxiadv.v2.injection.module.LocationModule;
import br.com.i9algo.taxiadv.v2.injection.module.NetworkModule;
import br.com.i9algo.taxiadv.v2.injection.component.SchedulerComponent;
import br.com.i9algo.taxiadv.v2.injection.module.ServiceModule;
import br.com.i9algo.taxiadv.v2.injection.module.SessionModule;
import br.com.i9algo.taxiadv.v2.injection.module.StateModule;
import br.com.i9algo.taxiadv.v2.injection.provides.AppStateManager;
import br.com.i9algo.taxiadv.v2.injection.provides.ProgrammingState;
import br.com.i9algo.taxiadv.v2.models.DeviceUser;
import br.com.i9algo.taxiadv.v2.storage.TaxiAdvOpenDBHelper;
import br.com.i9algo.taxiadv.v2.storage.firebase.RemoteConfigs;
import br.com.i9algo.taxiadv.v2.utils.Installation;
import br.com.i9algo.taxiadv.v2.utils.Pref;
import br.com.i9algo.taxiadv.v2.views.CustomErrorActivity;
import br.com.i9algo.taxiadv.v2.views.LauncherActivity;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

public class CustomApplication extends MultiDexApplication {

    private static final String TAG = CustomApplication.class.getSimpleName();

    public static boolean isDemo = false; // Pref.isDemo(context);
    public static int isDemoContentId = 0; // Pref.getDemoId(context);

    private AppExecutors mAppExecutors;

    // segment.com
    public Analytics segmentAnalytics;

    private static CustomApplication _app;
    private static ActivityComponent activityComponent;
    private Activity currentActivity = null;
    private AppModule appModule;
    private SchedulerComponent schedulerComponent;
    private StateModule stateModule;
    private AlarmReceiver alarm = new AlarmReceiver();
    private MixpanelAPI mMixpanel;

    /**
     * Static method for get application context
     *
     * @param context
     * @return
     */
    public static CustomApplication get(Context context) {
        return (CustomApplication) context.getApplicationContext();
    }

    public static CustomApplication getInstance() {
        return _app;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //BuildConfig.DEBUG // Identificar se eh uma versao debug, ou uma versao de APP Assinado
    @Override
    public void onCreate() {
        _app = this;
        Utils.init(_app);
        super.onCreate();
        mAppExecutors = new AppExecutors();
        Device.init(this);

        if (isDebug()) {
            Logger.v(TAG, "maxMemory -------> " + Runtime.getRuntime().maxMemory());
            Logger.v(TAG, "getMemoryClass -------> " + ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getMemoryClass());
        }

        // Gerar codigo de instalacao
        /*boolean firstAccess = Installation.isFirstAccess(getApplicationContext());
        if (firstAccess) {

        }
        Logger.v(TAG, "First Access ---> " + firstAccess);*/
        String installCode = Installation.id(getApplicationContext());
        Logger.v(TAG, "installCode ---> " + installCode);

        appModule = new AppModule(this);



        // Estava com algum problema nas requisicoes externas
        // Nao sei o que isso faz, mas resolveu!!!
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Instancia SQLite
        TaxiAdvOpenDBHelper helper = new TaxiAdvOpenDBHelper(this);

        stateModule = new StateModule(new AppStateManager(), new ProgrammingState());

        activityComponent = DaggerActivityComponent.builder()
                .appModule(appModule)
                .networkModule(new NetworkModule())
                .databaseModule(new DatabaseModule(helper))
                .contextModule(new ContextModule(this))
                .locationModule(new LocationModule(this))
                .sessionModule(new SessionModule(this))
                .build();


        initFirebase();
        initActivityOncrash();
        initSegmentIo();

        // Seta a variavel da SheredPrefs para definir se é versao demo ou nao
        Pref.setIsDemo(isDemo, isDemoContentId);

        ScreenLockReceiver screenLockReceiver = new ScreenLockReceiver();
        SchedulingService schedulingService = new SchedulingService(alarm);

        KeepAliveAlarmReceiver keepAliveAlarmReceiver = new KeepAliveAlarmReceiver();
        AlarmRestartReceiver alarmRestartReceiver = new AlarmRestartReceiver();

        BatchDownloadService batchDownloadService = new BatchDownloadService(activityComponent.bus());
        BatchDownloadFileReceiver fileReceiver = new BatchDownloadFileReceiver();
        NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver(activityComponent.bus());
        PowerConnectionReceiver powerConnectionReceiver = new PowerConnectionReceiver(activityComponent.bus());

        if (LauncherActivity.checkGooglePlayServices(this)) {
            schedulerComponent = activityComponent.schedulerComponent(new ServiceModule(schedulingService, alarm, new GeofenceBroadcastReceiver(), new BatteryLowReceiver(), new KeepAliveSchedulingService(keepAliveAlarmReceiver), keepAliveAlarmReceiver, batchDownloadService, fileReceiver, networkChangeReceiver, screenLockReceiver, powerConnectionReceiver));
        } else {
            schedulerComponent = activityComponent.schedulerComponent(new ServiceModule(schedulingService, alarm, new GeofenceBroadcastReceiver(), new BatteryLowReceiver(), null, null, batchDownloadService, fileReceiver, networkChangeReceiver, screenLockReceiver, powerConnectionReceiver));
        }

        // Mixpanel API
        this.mMixpanel = MixpanelAPI.getInstance(getApplicationContext(), Constants.MIXPANEL_PROJECT_TOKEN, true);

        alarm.setAlarm(this.getApplicationContext());

        if (keepAliveAlarmReceiver != null)
            keepAliveAlarmReceiver.setAlarm(this.getApplicationContext());

        if (alarmRestartReceiver != null)
            alarmRestartReceiver.setAlarm(this.getApplicationContext());

        helper.getWritableDatabase();
        activityComponent.bus().register(this);
    }

    private void initFirebase() {
        /**
         * Firebase
         */
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getToken();

        // Firebase Cloud Messaging
        //FirebaseMessaging.getInstance().setAutoInitEnabled(true); // on manifest -> firebase_messaging_auto_init_enabled
        // inscrever em um tópico especifico
        //FirebaseMessaging.getInstance().subscribeToTopic(getGroupName()); // group name
        FirebaseMessaging.getInstance().subscribeToTopic(getDevice().getDeviceSerial());
        //FirebaseMessaging.getInstance().subscribeToTopic(getDevice().getDevicePhoneNumber());
        //FirebaseMessaging.getInstance().subscribeToTopic(getDevice().getDeviceCarNumber());
        // Get Firebase FCM Token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Device.getInstance().setFCMToken(token);
            }
        });

        // Firebase RemoteConfigs
        // Desativado pois estava verificando atualizacao em todas as telas
        // Trecho foi passado para a classe 'AdvertSlideshowActivity'
        RemoteConfigs.init(this);

        try {
            // O setPersistenceEnabled precisa estar sozinho no try catch
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (DatabaseException e) {
            Logger.e(Logger.getStackTraceString(e));
        } catch (NoSuchMethodError | Exception e) {
            Logger.e(Logger.getStackTraceString(e));
        }
        /** End Firebase **/
    }

    private void initActivityOncrash() {
        /**************************************************
         * 		Custom Activity Oncrash
         * 	see: https://github.com/Ereza/CustomActivityOnCrash
         *************************************************/
        // Version 1.5.0
        // Isso faz com que a biblioteca nao inice a Activity de erro quando as falhas de aplicativos enquanto ele estiver no fundo.
        //CustomActivityOnCrash.setLaunchErrorActivityWhenInBackground(false);
        CustomActivityOnCrash.setRestartActivityClass(LauncherActivity.class);
        CustomActivityOnCrash.setShowErrorDetails(false);
        //CustomActivityOnCrash.setEnableAppRestart(false);
        //CustomActivityOnCrash.setDefaultErrorActivityDrawable(R.mipmap.ic_launcher);
        CustomActivityOnCrash.setErrorActivityClass(CustomErrorActivity.class);
        CustomActivityOnCrash.setEventListener(new CustomEventListener());
        CustomActivityOnCrash.install(this);
        /*************************************************/
        /*
        // Version 2.2.0
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(false) //default: true
                .showErrorDetails(false)
                .showRestartButton(false) //default: true
                .logErrorOnRestart(false) //default: true
                .trackActivities(true) //default: false
                .minTimeBetweenCrashesMs(2000) //default: 3000
                .errorDrawable(R.drawable.customactivityoncrash_error_image) //default: bug image
                .restartActivity(LauncherActivity.class)
                .errorActivity(CustomErrorActivity.class)
                .eventListener(new CustomEventListener())
                .apply();
         */
        /*************************************************/
    }

    private void initSegmentIo() {
        /*************************************************
         ##### segment.com
         *************************************************/
        // Create an analytics client with the given context and Segment write key.
        segmentAnalytics = new Analytics.Builder(getApplicationContext(), Constants.SEGMENT_KEY)
                // Enable this to record certain application events automatically!
                .trackApplicationLifecycleEvents()
                // Enable this to record screen views automatically!
                .recordScreenViews()
                .build();

        //Logger.v("LocationUpdatesBroadcast", "----->" + getDevice().toJsonObject().toString());
        Traits traits = new Traits();
        traits.putAll(getDevice().toStringMap());
        segmentAnalytics.alias(getDevice().getDeviceId());
        segmentAnalytics.identify(getDevice().getDeviceId());
        segmentAnalytics.identify(traits);

        // Set the initialized instance as a globally accessible instance.
        Analytics.setSingletonInstance(segmentAnalytics);
        /*************************************************/
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }
    public void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public FirebaseUser getFirebaseUser() {
        try {
            if (FirebaseAuth.getInstance() == null)
                return null;
        }catch (Exception ex) {
            return null;
        }
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public FirebaseAnalytics getFirebaseAnalytics(Context context) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.setSessionTimeoutDuration(1000 * 60 * 15);
        mFirebaseAnalytics.setUserProperty("device_uid", this.getDevice().getUid());
        mFirebaseAnalytics.setUserProperty("device_serial", this.getDevice().getDeviceSerial());
        mFirebaseAnalytics.setUserProperty("device_mac", this.getDevice().getDeviceMacAddress());
        mFirebaseAnalytics.setUserProperty("device_imei", this.getDevice().getDeviceImei());
        return mFirebaseAnalytics;
    }

    public MixpanelAPI getMixpanelAPI() {
        return mMixpanel;
    }

    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /*private boolean isMainProcess(Context context) {
        if (null == context) {
            return true;
        }
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        int pid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            String name = processInfo.processName;
            if (!StringUtils.isEmpty(name) && pid == processInfo.pid && name.equals(APPLICATION_ID)) {
                return true;
            }
        }
        return false;
    }*/

    public Device getDevice() {
        return Device.getInstance();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getDatabase(getDatabase());
    }

    /**
     * Retorna o diretorio onde esta sendo armazenado o cache
     * @return File
     */
    public final File getCacheDir() {
        return getApplicationContext().getExternalCacheDir();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.i("Application", "Application.onLowMemory()");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    static class CustomEventListener implements CustomActivityOnCrash.EventListener {
        @Override
        public void onLaunchErrorActivity() {
            Logger.e(TAG, "onLaunchErrorActivity()");
        }

        @Override
        public void onRestartAppFromErrorActivity() {
            Logger.e(TAG, "onRestartAppFromErrorActivity()");
        }

        @Override
        public void onCloseAppFromErrorActivity() {
            Logger.e(TAG, "onCloseAppFromErrorActivity()");
        }
    }

    private ActivityLifecycleCallbacks createLifecycleCallback() {
        return new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                appModule.setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        };
    }

    // post(new SetIsDemoEvent(isDemo, isDemoContentId));
    @Subscribe
    public void setIsDemo(SetIsDemoEvent isDemoEvent){
        Pref.setIsDemo(isDemoEvent.isDemoStatus(), isDemoEvent.getDemoContentId());
    }


    public SchedulerComponent getSchedulerComponent() {
        return schedulerComponent;
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

}
