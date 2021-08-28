package br.com.i9algo.taxiadv.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import com.google.android.gms.location.LocationResult;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.i9algo.taxiadv.data.task.LocationUpdateSendAsyncTask;
import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.v2.event.LocationChangedEvent;
import br.com.i9algo.taxiadv.v2.helpers.LocationUtils;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.network.taxiadv.IdoohMediaDeviceController;
import br.com.i9algo.taxiadv.v2.utils.Pref;

/**
 * Receptor para lidar com atualizações de localização.
 *
 * For apps targeting API level O
 * {@link android.app.PendingIntent#getBroadcast(Context, int, Intent, int)} should be used when
 * requesting location updates. Due to limits on background services,
 * {@link android.app.PendingIntent#getService(Context, int, Intent, int)} should not be used.
 *
 *  Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 *  less frequently than the interval specified in the
 *  {@link com.google.android.gms.location.LocationRequest} when the app is no longer in the
 *  foreground.
 */
public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = LocationUpdatesBroadcastReceiver.class.getSimpleName();

    public static final String ACTION_PROCESS_UPDATES =
            "br.com.i9algo.taxiadv.action" + ".PROCESS_UPDATES";

    @Override
    public void onReceive(Context context, Intent intent) {
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

                    Location loc = LocationUtils.setLastLocation(context, locations);
                    Logger.i(TAG, "LastUpdate: " + LocationUtils.getLastLocation(context));

                    // TODO - nao esta sendo utilizado corretamento o eventBus
                    LocationChangedEvent locationEvent = new LocationChangedEvent(context, loc);
                    //((CustomApplication)context.getApplicationContext()).getActivityComponent().bus().post(new LocationChangedEvent(context, loc));
                    receiveLocationChangedEvent(locationEvent);

                    //LocationEntity locationEntity = new LocationEntity(loc);
                    //((CustomApplication) context.getApplicationContext()).getRepository().insertLocation(locationEntity);
                }
            }
        }
    }

    @Subscribe
    public void receiveLocationChangedEvent(LocationChangedEvent event){
        Logger.v(TAG, "receiveLocationChangedEvent ----------->");

        new LocationUpdateSendAsyncTask(event.getContext()).execute(event.getLocation());

        /*
        // identificar cidade para criar grupo automaticamente
        Geocoder geocoder = new Geocoder(event.getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(event.getLatitude(), event.getLongitude(), 1);

            String code = addresses.get(0).getAdminArea().toLowerCase();
            Pref.setNetworkContentGroup(code);
            //Logger.v(TAG, "----------> " + Pref.getNetworkContentGroup());

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
