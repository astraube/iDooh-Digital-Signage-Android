package br.com.i9algo.taxiadv.v2.event;

import android.location.Location;

public class GeofenceListenerBoundariesEvent extends BasicEvent {

    private Location previousLocation;
    private Location newCurrentLocation;

    public GeofenceListenerBoundariesEvent(Location previousLocation, Location newCurrentLocation) {
        this.previousLocation = previousLocation;
        this.newCurrentLocation = newCurrentLocation;
    }

    public Location getPreviousLocation() {
        return previousLocation;
    }

    public void setPreviousLocation(Location previousLocation) {
        this.previousLocation = previousLocation;
    }

    public Location getNewCurrentLocation() {
        return newCurrentLocation;
    }

    public void setNewCurrentLocation(Location newCurrentLocation) {
        this.newCurrentLocation = newCurrentLocation;
    }

    public GeofenceListenerBoundariesEvent() {
        super();
    }
}
