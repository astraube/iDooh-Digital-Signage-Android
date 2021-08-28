package br.com.i9algo.taxiadv.v2.models.inbound.geo;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.i9algo.taxiadv.v2.helpers.Db;

public class Geometry {

    public static final String GEOJSON_GEOMETRY_TYPE = "geojson_geometry_type";
    public static final String GEOJSON_GEOMETRY_LATITUDE = "geojson_geometry_latitude";
    public static final String GEOJSON_GEOMETRY_LONGITUDE = "geojson_geometry_longitude";

    private String type;
    private List<Double> coordinates = new ArrayList<Double>();

    public Geometry() {
    }
    public Geometry(List<Double> coordinates, String type) {
        this.coordinates = coordinates;
        this.type = type;
    }
    public Geometry(Cursor cursor) {
        this.type = Db.getString(cursor, GEOJSON_GEOMETRY_TYPE, "");
        double geojson_geometry_latitude = Db.getFloat(cursor, GEOJSON_GEOMETRY_LATITUDE, 0);
        double geojson_geometry_longitude = Db.getFloat(cursor, GEOJSON_GEOMETRY_LONGITUDE, 0);

        List<Double> c = new ArrayList<Double>();
        c.add(geojson_geometry_latitude);
        c.add(geojson_geometry_longitude);
        this.coordinates = c;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public double getLongitude(){
        return coordinates.get(1);
    }

    public double getLatitude(){
        return coordinates.get(0);
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}