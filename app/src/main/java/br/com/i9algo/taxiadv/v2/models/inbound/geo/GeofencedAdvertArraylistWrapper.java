package br.com.i9algo.taxiadv.v2.models.inbound.geo;

import com.google.android.gms.location.Geofence;

import java.util.ArrayList;
import java.util.List;

public class GeofencedAdvertArraylistWrapper {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public GeofencedAdvertArraylistWrapper() {
    }

    public GeofencedAdvertArraylistWrapper(List<GeofencedAdvert> genfencedAdverts) {
        this.data = new Data(genfencedAdverts);
    }

    public static List<Geofence> getGoogleGeofenceArraylist(ArrayList<GeofencedAdvert> adverts) {
        List<Geofence> mGeofencesToAdd = new ArrayList<>();
        if (!adverts.isEmpty()){
            for (GeofencedAdvert advert : adverts){
                mGeofencesToAdd.add(GeofencedAdvert.getGeofenceFromAdvert(advert));
            }
        }
        return mGeofencesToAdd;
    }

    public class Data {
        private List<GeofencedAdvert> genfencedAdverts = new ArrayList<>();

        public List<GeofencedAdvert> getGeofencedAdverts() {
            return genfencedAdverts;
        }

        public void setGeofencedAdverts(List<GeofencedAdvert> genfencedAdverts) {
            this.genfencedAdverts = genfencedAdverts;
        }

        public Data(List<GeofencedAdvert> genfencedAdverts) {
            this.genfencedAdverts = genfencedAdverts;
        }
    }

}
