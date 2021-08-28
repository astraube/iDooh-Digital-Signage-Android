package br.com.i9algo.taxiadv.v2.storage.firebase;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import androidx.annotation.NonNull;
import br.com.i9algo.taxiadv.BuildConfig;
import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.utils.SoundUtil;

public class RemoteConfigs {

    private final String LOG_TAG = getClass().getSimpleName();

    // Remote Config keys
    public static final String time_minute_newsession_interaction = "time_minute_newsession_interaction";
    public static final String time_minute_newsession_normal = "time_minute_newsession_normal";
    public static final String time_minute_min_update_content = "time_minute_min_update_content";
    public static final String time_minute_max_update_content = "time_minute_max_update_content";
    public static final String audio_max = "audio_max";
    public static final String audio_normalize = "audio_normalize";
    public static final String db_save_in_which = "db_save_in_which";
    public static final String endpoint_api = "endpoint_api";
    public static final String endpoint_apk_update = "endpoint_apk_update";
    public static final String endpoint_playlist_no_content = "endpoint_playlist_no_content";
    public static final String color_bg_category = "color_bg_category";
    public static final String password_maintenance = "password_maintenance";
    public static final String dimen_taskbar_height = "dimen_taskbar_height";
    public static final String close_system_dialog = "close_system_dialog";
    //public static final String content_newsflash = "content_newsflash";

    private static RemoteConfigs mInstance;

    private final Context mContext;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public RemoteConfigs(Context context) {
        this.mInstance = this;
        this.mContext = context;
        this.startFirebaseConfigs();
    }

    public static RemoteConfigs init(Context context) {
        if(RemoteConfigs.mInstance == null) {
            return new RemoteConfigs(context);
        }
        return RemoteConfigs.mInstance;
    }

    public FirebaseRemoteConfig getFirebaseRemoteConfig() {
        return this.mFirebaseRemoteConfig;
    }

    private void startFirebaseConfigs() {
        //Logger.i(LOG_TAG, "-----> startFirebaseConfigs");

        // https://console.firebase.google.com/u/0/project/taxiadv-dubai/config

        // Get Remote Config instance.
        // [START get_remote_config_instance]
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // [END get_remote_config_instance]

        // Create a Remote Config Setting to enable developer mode, which you can use to increase
        // the number of fetches available per hour during development. See Best Practices in the
        // README for more information.
        // [START enable_dev_mode]
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        // [END enable_dev_mode]


        mFirebaseRemoteConfig.setDefaults(R.xml.firebase_remote_config_defaults);

        fetchFirebaseConfigs();
    }

    /**
     * Fetch a welcome message from the Firebase Remote Config service, and then activate it.
     */
    private void fetchFirebaseConfigs() {
        //Logger.i(LOG_TAG, "-----> fetchFirebaseConfigs");

        displayFirebaseConfigs();

        long cacheExpiration = 43200L; // 12 hours in seconds.
        //long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
        // will use fetch data from the Remote Config service, rather than cached parameter values,
        // if cached parameter values are more than cacheExpiration seconds old.
        // See Best Practices in the README for more information.
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {

                        }
                        displayFirebaseConfigs();
                    }
                });
        // [END fetch_config_with_callback]
    }
    private void displayFirebaseConfigs() {
        fbconfigs_audio();
    }
    private void fbconfigs_audio() {
        boolean audioMax = mFirebaseRemoteConfig.getBoolean(audio_max);
        boolean audioNorm = mFirebaseRemoteConfig.getBoolean(audio_normalize);

        SoundUtil.setSoundOff(this.mContext);

        if (audioMax) {
            Logger.i(LOG_TAG, "audioMax-----> " + audioMax);
            SoundUtil.setSoundMax(this.mContext);
        }
        if (audioNorm) {
            Logger.i(LOG_TAG, "audioNorm-----> " + audioNorm);
            SoundUtil.setSoundNormalize(this.mContext);
        }
    }

    /**
     * Salvar em qual banco de dados?
     * dynamodb / firebase database / firebase datastore
     * @return
     */
    public static String getDbSaveInWhich() {
        if (mInstance == null)
            return "";
        return mInstance.mFirebaseRemoteConfig.getString(db_save_in_which);
    }

    public static String getEndpointApi() {
        if (mInstance == null)
            return "";
        return mInstance.mFirebaseRemoteConfig.getString(endpoint_api);
    }

    public static String getEndpointApkUpdate() {
        if (mInstance == null)
            return "";
        return mInstance.mFirebaseRemoteConfig.getString(endpoint_apk_update);
    }

    public static String getColorBgCategory() {
        if (mInstance == null)
            return "";
        return mInstance.mFirebaseRemoteConfig.getString(color_bg_category);
    }

    public static String getPasswordMaintenance() {
        if (mInstance == null)
            return "";
        return mInstance.mFirebaseRemoteConfig.getString(password_maintenance);
    }

    public static long getTimeMinUpdateContent() {
        if (mInstance == null)
            return 60;
        return mInstance.mFirebaseRemoteConfig.getLong(time_minute_min_update_content);
    }

    public static long getTimeMaxUpdateContent() {
        if (mInstance == null)
            return 60;
        return mInstance.mFirebaseRemoteConfig.getLong(time_minute_max_update_content);
    }

    public static long getTimeNewSessionInteraction(){
        if (mInstance == null)
            return 5;
        return mInstance.mFirebaseRemoteConfig.getLong(time_minute_newsession_interaction);
    }

    public static long getTimeNewSessionNormal(){
        if (mInstance == null)
            return 10;
        return mInstance.mFirebaseRemoteConfig.getLong(time_minute_newsession_normal);
    }

    public static int getDimenTaskbarHeight(){
        if (mInstance == null)
            return 50;
        return (int)mInstance.mFirebaseRemoteConfig.getLong(dimen_taskbar_height);
    }

    public static boolean isCloseSystemDialog() {
        if (mInstance == null)
            return false;
        return mInstance.mFirebaseRemoteConfig.getBoolean(close_system_dialog);
    }
}
