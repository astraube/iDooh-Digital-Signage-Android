package br.com.i9algo.taxiadv.v2.models.inbound.geo;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.android.gms.location.Geofence;

import br.com.i9algo.taxiadv.v2.helpers.Db;

public class GeofencedAdvert {

    private int id;

    private String name;

    private String url;

    private GeographyData geojson;

    public static final String TABLE = "geofence";
    public static final String ID = Db.ID;

    public static final String URL = "url";
    public static final String NAME = "name";

    public GeofencedAdvert(int id, String name, String url, GeographyData geojson) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.geojson = geojson;
    }

    public GeofencedAdvert(int id, String url, GeographyData geojson) {
        this.id = id;
        this.url = url;
        this.geojson = geojson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public GeographyData getGeojson() {
        return geojson;
    }

    public void setGeojson(GeographyData geojson) {
        this.geojson = geojson;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ID, getId());
        values.put(NAME, getName());
        values.put(URL, getUrl());

        values.put(Geometry.GEOJSON_GEOMETRY_TYPE, getGeojson().getGeometry().getType());
        values.put(Geometry.GEOJSON_GEOMETRY_LATITUDE, getGeojson().getGeometry().getLatitude());
        values.put(Geometry.GEOJSON_GEOMETRY_LONGITUDE, getGeojson().getGeometry().getLongitude());

        values.put(GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_COLOR, getGeojson().getProperties().getMarkercolor());
        values.put(GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_SIZE, getGeojson().getProperties().getMarkersize());
        values.put(GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_SYMBOL, getGeojson().getProperties().getMarkersymbol());
        values.put(GeolocalisationData.GEOJSON_PROPERTIES_NAME, getGeojson().getProperties().getName());
        values.put(GeolocalisationData.GEOJSON_PROPERTIES_ADDRESS, getGeojson().getProperties().getAddress());
        values.put(GeolocalisationData.GEOJSON_PROPERTIES_CITY, getGeojson().getProperties().getCity());
        values.put(GeolocalisationData.GEOJSON_PROPERTIES_STATE, getGeojson().getProperties().getState());
        values.put(GeolocalisationData.GEOJSON_PROPERTIES_UF, getGeojson().getProperties().getUf());
        values.put(GeolocalisationData.GEOJSON_PROPERTIES_ZIPCODE, getGeojson().getProperties().getZipcode());
        values.put(GeolocalisationData.GEOJSON_PROPERTIES_COUNTRY, getGeojson().getProperties().getCountry());

        values.put(GeographyData.GEOJSON_TYPE, getGeojson().getType());
        return values;
    }

    public static GeofencedAdvert fromCursor(Cursor cursor) {

        int id = Db.getInt(cursor, ID, -1);
        String name = Db.getString(cursor, NAME, "");
        String url = Db.getString(cursor, URL, "");

        Geometry geometry = new Geometry(cursor);

        String geojson_property_markercolor = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_COLOR, "");
        String geojson_property_markersize = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_SIZE, "");
        String geojson_property_markersymbol = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_SYMBOL, "");
        String geojson_property_name = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_NAME, "");
        String geojson_property_address = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_ADDRESS, "");
        String geojson_property_city = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_CITY, "");
        String geojson_property_state = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_STATE, "");
        String geojson_property_uf = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_UF, "");
        String geojson_property_zipcode = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_ZIPCODE, "");
        String geojson_property_country = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_COUNTRY, "");
        GeolocalisationData properties = new GeolocalisationData(geojson_property_markercolor, geojson_property_markersize, geojson_property_markersymbol, geojson_property_name, geojson_property_address, geojson_property_city, geojson_property_state, geojson_property_uf, geojson_property_zipcode, geojson_property_country);

        String geojson_type = Db.getString(cursor, GeographyData.GEOJSON_TYPE, "");
        GeographyData geoJson = new GeographyData(geojson_type, geometry, properties);

        GeofencedAdvert advert = new GeofencedAdvert(id, name, url, geoJson);
        //int demoid = Db.getInt(cursor, DEMO_ID, 0);
        //advert.setDemoId(demoid);
        return advert;
    }

    public static Geofence getGeofenceFromAdvert(GeofencedAdvert advert) {
        return new Geofence.Builder()
                .setRequestId(Integer.toString(advert.getId()))
                .setCircularRegion(
                        advert.getGeojson().getGeometry().getLatitude(),
                        advert.getGeojson().getGeometry().getLongitude(),
                        100
                )
                .setExpirationDuration(12* 60 * 60 * 1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }
}
