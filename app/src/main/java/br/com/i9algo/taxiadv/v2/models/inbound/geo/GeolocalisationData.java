package br.com.i9algo.taxiadv.v2.models.inbound.geo;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import br.com.i9algo.taxiadv.v2.helpers.Db;

public class GeolocalisationData {

    public static final String GEOJSON_PROPERTIES_DATA_MARKER_COLOR = "geojson_properties_data_marker_color";
    public static final String GEOJSON_PROPERTIES_DATA_MARKER_SIZE = "geojson_properties_data_marker_size";
    public static final String GEOJSON_PROPERTIES_DATA_MARKER_SYMBOL = "geojson_properties_data_marker_symbol";
    public static final String GEOJSON_PROPERTIES_NAME = "geojson_properties_name";
    public static final String GEOJSON_PROPERTIES_ADDRESS = "geojson_properties_address";
    public static final String GEOJSON_PROPERTIES_CITY = "geojson_properties_city";
    public static final String GEOJSON_PROPERTIES_STATE = "geojson_properties_state";
    public static final String GEOJSON_PROPERTIES_UF = "geojson_properties_uf";
    public static final String GEOJSON_PROPERTIES_ZIPCODE = "geojson_properties_zipcode";
    public static final String GEOJSON_PROPERTIES_COUNTRY = "geojson_properties_country";

    @SerializedName("marker-color")
    private String markercolor;

    @SerializedName("marker-size")
    private String markersize;

    @SerializedName("marker-symbol")
    private String markersymbol;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("uf")
    private String uf;

    @SerializedName("zip-code")
    private String zipcode;

    @SerializedName("country")
    private String country;

    public String getMarkercolor() {
        return markercolor;
    }
    public void setMarkercolor(String markercolor) {
        this.markercolor = markercolor;
    }

    public String getMarkersize() {
        return markersize;
    }
    public void setMarkersize(String markersize) {
        this.markersize = markersize;
    }

    public String getMarkersymbol() {
        return markersymbol;
    }
    public void setMarkersymbol(String markersymbol) {
        this.markersymbol = markersymbol;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getUf() {
        return uf;
    }
    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getZipcode() {
        return zipcode;
    }
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public GeolocalisationData() {
    }

    public GeolocalisationData(String markercolor, String markersize, String markersymbol, String name, String address, String city, String state, String uf, String zipcode, String country) {
        this.markercolor = markercolor;
        this.markersize = markersize;
        this.markersymbol = markersymbol;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.uf = uf;
        this.zipcode = zipcode;
        this.country = country;
    }

    public GeolocalisationData(Cursor cursor) {
        this.markercolor = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_COLOR, "");
        this.markersize = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_SIZE, "");
        this.markersymbol = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_SYMBOL, "");
        this.name = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_NAME, "");
        this.address = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_ADDRESS, "");
        this.city = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_CITY, "");
        this.state = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_STATE, "");
        this.uf = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_UF, "");
        this.zipcode = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_ZIPCODE, "");
        this.country = Db.getString(cursor, GeolocalisationData.GEOJSON_PROPERTIES_COUNTRY, "");
    }
}