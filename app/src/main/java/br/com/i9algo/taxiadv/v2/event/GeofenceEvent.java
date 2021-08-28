package br.com.i9algo.taxiadv.v2.event;

public class GeofenceEvent extends BasicEvent{

    private int geofenceid;

    public int getGeofenceid() {
        return geofenceid;
    }

    public GeofenceEvent(int geofenceid) {
        this.geofenceid = geofenceid;
    }

    public GeofenceEvent() {
        super();
    }
}
