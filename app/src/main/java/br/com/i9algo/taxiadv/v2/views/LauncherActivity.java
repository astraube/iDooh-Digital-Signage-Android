package br.com.i9algo.taxiadv.v2.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.PictureDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import javax.inject.Inject;

import androidx.annotation.NonNull;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.domain.constants.FirebaseVars;
import br.com.i9algo.taxiadv.libs.utilcode.util.PhoneUtils;
import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.domain.constants.Constants;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.helpers.defaults.DefaultSubscriber;
import br.com.i9algo.taxiadv.v2.logging.LogFileWriteHelper;
import br.com.i9algo.taxiadv.v2.models.DeviceUser;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowPlaylist;
import br.com.i9algo.taxiadv.v2.injection.provides.AppStateManager;
import br.com.i9algo.taxiadv.v2.utils.time.CountTimerThread;
import br.com.i9algo.taxiadv.libs.utilcode.util.NetworkUtils;
import br.com.i9algo.taxiadv.v2.views.slideshow.AdvertSlideshowActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LauncherActivity extends BaseActivity implements LauncherScreenViewInterface {

    private final String LOG_TAG = getClass().getSimpleName();

    @Inject
    AppStateManager stateManager;

    // Loader V1
    //@BindView(R.id.fillableLoader)
    //FillableLoader fillableLoader;

    // Loader V2
    @BindView(R.id.videoLoader)
    VideoView videoLoader;

    private Subscription contentSubscription;
    private Subscription initializeSubscription;

    boolean isHandlingNoInternet = false;

    public static Intent createIntent(Context context) {
        return new Intent(context, LauncherActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);
        ((CustomApplication) getApplication()).getActivityComponent().inject(this);
        ButterKnife.bind(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Loader - V1
        //fillableLoader.setSvgPath(Paths.LOGO_APP);
        //fillableLoader.start();

        // Loader - V2
        Uri uriVideoLoader = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.idooh_vinheta);
        videoLoader.setVideoURI(uriVideoLoader);
        videoLoader.start();
        videoLoader.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        CountTimerThread mTimerInactive = new CountTimerThread(new Handler(), startLaunchMethod, Constants.INTERVAL_START_APP);
        mTimerInactive.resetTimer();

        checkGooglePlayServices(LauncherActivity.this);
    }

    /*synchronized private void runInitialCommands() {
        try {
            List<String> commands = new ArrayList<>();
            commands.add("settings put secure location_providers_allowed +gps,network");
            commands.add("settings put system screen_off_timeout -1");
            commands.add("settings put secure install_non_market_apps 1");
            commands.add("pm grant " + getApplicationContext().getPackageName() + " android.permission.WRITE_SECURE_SETTINGS");
            ShellUtils.execCmd(commands, DeviceUtils.isDeviceRooted());

        } catch (Exception ex) {
            Logger.e(LOG_TAG, ex);
            ex.printStackTrace();
        }
    }*/

    public static boolean checkGooglePlayServices(Context context){
        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);

        if (result == ConnectionResult.SUCCESS){
            return true;
        }
        if (result > ConnectionResult.SUCCESS && context instanceof Activity)
            GoogleApiAvailability.getInstance().getErrorDialog((Activity)context, result,0).show();

        return false;
    }

    boolean appHasLoaded = false;

    private Runnable startLaunchMethod = new Runnable() {
        public void run() {

            boolean isConnected = NetworkUtils.isConnected();
            if (isConnected) {
                init();
            } else {
                handleNoInternet();
            }
        }
    };

    private void init() {
        //FirebaseUser fbUser = getApp().getFirebaseUser();
        /*if (fbUser != null) {
            fbUser.reload();
            initConfigApproval();
        } else {
            onFirebaseUserLogin();
        }*/
        onFirebaseUserLogin();
    }

    private void onFirebaseUserLogin() {
        Log.d(LOG_TAG, "@@@ - " + "onFirebaseUserLogin");

        Log.d(LOG_TAG, "@@@ - " + "getUsername: " + DeviceUser.getUsername());
        Log.d(LOG_TAG, "@@@ - " + "getPassword: " + DeviceUser.getPassword());

        // TODO criar log para saber quando foi efetuado login
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(DeviceUser.getUsername(), DeviceUser.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        onCompleteAuth(task, "SIGNIN");
                    }
                });
    }
    private void onFirebaseUserCreate(){
        Log.d(LOG_TAG, "@@@ - " + "onFirebaseUserCreate");

        //Log.d(LOG_TAG, "@@@ - " + "getUsername: " + DeviceUser.getUsername());
        //Log.d(LOG_TAG, "@@@ - " + "getPassword: " + DeviceUser.getPassword());

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(DeviceUser.getUsername(), DeviceUser.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        onCompleteAuth(task, "SIGNUP");
                    }
                });
    }

    public void onCompleteAuth(@NonNull Task<AuthResult> task, String action) {
        if (task.isSuccessful()) {
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();

            Log.d(LOG_TAG, "@@@ - " + "signInWithEmail:success");
            FirebaseUser fbUser = mAuth.getCurrentUser();

            if (fbUser != null) {
                Log.d(LOG_TAG, "@@@ - " + "UUUID: " + fbUser.getUid());
                DeviceUser.setUidByFirebase(fbUser);
            }
            // stateManager.sendDeviceInfos(LauncherActivity.this.getApplicationContext());

            launchApp();

        } else {
            Log.d(LOG_TAG, "@@@ - " + "signInWithEmail:failure", task.getException());

            onFirebaseUserCreate();
        }
    }

    private void initConfigApproval() {
        Log.d(LOG_TAG, "@@@ - " + "initConfigApproval");

        final String devId = DeviceUser.getUid();

        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("device").document("v1").collection("approval")
                .whereEqualTo("id", devId)
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
                });*/

        FirebaseDatabase db = FirebaseDatabase.getInstance(FirebaseVars.DB_DEVICE);
        DatabaseReference dbRef = db.getReference().child(FirebaseVars.DB_DEVICE_CHILD_APPROVAL);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object network = dataSnapshot.child(devId).getValue();
                if (network != null) {
                    Log.d(LOG_TAG, "@@@ - " + network.toString());

                    if (Boolean.getBoolean(network.toString())) {
                        Log.d(LOG_TAG, "@@@ - " + "1");
                        launchApp();
                    } else {
                        Log.d(LOG_TAG, "@@@ - " + "2");
                        showAdmLoginApproval();
                    }

                } else {
                    // First Acccess
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(devId, true); // true = Aprovar automaticamente | false = adm aprova cada device cadastrado
                    dbRef.updateChildren(result);

                    showAdmLoginApproval();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, "@@@ - " + "loadPost:onCancelled", databaseError.toException());
            }
        };
        dbRef.addValueEventListener(postListener);
    }
    private void showAdmLoginApproval() {
        Log.d(LOG_TAG, "@@@ - " + "showAdmLoginApproval");
        // exibir popup senha de ADM
        launchApp();
    }

    @SuppressLint("MissingPermission")
    private void launchApp() {
        boolean isConnected = NetworkUtils.isConnected();
        Logger.i(LOG_TAG, "isOnline: " + isConnected );

        if (isConnected) {

            Logger.i(LOG_TAG, "@@@ - isOnline. About to get Uid");
            Logger.i(LOG_TAG, "@@@ - stateManager: " + stateManager.toString());
            Logger.i(LOG_TAG, "@@@ - getSerial(): " + PhoneUtils.getSerial());
            Logger.i(LOG_TAG, "@@@ - getUid(): " + DeviceUser.getUid());

            appHasLoaded = true;

            LogFileWriteHelper.log("Application is initializing on Regular Mode.", getContext());

            initializeSubscription = stateManager.initialize()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultSubscriber<Boolean>() {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            Logger.e(LOG_TAG, "initializeSubscription onErroring");
                            Logger.e(LOG_TAG, e);
                            e.printStackTrace();
                            handleNoInternet();
                            forwardToView(AdvertSlideshowActivity.class);
                            initializeSubscription.unsubscribe();
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            super.onNext(aBoolean);
                            Logger.i(LOG_TAG, "OnNexting");
                            if (aBoolean) {
                                Logger.i(LOG_TAG, "Starting aplication");
                                forwardToView(AdvertSlideshowActivity.class);
                                LauncherActivity.this.finish();
                            } else {
                                handleNoInternet();
                            }
                            initializeSubscription.unsubscribe();
                        }

                        @Override
                        public void onCompleted() {
                            initializeSubscription.unsubscribe();
                            Logger.i(LOG_TAG, "Starting aplication");
                            forwardToView(AdvertSlideshowActivity.class);
                            LauncherActivity.this.finish();
                            initializeSubscription.unsubscribe();
                        }
                    });
        } else {
            handleNoInternet();
        }
    }

    private void handleNoInternet() {
        Logger.i(LOG_TAG, "handleNoInternet()");
        if (isHandlingNoInternet == false) {
            isHandlingNoInternet = true;
            contentSubscription = stateManager.initializeNoConnection()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultSubscriber<SlideshowPlaylist>() {
                        @Override
                        public void onError(Throwable e) {
                            Logger.e(LOG_TAG, "handleNoInternet");
                            super.onError(e);
                        }

                        @Override
                        public void onNext(SlideshowPlaylist playlist) {
                            super.onNext(playlist);
                            if (playlist == null) {
                                Logger.i(LOG_TAG, "About to initialize No Content");
                                stateManager.initializeNoContent();
                                forwardToView(AdvertSlideshowActivity.class);
                                contentSubscription.unsubscribe();
                                LauncherActivity.this.finish();
                            } else {
                                Logger.i(LOG_TAG, "About to initialize Old Content");
                                stateManager.initializeOldContent();
                                forwardToView(AdvertSlideshowActivity.class);
                                contentSubscription.unsubscribe();
                                LauncherActivity.this.finish();
                                contentSubscription.unsubscribe();
                            }
                            contentSubscription.unsubscribe();
                            Logger.i(LOG_TAG, "initializeNoConnection onNext");

                        }

                        @Override
                        public void onCompleted() {
                            contentSubscription.unsubscribe();
                            Logger.i(LOG_TAG, "initializeNoConnection onCompleted");
                        }
                    });
        } else {
            contentSubscription.unsubscribe();
        }
    }
}