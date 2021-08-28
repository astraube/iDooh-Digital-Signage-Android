package br.com.i9algo.taxiadv.v2.models.inbound.geo;

public class GeographyData {

    public static final String GEOJSON_TYPE = "geojson_type";

    private String type;
    private Geometry geometry;
    private GeolocalisationData properties;

    public GeographyData() {
    }
    public GeographyData(String type, Geometry geometry, GeolocalisationData properties) {
        this.type = type;
        this.geometry = geometry;
        this.properties = properties;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Geometry getGeometry() { return geometry; }
    public void setGeometry(Geometry geometry) { this.geometry = geometry; }

    public GeolocalisationData getProperties() { return properties; }
    public void setProperties(GeolocalisationData properties) { this.properties = properties; }
}