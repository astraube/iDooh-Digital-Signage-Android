package br.com.i9algo.taxiadv.v2.views.slideshow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.firebase.perf.metrics.Trace;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import br.com.i9algo.taxiadv.BuildConfig;
import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.domain.constants.FirebaseVars;
import br.com.i9algo.taxiadv.domain.enums.ActionsPlaylistItem;
import br.com.i9algo.taxiadv.ui.recyclers.CategoryListView;
import br.com.i9algo.taxiadv.ui.recyclers.adapter.CategoryAdapter;
import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.data.receiver.LocationUpdatesBroadcastReceiver;
import br.com.i9algo.taxiadv.domain.constants.Constants;
import br.com.i9algo.taxiadv.ui.components.DynamicTextView;
import br.com.i9algo.taxiadv.ui.components.GeopointView;
import br.com.i9algo.taxiadv.ui.fragments.AboutFragment;
import br.com.i9algo.taxiadv.ui.fragments.GridFragment_V2;
import br.com.i9algo.taxiadv.ui.fragments.HtmlFragment;
import br.com.i9algo.taxiadv.ui.fragments.ReadMoreFragment;
import br.com.i9algo.taxiadv.ui.views.dialog.DialogLogin;
import br.com.i9algo.taxiadv.v2.alarm.ScreenLockReceiver;
import br.com.i9algo.taxiadv.v2.components.UpdaterApp.UpdateAppCheck;
import br.com.i9algo.taxiadv.v2.event.NewSessionEvent;
import br.com.i9algo.taxiadv.v2.event.ResizePlaylistEvent;
import br.com.i9algo.taxiadv.v2.injection.model.ObjectManager;
import br.com.i9algo.taxiadv.v2.injection.model.SessionTimeManager;
import br.com.i9algo.taxiadv.ui.components.ClockView;
import br.com.i9algo.taxiadv.v2.download.DownloadHelperDAO;
import br.com.i9algo.taxiadv.v2.event.GeofenceListenerBoundariesEvent;
import br.com.i9algo.taxiadv.v2.event.PowerConnectionEvent;
import br.com.i9algo.taxiadv.data.service.FloatButtonService;
import br.com.i9algo.taxiadv.v2.geo.GeofenceBroadcastReceiver;
import br.com.i9algo.taxiadv.v2.helpers.AnalyticsTrackerProvider;
import br.com.i9algo.taxiadv.v2.helpers.DateFormatHelper;
import br.com.i9algo.taxiadv.v2.helpers.LocationUtils;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.helpers.defaults.DefaultSubscriber;
import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;
import br.com.i9algo.taxiadv.v2.logging.LogFileWriteHelper;
import br.com.i9algo.taxiadv.v2.models.inbound.Category;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeofencedAdvert;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeofencedAdvertArraylistWrapper;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeographyData;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarItem;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowPlaylist;
import br.com.i9algo.taxiadv.v2.models.viewmodel.GridItemViewModel;
import br.com.i9algo.taxiadv.v2.network.taxiadv.AdvService;
import br.com.i9algo.taxiadv.v2.presenters.AdvertSlideshowPresenter;
import br.com.i9algo.taxiadv.v2.injection.provides.AppStateManager;
import br.com.i9algo.taxiadv.v2.injection.provides.LocationStateManager;
import br.com.i9algo.taxiadv.v2.storage.CategoryDAO;
import br.com.i9algo.taxiadv.v2.storage.FillerDAO;
import br.com.i9algo.taxiadv.v2.storage.GeofencedAdvertDAO;
import br.com.i9algo.taxiadv.v2.storage.SidebarDAO;
import br.com.i9algo.taxiadv.v2.storage.firebase.RemoteConfigs;
import br.com.i9algo.taxiadv.v2.utils.time.CountTimerThread;
import br.com.i9algo.taxiadv.v2.utils.LightUtil;
import br.com.i9algo.taxiadv.v2.utils.SoundUtil;
import br.com.i9algo.taxiadv.v2.views.BaseActivity;
import br.com.i9algo.taxiadv.v2.views.BaseFragment;
import br.com.i9algo.taxiadv.v2.views.LauncherActivity;
import br.com.i9algo.taxiadv.v2.views.slideshow.maintenance.MaintenanceFragment;
import br.com.i9algo.taxiadv.v2.views.widgets.playlistview.PlaylistViewV2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AdvertSlideshowActivity extends BaseActivity implements
        AdvertSlideshowViewInterface,
        CategoryAdapter.Delegate {


    private final String LOG_TAG = getClass().getSimpleName();

    @Inject
    AdvService advService;
    @Inject
    SidebarDAO sidebarDAO;
    @Inject
    CategoryDAO categoryDAO;
    @Inject
    AppStateManager appStateManager;
    @Inject
    SideBarAdvertFragment navigationFragment;
    @Inject
    AdvertSlideshowPresenter presenter;
    @Inject
    LocationStateManager locationStateManager;
    @Inject
    ObjectManager objectManager;
    @Inject
    SessionTimeManager sessionTimeManager;

    int lastPlaylistId = -1;

    @BindView(R.id.txtDebug)
    TextView txtDebug;
    @BindView(R.id.sidebar_frame)
    FrameLayout sidebarframe;
    @BindView(R.id.txtClock)
    ClockView clock;

    //@BindView(R.id.seekbarSound)
    //SeekbarPopup seekbarSound;

    @BindView(R.id.btSound)
    ImageButton btSound;

    //@BindView(R.id.seekbarLight)
    //SeekbarPopup seekbarLight;

    @BindView(R.id.btLight)
    ImageButton btLight;

    @BindView(R.id.txtDinamic)
    DynamicTextView mTxtDinamic;
    @BindView(R.id.playlist_view)
    PlaylistViewV2 mPlaylistView;

    @BindView(R.id.categoryListRecycler)
    CategoryListView mNavigationRecyclerView;

    @BindView(R.id.loadingPanel)
    RelativeLayout mLoadingPanel;
    @BindView(R.id.messageBox)
    TextView mMessageBox;

    //R.id.geopoints_frame - DO NOT BIND
    public static FrameLayout geopointsframe = null;
    BaseFragment currentFragment = null; // TUDO utilizar apenas currentFragment

    //Needs to unsubscribe whenever the activity is destroyed
    Subscription loadPlaylistSubscription;

    Subscription listenToGeofencesSubscription;
    @Inject
    GeofencedAdvertDAO geofencedAdvertDAO;
    @Inject
    FillerDAO fillerDAO;
    @Inject
    DownloadHelperDAO downloadHelperDAO;
    @Inject
    MainThreadBus bus;
    List<Geofence> fences;
    ScreenLockReceiver receiver;

    @BindView(R.id.container_taskbar)
    ConstraintLayout mTaskbar;

    DialogLogin dialog;

    // Timer para identificar inatividade do equipamento
    private CountTimerThread mTimerInactive;
    private CountTimerThread mTimerLoadingScreen;

    // LOCATION API
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;


    // Retornar o aplicativo ao estado de inatividade
    private Runnable restorePlaylistMethod = new Runnable() {
        public void run() {
            mTimerInactive.stopTimer();

            presenter.resetModel();

            // Maximizar a tela de video
            hideSidebarFragment();
            hideCurrentSidebarFragment();
        }
    };

    private Runnable hideLoadingScreenMethod = new Runnable() {
        public void run() {
            mLoadingPanel.setVisibility(View.GONE);
            mTimerLoadingScreen.stopTimer();
        }
    };


    @Override
    @AddTrace(name = "onCreateTrace", enabled = true /* optional */)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CustomApplication) getApplication()).getActivityComponent().inject(this);
        setContentView(R.layout.advert_slideshow_activity);
        ButterKnife.bind(this);
        presenter.bindView(this);
        bus.register(this);

        // TODO teste
        geopointsframe = (FrameLayout) findViewById(R.id.geopoints_frame);
        //GeopointView.setViewTrigger((FrameLayout) findViewById(R.id.geopoints_frame));

        // Inicializar dados para o MixPanel
        AnalyticsTrackerProvider.loginMixPanel(this);

        mPlaylistView.setDelegate(presenter);
        mPlaylistView.setDownloadDao(downloadHelperDAO);

        // Timer para restaurar a tela principal (Playlist)
        mTimerInactive = new CountTimerThread(new Handler(), restorePlaylistMethod, Constants.INTERVAL_RESTORE_MAIN_VIEW);

        //handleGeofences(null);
        //testLocation();


        // First we need to check availability of play services
        if (LauncherActivity.checkGooglePlayServices(this)) {
            // Check if the user revoked runtime permissions.
            if (!LocationUtils.checkPermissions(this)) {
                LocationUtils.requestPermissions(this);
            }

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mSettingsClient = LocationServices.getSettingsClient(this);

            createLocationRequest();
            buildLocationSettingsRequest();
            requestLocationUpdates();
        } else {
            Logger.v(LOG_TAG, "GPS OFF");
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        receiver = new ScreenLockReceiver();
        registerReceiver(receiver, filter);

        new UpdateAppCheck(this, RemoteConfigs.getEndpointApkUpdate());

        initializeLayout();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case Constants.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Logger.i("User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Logger.i("User chose not to make required location settings changes.");
                        //mRequestingLocationUpdates = false;
                        //updateUI();
                        break;
                }
                break;
        }
    }

    /**
     * Realiza todos os ajustes de layout
     */
    private void initializeLayout() {
        int taskbarH = RemoteConfigs.getDimenTaskbarHeight();
        mTaskbar.getLayoutParams().height = taskbarH;

        setupNavigationBar();
        setupTaskbar();
        loadPlaylist();
        initializeNewsFlash();
    }

    private void initializeNewsFlash() {
        //Logger.v(LOG_TAG, "NewsFlash - contentNewsflash: " + contentNewsflash);

        DatabaseReference categoryRef = FirebaseDatabase
                .getInstance(FirebaseVars.DB_CONTENT)
                .getReference(FirebaseVars.DB_CONTENT_CHILD_GROUP_CWB + FirebaseVars.DB_CONTENT_CHILD_NEWSFLASH);
        categoryRef.orderByValue().limitToLast(4).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChild) {
                //Logger.d(LOG_TAG, "@@@@@ onChildAdded " + snapshot.getKey() + " - " + snapshot.getValue());
                mTxtDinamic.addItem(snapshot.getKey(), " " + snapshot.getValue() + " "); // Maximo 75 caracteres
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {
                //Logger.d(LOG_TAG, "@@@@@ onChildChanged " + snapshot.getKey() + " - " + snapshot.getValue());
                mTxtDinamic.addItem(snapshot.getKey(), " " + snapshot.getValue() + " "); // Maximo 75 caracteres
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //Logger.d(LOG_TAG, "@@@@@ onChildRemoved " + snapshot.getKey() + " - " + snapshot.getValue());
                mTxtDinamic.removeItem(snapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {
                //Logger.d(LOG_TAG, "@@@@@ onChildMoved " + snapshot.getKey() + " - " + snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError snapshot) {
                //Logger.e(LOG_TAG, "@@@@@ onCancelled " + snapshot.getMessage());
                throw snapshot.toException();
            }
        });
        mTxtDinamic.setVisibility(View.VISIBLE);
    }


    /*****************************************************************/
    /********************** LOCATION API *****************************/
    /*****************************************************************/

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Define o tempo máximo em que as atualizações de localização em lote são entregues. Atualizações podem ser
        // entregue mais cedo que este intervalo.
        mLocationRequest.setMaxWaitTime(Constants.MAX_WAIT_TIME);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    private PendingIntent getPendingIntent() {
        // TODO(developer): uncomment to use PendingIntent.getService().
//        Intent intent = new Intent(this, LocationUpdatesIntentService.class);
//        intent.setAction(LocationUpdatesIntentService.ACTION_PROCESS_UPDATES);
//        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Logger.i( "onRequestPermissionResult");

        switch (requestCode) {
            case Constants.REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Logger.i( "User interaction was cancelled.");
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    requestLocationUpdates();
                } else {
                    // Permission denied.
                    Intent intent = new Intent();
                    intent.setAction(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",
                            BuildConfig.APPLICATION_ID, null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * Handles the Request Updates button and requests start of location updates.
     */
    public void requestLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Logger.i("All location settings are satisfied.");

                        try {
                            Logger.i("Starting location updates");
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest, getPendingIntent());
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Logger.i( "LocationRoom settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(AdvertSlideshowActivity.this, Constants.REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Logger.i( "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "LocationRoom settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Logger.e( errorMessage);
                        }
                    }
                });
    }

    /**********************
     * FIM LOCATION API
     ***********************/

    @Subscribe
    public void handleGeofences(GeofenceListenerBoundariesEvent event) {
        if (listenToGeofencesSubscription != null) {
            listenToGeofencesSubscription.unsubscribe();
        }
        listenToGeofencesSubscription = geofencedAdvertDAO.getAllGeofencedAdverts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<ArrayList<GeofencedAdvert>>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Logger.e(LOG_TAG, "onErroring");
                    }

                    @Override
                    public void onNext(ArrayList<GeofencedAdvert> adverts) {
                        super.onNext(adverts);
                        Logger.e(LOG_TAG, "onNext");

                        GeofencedAdvert advert = new GeofencedAdvert(1, "asdasd", "asdasdasd", new GeographyData());
                        Gson gson = new Gson();
                        Type type = new TypeToken<GeofencedAdvert>() {
                        }.getType();
                        String json = gson.toJson(advert, type);
                        System.out.println(json);
                        Logger.e(LOG_TAG, json);

                        fences = GeofencedAdvertArraylistWrapper.getGoogleGeofenceArraylist(adverts);
                        PendingIntent notificationPendingIntent =
                                PendingIntent.getBroadcast(AdvertSlideshowActivity.this, 0, new Intent(AdvertSlideshowActivity.this, GeofenceBroadcastReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
                        // Enjambre pra teste
                        fences.add(new Geofence.Builder()
                                // Set the request ID of the geofence. This is a string to identify this
                                // geofence.
                                .setRequestId("1")
                                .setCircularRegion(
                                        -25.49228721398866,
                                        -49.32320594787597,
                                        100
                                )
                                .setExpirationDuration(12 * 60 * 60 * 1000)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                        Geofence.GEOFENCE_TRANSITION_EXIT)
                                .build());

                        fences.add(new Geofence.Builder()
                                // Set the request ID of the geofence. This is a string to identify this
                                // geofence.
                                .setRequestId("2")
                                .setCircularRegion(
                                        -25.489381885501576,
                                        -49.32118892669678,
                                        100
                                )
                                .setExpirationDuration(12 * 60 * 60 * 1000)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                        Geofence.GEOFENCE_TRANSITION_EXIT)
                                .build());
                        fences.add(new Geofence.Builder()
                                // Set the request ID of the geofence. This is a string to identify this
                                // geofence.
                                .setRequestId("3")
                                .setCircularRegion(
                                        -25.48593413790335,
                                        -49.31861400604247,
                                        100
                                )
                                .setExpirationDuration(12 * 60 * 60 * 1000)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                        Geofence.GEOFENCE_TRANSITION_EXIT)
                                .build());
                        fences.add(new Geofence.Builder()
                                // Set the request ID of the geofence. This is a string to identify this
                                // geofence.
                                .setRequestId("4")
                                .setCircularRegion(
                                        -25.4773724493467,
                                        -49.30930137634277,
                                        100
                                )
                                .setExpirationDuration(12 * 60 * 60 * 1000)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                        Geofence.GEOFENCE_TRANSITION_EXIT)
                                .build());

                        fences.add(new Geofence.Builder()
                                // Set the request ID of the geofence. This is a string to identify this
                                // geofence.
                                .setRequestId("5")
                                .setCircularRegion(
                                        -25.494276,
                                        -49.332064,
                                        100
                                )
                                .setExpirationDuration(12 * 60 * 60 * 1000)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                        Geofence.GEOFENCE_TRANSITION_EXIT)
                                .build());

                        /*ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(getApplicationContext());
                        locationProvider.addGeofences(notificationPendingIntent, new GeofencingRequest.Builder().addGeofences(fences).build())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Status>() {
                                    @Override
                                    public void call(Status status) {
                                        Logger.e("geo", "Status retrieved");
                                    }
                                });*/
                    }

                    @Override
                    public void onCompleted() {
                        Logger.e(LOG_TAG, "Starting service");
                    }
                });

    }

    @SuppressLint("RestrictedApi")
    @Subscribe
    public void handlePowerOffEvent(PowerConnectionEvent event){
        Logger.i("@@", "handlePowerOffEvent -->  "+event);

        if (event.intent == null || event.intent.getAction() == null)
            return;

        String action = event.intent.getAction();

        if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
            // parar timer de desligamento de equipamento
            Logger.i("@@", "conectou cabo!!!");

            dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_POWER));

            // TODO - Nao esta ligando a tela
            return;
        } else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Logger.i("@@", "DESconectou cabo!!!");

            dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_POWER));

            // TODO - Nao esta desligando a tela
            // iniciar timer para desligamento de tela
            // desligar LCD após 10 minutos
            // desligar device após 20 minutos (apenas se tiver o dispositivo com auduino instalado junto)
            return;
        }
    }


    @Subscribe
    public void onResizePlaylistEvent(ResizePlaylistEvent event) {
        //Logger.d("@@", "-----> ResizePlaylistEvent <--- "+ event.getType() );

        if (event.getType().equals(ResizePlaylistEvent.ResizeEventType.MAXIMIZE) ) {
            //Logger.v(LOG_TAG, "PlayList - onMaximized");
            if (mTimerInactive != null)
                mTimerInactive.stopTimer();

        } else if (event.getType().equals(ResizePlaylistEvent.ResizeEventType.MINIMIZE))  {
            //Logger.v(LOG_TAG, "PlayList - onMinimized");
            if (mTimerInactive != null)
                mTimerInactive.resetTimer();
        }
    }

    @Subscribe
    public void onNewSessionEventEvent(NewSessionEvent event) {
        Logger.v(LOG_TAG, "@@@ ---> NewSessionEvent <---: " );
        AnalyticsTrackerProvider.sendNewSession(getContext(), event);
    }

    private void loadPlaylist() {
        // TODO - load from firebase firestore

        Logger.v(LOG_TAG, "@@@ - loadPlaylist");
        loadPlaylistSubscription = presenter.getFillers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<SlideshowItem>, Observable<SlideshowPlaylist>>() {
                    @Override
                    public Observable<SlideshowPlaylist> call(List<SlideshowItem> wrapper) {
                        Logger.v(LOG_TAG, "@@@ - Application has fetched " + wrapper.size() + " filler slides.");
                        LogFileWriteHelper.log("Application has fetched " + wrapper.size() + " filler slides.", AdvertSlideshowActivity.this.getContext());
                        mPlaylistView.setmFillers(wrapper);
                        return presenter.getCurrentPlaylist(DateFormatHelper.getTodaysDateAsString(), DateFormatHelper.getCurrentHourAsInt(), (DateFormatHelper.getCurrentHourAsInt() + 5));
                    }
                })
                .subscribe(new DefaultSubscriber<SlideshowPlaylist>() {
                    @Override
                    public void onNext(SlideshowPlaylist playlist) {
                        if (playlist == null) {
                            Logger.e(LOG_TAG, "@@@ - AdvertSlideshowActivity: found no content on database");
                            LogFileWriteHelper.log("Application has failed to find a playlist to play and will enter No Content Mode.", AdvertSlideshowActivity.this.getContext());

                            appStateManager.initializeNoContent();
                        } else {
                            Logger.v(LOG_TAG, "@@@ - playlist.getId(" + playlist.getId());

                            if (playlist.getId() != lastPlaylistId) {

                                LogFileWriteHelper.log("Application has found a suitable Playlist for the current time frame. The playlist id is " + playlist.getId(), AdvertSlideshowActivity.this.getContext());
                                lastPlaylistId = playlist.getId();

                                Logger.e(LOG_TAG, "retrieving playlist");
                                mPlaylistView.setPlaylist(playlist);
                                Logger.e(LOG_TAG, "playlist will start now");

                                if (mPlaylistView != null) {
                                    mPlaylistView.onPlayCurrentItem();
                                    mPlaylistView.maximize();
                                }
                                mPlaylistView.start();

                                Logger.e(LOG_TAG, "playlist started");

                                LogFileWriteHelper.log("Slideshow reproduction of Playlist with Id " + playlist.getId() + "will begin soon.", AdvertSlideshowActivity.this.getContext());
                            }
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

    // TODO apenas para teste
    public void showGeo(int id) {
        if (mPlaylistView.isMaximized()) {
            mPlaylistView.minimize();
        }
        if (geopointsframe == null) {
            Logger.e("geo", "geopointsframe == null");
            return;
        }

        Logger.e("geo", "showGeo");
        GeopointView geo = new GeopointView(geopointsframe.getContext(), new GeopointView.OnGeopointListener() {
            @Override
            public void onCloseGeopoint(GeopointView geo) {
                Logger.e("geo", "onCloseGeopoint");
                geopointsframe.removeView(geo);
                if (currentFragment == null) {
                    mPlaylistView.maximize();
                }
                if (geopointsframe.getChildCount() > 1) {
                    geopointsframe.setVisibility(View.GONE);
                }
            }
        });
        geopointsframe.addView(geo);

        geopointsframe.setVisibility(View.VISIBLE);
    }

    public void debug(String str) {
        if (str != null) {
            if (txtDebug.getVisibility() != View.VISIBLE) {
                txtDebug.setVisibility(View.VISIBLE);
                ((ScrollView) findViewById(R.id.scrollDebug)).setVisibility(View.VISIBLE);
            }
            txtDebug.append(str);
            txtDebug.append("\n");
        }

    }


    //This snippet serves to tells us if a service is running or not.
    @Override
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void showFloat() {
        if (this.isMyServiceRunning(FloatButtonService.class)) {
            Intent i = new Intent(AdvertSlideshowActivity.this, FloatButtonService.class);
            i.putExtra("name", "stoppingService");
            AdvertSlideshowActivity.this.stopService(i);
        } else {
            Intent i = new Intent(AdvertSlideshowActivity.this, FloatButtonService.class);
            i.putExtra("name", "startingService");
            AdvertSlideshowActivity.this.startService(i);

        }
    }

    @Override
    public int getCurrentSoundLevel() {
        int level = SoundUtil.getSoundLevel(getContext());
        return level;
    }

    @Override
    public void delegateActionSlide(SlideshowItem item) {
        // https://firebase.google.com/docs/perf-mon/get-started-android
        Trace myTrace = FirebasePerformance.getInstance().newTrace("delegateActionSlide");
        myTrace.start();

        Logger.v(LOG_TAG, "@@@ - delegateActionSlide - " + item.getActionModel());

        ActionsPlaylistItem actionModel = ActionsPlaylistItem.valueOf(item.getActionModel());
        int id = item.getActionModelId();
        //String token = item.getActionModelToken();

        Logger.d(getClass().getSimpleName(), "### actionModel - " + actionModel);
        Logger.d(getClass().getSimpleName(), "### delegateActionSlide - " + id);
        //Logger.d(getClass().getSimpleName(), "### delegateActionSlide - " + token);

        if (id > -1) {
            //Logger.d(LOG_TAG, "@@@ " + actionModel + " - " + id);

            if (actionModel == ActionsPlaylistItem.CATEGORY) {

                //Logger.d(LOG_TAG, "@#=> Abrir Categoria - " + id);

                // SEE: https://github.com/firebase/snippets-android/blob/fc6286da0e4ea63ec730429f0896f28cf791216e/firestore/app/src/main/java/com/google/example/firestore/DocSnippets.java#L136-L139
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(true)
                        .build();
                db.setFirestoreSettings(settings);

                db.collection("content").document("v1").collection("category")
                        .whereEqualTo("id", id)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NotNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot result = task.getResult();

                                    for (DocumentChange change : result.getDocumentChanges()) {
                                        QueryDocumentSnapshot doc = change.getDocument();

                                        if (doc.exists()) {
                                            //Logger.d(LOG_TAG, doc.getId() + " @#=> " + doc.getData());

                                            Category cat = Category.fromDataDocmentSnapshot(doc);

                                            presenter.showNavigationClickAction(cat);

                                        } else {
                                            Logger.d(LOG_TAG, "@#=> No such document");
                                        }
                                    }
                                } else {
                                    Logger.d(LOG_TAG, "get failed with " + task.getException());
                                }
                            }
                        });

                /**
                 * 16/07/2019
                 * desabilitado, dados estao no FIREBASE
                 *
                categoryDAO.getCategory(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultSubscriber<Category>() {
                            @Override
                            public void onNext(Category result) {
                                super.onNext(result);

                                Logger.d(LOG_TAG, "@@@ Categoria - " + (result == null));

                                if (result == null)
                                    return;

                                presenter.showNavigationClickAction(result);
                            }
                            @Override
                            public void onCompleted() {
                                Logger.v(LOG_TAG, "finished seeCategory");
                            }
                            @Override
                            public void onError(Throwable e) {
                                Logger.e(LOG_TAG, e);
                                e.printStackTrace();
                            }
                        });*/

            } else if (actionModel == ActionsPlaylistItem.SIDEBAR) {
                Logger.d(LOG_TAG, "@@@ Abrir Sidebar - " + id);
                //Logger.d(LOG_TAG, "@@@ Abrir Sidebar - " + token);
                sidebarDAO.getSidebarById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultSubscriber<SidebarItem>() {
                            @Override
                            public void onNext(SidebarItem result) {
                                super.onNext(result);

                                Logger.d(LOG_TAG, "Sidebar - " + (result == null));

                                if (result == null)
                                    return;

                                GridItemViewModel model = new GridItemViewModel(1, id,
                                        result.getCoverImage(),
                                        result.getTitle(),
                                        result.getUrl(),
                                        result.getType(),
                                        result.getDateStartEvent());

                                showSidebarFragment(result.getCategoryId(), model.getSidebarId());
                            }

                            @Override
                            public void onCompleted() {
                                Logger.v(LOG_TAG, "finished seeSidebar");
                            }
                            @Override
                            public void onError(Throwable e) {
                                Logger.e(LOG_TAG, e);
                                e.printStackTrace();
                            }
                        });
            }
        }

        myTrace.stop();
    }

    private DialogLogin mValidDialog = null;

    @Override
    public void showMaintenanceFragment() {
        // Caso queira desativar o login para a configuracao
        // Para quando clicar no botao 'btn_config' entra direto nas configuracoes do android
        // desconmentar esse trexo
        // sendToAndroidConfigMenu();

        // e comentar esse resto de codigo
        Runnable runResult = new Runnable() {
            public void run() {
                mTimerInactive.resetTimer();
                currentFragment.onDestroy();
                currentFragment = new MaintenanceFragment(presenter);
                mPlaylistView.minimize();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.sidebar_frame, currentFragment, "maintenanceFragment");
                ft.commit();
            }
        };
        this.mValidDialog = new DialogLogin(this, runResult);

    }

    @Override
    public void sendToAndroidConfigMenu() {
        showFloat();
        Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }

    // TODO depreciado...
    @Override
    public void setDialogFlag(boolean flag) {
        // TODO depreciado...
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        //if (!presenter.click())
           //return;

        //Logger.v(LOG_TAG, "---# onUserInteraction");

        // Normalizar Audio
        //SoundUtil.setSoundNormalize(getContext());

        /*
        // Normalizar Luminosidade
        float levelLight = LightUtil.getLightMaxLevel(AdvertSlideshowActivity.this);
        if (LightUtil.getLightLevel(AdvertSlideshowActivity.this) < levelLight / 2) {
            LightUtil.setLightLevel(AdvertSlideshowActivity.this, (int) levelLight * 100);
        }
        */

        // Iniciar o timer de restauracao da tela principal
        if (mTimerInactive != null)
            this.mTimerInactive.resetTimer();

        this.sessionTimeManager.onUserInteraction();

        if (mPlaylistView.getCurrentItem() != null) {
            AnalyticsTrackerProvider.sendAnyInteraction(getContext(), mPlaylistView.getCurrentItem().getTitle(), mPlaylistView.getCurrentItem().getId());
        }
        //Logger.e(LOG_TAG, "ResetSchedulerEvent");
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
            //Logger.i("@@", "dispatchKeyEvent - " + event.getKeyCode());
            if (RemoteConfigs.isCloseSystemDialog())
                closeSystemDialog();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //Logger.d("@@@", "Focus changed ! " + hasFocus);

        if (!hasFocus && (mValidDialog != null && !mValidDialog.getDialog().isShowing())) {
            Logger.d("@@@", "Lost focus !");

            if (RemoteConfigs.isCloseSystemDialog())
                closeSystemDialog();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        /*if (mAdView != null) {
            mAdView.pause();
        }*/
        super.onPause();
        mPlaylistView.onPauseCurrentItem();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (mAdView != null) {
            mAdView.resume();
        }*/
        //Logger.v("onResume");

        mPlaylistView.onPlayCurrentItem();
        mPlaylistView.maximize();
    }

    @Override
    public void onDestroy() {
        /*if (mAdView != null) {
            mAdView.destroy();
        }*/
        //Controller.getInstance().onDestroy();

        super.onDestroy();
    }

    /**
     * Methods exposed to presenter
     */

    @Override
    public void closeGeoView() {
        if (currentFragment == null) {
            mPlaylistView.minimize();
        }
    }

    @Override
    public void showHtmlViewSidebar(Category category) {
        BaseFragment frag = new HtmlFragment();
        showSidebarFragment(frag, category.getName());
    }

    @Override
    public void showHTMLSidebarFragment(GridItemViewModel advertising) {
        BaseFragment frag = new ReadMoreFragment();
        ((ReadMoreFragment) frag).setModel(advertising);
        showSidebarFragment(frag, advertising.getItemName());
    }

    @Override
    public void showGridViewSidebar(Category category) {
        BaseFragment frag = new GridFragment_V2(sidebarDAO);
        ((GridFragment_V2) frag).setModel(category);
        ((GridFragment_V2) frag).setDelegate(presenter);
        showSidebarFragment(frag, category.getName());
    }

    @Override
    public void showAboutFragment() {
        BaseFragment frag = new AboutFragment();
        ((AboutFragment) frag).setDelegate(presenter);
        //frag.setArguments(); // TODO enviar bundle de argumentos
        showSidebarFragment(frag, "About");
    }

    /**
     * Entrou em um dos itens do Sidebar
     * @param fragment
     * @param titleFragment
     */
    public void showSidebarFragment(BaseFragment fragment, String titleFragment) {
        Logger.v(LOG_TAG, "showSideBarFragment  " + "\n" + " - titleFragment: " + titleFragment);

        mTimerInactive.resetTimer();

        hideReadMoreFragment();
        hideSidebarFragment();

        if (currentFragment != null)
            currentFragment.onDestroy();

        currentFragment = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.sidebar_frame, currentFragment, titleFragment);
        ft.commit();
        // Minimizar playlist
        mPlaylistView.minimize();
    }

    @Override
    public void showSidebarFragment(int category, int id) {
        Logger.v(LOG_TAG, "showSidebarFragment \n" +
                " - Category: " + category + "\n" +
                " - ID: " + id);

        mTimerInactive.resetTimer();
        if (navigationFragment != null && navigationFragment.isVisible()) {

        } else {
            //navigationFragment = new SideBarFeedFragment();
            // https://github.com/prof18/RSS-Parser/blob/master/app/src/main/java/com/prof/rssparser/example/MainActivity.java

            navigationFragment = new SideBarAdvertFragment(advService, sidebarDAO);
            navigationFragment.setCategory(category);
            navigationFragment.setId(id);

            mPlaylistView.minimize();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.sidebar_frame, navigationFragment, "navigationFragment");
            ft.commit();
        }
    }

    @Override
    public void showLoadingScreen() {
        Logger.v(LOG_TAG, "showLoadingScreen");
        mMessageBox.setText("Atualizando...");
        mMessageBox.setTextColor(getResources().getColor(R.color.black));
        mLoadingPanel.bringToFront();
        mLoadingPanel.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingErrorScreen() {
        Logger.v(LOG_TAG, "showLoadingScreen");
        mMessageBox.setText("Erro ao Atualizar Playlist!");
        mMessageBox.setTextColor(getResources().getColor(R.color.redPrimary));
        mTimerLoadingScreen = new CountTimerThread(new Handler(), hideLoadingScreenMethod, 3000);
        mTimerLoadingScreen.resetTimer();

    }

    @Override
    public void showLoadingSuccessfulScreen() {
        Logger.v(LOG_TAG, "showLoadingScreen");
        mMessageBox.setText("Playlist Atualizada com Sucesso!");
        mMessageBox.setTextColor(getResources().getColor(R.color.green));
        mTimerLoadingScreen = new CountTimerThread(new Handler(), hideLoadingScreenMethod, 3000);
        mTimerLoadingScreen.resetTimer();
    }

    @Override
    public boolean isLoading() {
        if (mLoadingPanel.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<SlideshowItem> getPlaylistFromPlaylistView() {
        return mPlaylistView.getCurrentPlaylist();
    }

    @Override
    public void hideAboutFragment() {
        hideCurrentSidebarFragment();
    }

    @Override
    public void hideParquesFragment() {
        hideCurrentSidebarFragment();
    }

    @Override
    public void hideDrinksFragment() {
        hideCurrentSidebarFragment();
    }

    @Override
    public void hideSidebarFragment() {
        if (navigationFragment != null) {
            try {
                getSupportFragmentManager().beginTransaction().remove(navigationFragment).commit();
                navigationFragment = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            mPlaylistView.maximize();
        }

    }

    @Override
    public void reloadPlaylist() {
        loadPlaylist();
    }

    @Override
    public void showGeofenceAdvert(GeofencedAdvert geofencedAdvert) {
        Logger.e("GEO", "showGeofenceAdvert");
        showGeo(geofencedAdvert.getId());
    }

    @Override
    public void writeOnDebug(String something) {
        //debug(something);
    }

    public boolean isCurrentSidebarFragmentVisible() {
        return (currentFragment != null && currentFragment.isVisible());
    }

    public void hideCurrentSidebarFragment() {
        if (currentFragment != null) {
            try {
                currentFragment.onDestroy();
                getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                currentFragment = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Maximizar Playlist
            mPlaylistView.maximize();
        }
    }

    private void hideReadMoreFragment() {
        hideCurrentSidebarFragment();
    }

    public void hideAllSidebarFragments() {
        hideCurrentSidebarFragment();
        hideSidebarFragment();
        hideAboutFragment();
        hideDrinksFragment();
        hideParquesFragment();
        hideReadMoreFragment();
    }

    /**
     * User interaction Methods
     */

    @OnClick(R.id.txtClock)
    public void onAboutTapped() {
        presenter.clockAction();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mPlaylistView.isMaximized()) {
            //this.finish();
            //System.exit(0);
        } else {
            if (currentFragment.getClass().equals(AboutFragment.class)) {
                if (((AboutFragment) currentFragment).isShowingSlideshowList()) {
                    ((AboutFragment) currentFragment).hideSlideshowViewer();
                } else {
                    hideAllSidebarFragments();
                }
            } else {
                hideAllSidebarFragments();
            }
        }
    }

    private Map<Object, Category> cats;
    private CategoryAdapter catAdapter;

    /**
     * Setup methods
     */
    private void setupNavigationBar() {
        Logger.e(LOG_TAG, "@@@ setupNavigationBar");

        cats = new HashMap<Object, Category>();
        catAdapter = new CategoryAdapter(cats, R.layout.category_item_layout, AdvertSlideshowActivity.this, presenter, mNavigationRecyclerView);

        /**
         * 05/07/2019
         * desabilitado, dados estao no FIREBASE
         *
        appStateManager.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<Category>>() {
                    @Override
                    public void call(ArrayList<Category> categories) {
                        Logger.v(LOG_TAG, "got categories");
                        if (categories != null && categories.size() > 0) {
                            mNavigationRecyclerView.setAdapter(new CategoryAdapter(categories, R.layout.category_item_layout, AdvertSlideshowActivity.this, presenter) );
                        } else {
                            mNavigationRecyclerView.setVisibility(View.GONE);
                        }
                        Logger.v(LOG_TAG, "finished categories");
                    }
                });
         */


        // SEE: https://github.com/firebase/snippets-android/blob/fc6286da0e4ea63ec730429f0896f28cf791216e/firestore/app/src/main/java/com/google/example/firestore/DocSnippets.java#L136-L139
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        // TODO - revisar collection e grupos
        db.collection("content").document("v1").collection("category")
                .whereArrayContains("group", Constants.GROUP)
                .whereEqualTo("status", true)
                .orderBy("order")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Logger.e(LOG_TAG, "Listen error", e);
                            return;
                        }
                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {

                            Logger.d(LOG_TAG, " @#=> " + change.getType());

                            if (change.getType() == DocumentChange.Type.ADDED) {
                                QueryDocumentSnapshot doc = change.getDocument();
                                //Logger.d(LOG_TAG, doc.getId() + " @#=> " + doc.getData());
                                Logger.d(LOG_TAG, doc.getId() + " @#=> " + doc.getString("name"));

                                Category cat = Category.fromDataDocmentSnapshot(doc);
                                catAdapter.addObject(doc.getId(), cat, mNavigationRecyclerView);

                            } else if (change.getType() == DocumentChange.Type.MODIFIED) {
                                QueryDocumentSnapshot doc = change.getDocument();
                                //Logger.d(LOG_TAG, doc.getId() + " @#=> " + doc.getData());
                                Logger.d(LOG_TAG, doc.getId() + " @#=> " + doc.getString("name"));

                                Category cat = Category.fromDataDocmentSnapshot(doc);
                                catAdapter.changeObject(doc.getId(), cat, mNavigationRecyclerView);

                            } else if (change.getType() == DocumentChange.Type.REMOVED) {
                                QueryDocumentSnapshot doc = change.getDocument();
                                //Logger.d(LOG_TAG, doc.getId() + " @#=> " + doc.getData());
                                Logger.d(LOG_TAG, doc.getId() + " @#=> " + doc.getString("name"));

                                //Category cat = Category.fromDataDocmentSnapshot(doc);
                                catAdapter.removeObject(doc.getId(), mNavigationRecyclerView);
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ? "local cache" : "server";
                            Logger.d(LOG_TAG, "@#=> Data fetched from " + source);
                        }

                    }
                });

        mNavigationRecyclerView.setAdapter( catAdapter );
    }

    public void updateCategoryUI() {
        mNavigationRecyclerView.refreshDrawableState();
        mNavigationRecyclerView.invalidate();
        if (cats == null || cats.size() <= 0) {
            mNavigationRecyclerView.setVisibility(View.GONE);
        }
    }

    public static void print(String n)
    {
        System.out.println("Student Name is " + n);
    }

    private void setupTaskbar() {
        setupBTSound();
        setupBTLight();
    }
    private void setupBTSound() {
        updateIconBtSound();
    }
    private void setupBTLight() {
        // Restaura luminosidade para o maximo
        LightUtil.setLightNormalize(this);
        updateIconBtLight();
    }

    @OnClick(R.id.btSound)
    public void changeSound(View v) {
        Logger.v(LOG_TAG, "changeSound");

        if (SoundUtil.getSoundLevel(getContext()) > 0)
            SoundUtil.setSoundLevel(getContext(), -1);
        else {
            SoundUtil.setSoundNormalize(getContext());
        }
        updateIconBtSound();
    }

    private void updateIconBtSound() {
        //Logger.v(LOG_TAG, "updateIconBtSound");
        if (getCurrentSoundLevel() > 0) {
            Drawable btSoundImage = getResources().getDrawable(R.drawable.ic_volume_high_white_36dp);
            btSound.setImageDrawable(btSoundImage);
        } else {
            Drawable btSoundImage = getResources().getDrawable(R.drawable.ic_volume_off_white_36dp);
            btSound.setImageDrawable(btSoundImage);
        }
    }
    
    @OnClick(R.id.btLight)
    public void toggleLight(View v) {
        Logger.v(LOG_TAG, "toggleLight");

        float minLivel = 0.01F;
        float level = LightUtil.getLightLevel(AdvertSlideshowActivity.this);

        if (level > minLivel)
            LightUtil.setLightLevelOffset(AdvertSlideshowActivity.this, minLivel);
        else
            LightUtil.setLightLevelOffset(AdvertSlideshowActivity.this, LightUtil.getLightMaxLevel(AdvertSlideshowActivity.this));

        updateIconBtLight();
    }
    private void updateIconBtLight() {
        Drawable btLightImage = getResources().getDrawable(R.drawable.ic_brightness_6_white_36dp);
        btLight.setImageDrawable(btLightImage);

        // O icone de luminosidade nao possui outro estado
        /*if (getCurrentSoundLevel() > 0) {
            Drawable btSoundImage = getResources().getDrawable(R.drawable.ic_brightness_6_white_48dp);
            btSound.setImageDrawable(btSoundImage);
        } else {
            Drawable btSoundImage = getResources().getDrawable(R.drawable.ic_brightness_6_white_48dp);
            btSound.setImageDrawable(btSoundImage);
        }*/
    }

}
