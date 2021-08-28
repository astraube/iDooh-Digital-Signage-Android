package br.com.i9algo.taxiadv.data.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.LocationResult;

import java.util.List;

import br.com.i9algo.taxiadv.data.task.LocationUpdateSendAsyncTask;
import br.com.i9algo.taxiadv.v2.helpers.LocationUtils;
import br.com.i9algo.taxiadv.v2.helpers.Logger;


/**
 * Lida com as atualizações de localização recebidas e exibe uma notificação com os dados de localização.
 *
 * For apps targeting API level 25 ("Nougat") or lower, location updates may be requested
 * using {@link android.app.PendingIntent#getService(Context, int, Intent, int)} or
 * {@link android.app.PendingIntent#getBroadcast(Context, int, Intent, int)}. For apps targeting
 * API level O, only {@code getBroadcast} should be used.
 *
 *  Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 *  less frequently than the interval specified in the
 *  {@link com.google.android.gms.location.LocationRequest} when the app is no longer in the
 *  foreground.
 */
public class LocationUpdatesIntentService extends IntentService {

    private static final String ACTION_PROCESS_UPDATES =
            "br.com.i9algo.taxiadv.action" + ".PROCESS_UPDATES";

    private static final String TAG = LocationUpdatesIntentService.class.getSimpleName();


    public LocationUpdatesIntentService() {
        // Name the worker thread.
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();

                    //Logger.i(TAG, "locations: " + locations.toArray());

                    //LocationUtils.setLocationUpdatesResult(this, locations);
                    //Logger.i(TAG, "getLocationUpdatesResult: " + LocationUtils.getLocationUpdatesResult(this));
                    //NotificationUtils.sendNotification(this, LocationUtils.getLocationResultTitle(this, locations));

                    Location loc = LocationUtils.setLastLocation(this, locations);
                    Logger.i(TAG, "LastUpdate: " + LocationUtils.getLastLocation(this));

                    //Location loc = LocationUtils.getLocationResult(locations);

                    new LocationUpdateSendAsyncTask(this).execute(loc);
                }
            }
        }
    }
}
