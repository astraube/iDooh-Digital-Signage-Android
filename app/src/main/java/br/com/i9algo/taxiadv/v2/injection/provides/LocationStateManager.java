package br.com.i9algo.taxiadv.v2.injection.provides;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

import java.util.List;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.v2.event.GeofenceListenerBoundariesEvent;
import br.com.i9algo.taxiadv.v2.geo.GeofenceBroadcastReceiver;
import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;
//import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LocationStateManager {

    private Location geoFenceRequestLocation = null;
    private final int LOCATION_REQUEST_DISTANCE_IN_METERS = 10000; // 10km

    @Inject
    MainThreadBus bus;
    private boolean isBusRegistered = false; //Bus has no method to tell you if you're registed or not.



    //ReactiveLocationProvider locationProvider;

    Context context;

    @Inject
    public LocationStateManager(Context context) {
        setLocationProvider(context);
        this.context = context;
    }

    public Location getGeoFenceRequestLocation() {
        return geoFenceRequestLocation;
    }

    public void setGeoFenceRequestLocation(Location geoFenceRequestLocation) {
        this.geoFenceRequestLocation = geoFenceRequestLocation;
    }

    /*public Observable<Location> getLocation(final boolean updateLocation) {
        Log.e("Geo", "getLocation");
        return locationProvider.getLastKnownLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Location, Observable<Location>>() {
                    @Override
                    public Observable<Location> call(Location location) {
                        Log.e("Geo", "getLocation:call " + location.toString());
                        if (updateLocation){
                            geoFenceRequestLocation = location;
                        }
                        return Observable.just(location);
                    }
                });

    }*/

    /*public Observable<Status> handleGeofences(List<Geofence> fences, Context context) {
        PendingIntent notificationPendingIntent =
                PendingIntent.getService(context, 0, new Intent(context, GeofenceBroadcastReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
    // Enjambre pra teste
        fences.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId("1")
                .setCircularRegion(
                        -25.494264,
                        -49.332121,
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
                        -25.506523,
                        -49.322035,
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
                        -25.501701,
                        -49.327495,
                        100
                )
                .setExpirationDuration(12 * 60 * 60 * 1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());


        return locationProvider.addGeofences(notificationPendingIntent, new GeofencingRequest.Builder().addGeofences(fences).build());
    }*/

    /*public void listenForLocationChanges() {
        this.registerBus();
        this.getLocation(false)
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        float distanceInMeters = geoFenceRequestLocation.distanceTo(location);
                        if (distanceInMeters >= LOCATION_REQUEST_DISTANCE_IN_METERS) { // 10 km
                            bus.post(new GeofenceListenerBoundariesEvent(geoFenceRequestLocation, location));
                            //get moar geofences
                            //listen to moar geofences
                        } // else, ignore
                    }
                });
    }*/

    //public

    private void registerBus() {
        if (!isBusRegistered) {
            bus.register(this);
        }
    }

    private void setLocationProvider(Context context){
        /*if (locationProvider == null){
            locationProvider = new ReactiveLocationProvider(context);
        }*/
    }
}
