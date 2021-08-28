package br.com.i9algo.taxiadv.v2.models.inbound.sidebar;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.i9algo.taxiadv.v2.helpers.Db;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeographyData;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeolocalisationData;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.Geometry;
import br.com.i9algo.taxiadv.v2.utils.DateUtils;

public class SidebarItem {

    public static final String TABLE = "sidebar";

    public static final String ID = Db.ID;

    public static final String TOKEN = "token";
    public static final String CATEGORY_ID = "category_id";
    public static final String DATE_CREATED = "created_at";
    public static final String DATE_UPDATED = "updated_at";
    public static final String DATE_START_EVENT = "start_at"; // Data inicial do evento
    public static final String DATE_END_EVENT = "end_at"; // Data final do evento
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String BODY = "body";
    public static final String MAIN_IMAGE = "main_image";
    public static final String COVER_IMAGE = "cover_image";
    public static final String COVER_BG_COLOR = "cover_bg_color";
    public static final String QRCODE = "qrcode";
    public static final String URL = "url";
    public static final String TYPE = "type";
    public static final String TICKET = "ticket";
    public static final String IS_FREE = "is_free";
    public static final String PROPERTIES = "properties";
    public static final String PHONE = "phone";
    public static final String GEOJSON = "geojson";

    public static final String LOCATION_ID = "location_id";

    @SerializedName(ID)
    private int id;

    @SerializedName(CATEGORY_ID)
    private int categoryId;

    @SerializedName(TOKEN)
    private String token;

    @SerializedName(DATE_CREATED)
    private String createdAt;

    @SerializedName(DATE_UPDATED)
    private String updatedAt;

    @SerializedName(DATE_START_EVENT)
    private String startAt;

    @SerializedName(DATE_END_EVENT)
    private String endAt;

    @SerializedName(TITLE)
    private String title;

    @SerializedName(DESCRIPTION)
    private String description;

    @SerializedName(BODY)
    private String body;

    @SerializedName(PHONE)
    private String phone;

    @SerializedName(MAIN_IMAGE)
    private String mainImage;

    @SerializedName(COVER_IMAGE)
    private String coverImage;

    @SerializedName(COVER_BG_COLOR)
    private String coverBgColor;

    @SerializedName(URL)
    private String url;

    @SerializedName(QRCODE)
    private String qrcode;

    @SerializedName(TYPE)
    private String type;

    @SerializedName(IS_FREE)
    private boolean isFree;

    @SerializedName(TICKET)
    private List<SidebarTicket> ticket = new ArrayList<SidebarTicket>();

    private List<SidebarProperties> properties = new ArrayList<SidebarProperties>();

    @SerializedName(GEOJSON)
    private GeographyData geojson;


    public SidebarItem(int id, int categoryId, String token, String createdAt, String updatedAt, String startAt, String endAt,
                       boolean isFree, String title, String description, String body, String phone, String mainImage,
                       List<SidebarProperties> properties, GeographyData geojson, String coverImage, String coverBgColor,
                       String _url, String qrcode, String type, List<SidebarTicket> _ticket) {
        this.id = id;
        this.categoryId = categoryId;
        this.token = token;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startAt = startAt;
        this.endAt = endAt;
        this.isFree = isFree;
        this.title = title;
        this.description = description;
        this.body = body;
        this.phone = phone;
        this.properties = properties;
        this.geojson = geojson;
        this.mainImage = mainImage;
        this.coverImage = coverImage;
        this.coverBgColor = coverBgColor;
        this.url = _url;
        this.qrcode = qrcode;
        this.type = type;
        this.ticket = _ticket;
    }

    public SidebarItem(int id, int categoryId, String token, String createdAt, String updatedAt, String startAt, String endAt,
                       boolean isFree, String title, String description, String body, String phone,
                       GeographyData geojson, String mainImage, String coverImage, String coverBgColor, String _url, String qrcode, String type) {
        this.id = id;
        this.categoryId = categoryId;
        this.token = token;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startAt = startAt;
        this.endAt = endAt;
        this.isFree = isFree;
        this.title = title;
        this.description = description;
        this.body = body;
        this.phone = phone;
        this.geojson = geojson;
        this.mainImage = mainImage;
        this.coverImage = coverImage;
        this.coverBgColor = coverBgColor;
        this.url = _url;
        this.qrcode = qrcode;
        this.type = type;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ID, getId());
        values.put(CATEGORY_ID, getCategoryId());
        values.put(TOKEN, getToken());
        values.put(DATE_CREATED, getDateCreated());
        values.put(DATE_UPDATED, getDateUpdate());
        values.put(DATE_START_EVENT, getDateStartEvent());
        values.put(DATE_END_EVENT, getDateEndEvent());
        values.put(TITLE, getTitle());
        values.put(DESCRIPTION, getDescription());
        values.put(BODY, getBody());
        values.put(MAIN_IMAGE, getMainImage());
        values.put(COVER_IMAGE, getCoverImage());
        values.put(COVER_BG_COLOR, getCoverBgColor());
        values.put(URL, getUrl());
        values.put(QRCODE, getQrcode());
        values.put(TYPE, getType());

        values.put(IS_FREE, getIsFree());

        if (getTicket() != null){
            values.put(TICKET, getFlatTickets());
        }
        if (getProperties() != null){
            values.put(PROPERTIES, getFlatProperties());
        }
        if (!TextUtils.isEmpty( getPhone() )){
            values.put(PHONE, getPhone());
        }
        if (getGeoJson()!= null){
            values.put(Geometry.GEOJSON_GEOMETRY_TYPE, getGeoJson().getGeometry().getType());
            values.put(Geometry.GEOJSON_GEOMETRY_LATITUDE, getGeoJson().getGeometry().getLatitude());
            values.put(Geometry.GEOJSON_GEOMETRY_LONGITUDE, getGeoJson().getGeometry().getLongitude());

            values.put(GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_COLOR, getGeoJson().getProperties().getMarkercolor());
            values.put(GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_SIZE, getGeoJson().getProperties().getMarkersize());
            values.put(GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_SYMBOL, getGeoJson().getProperties().getMarkersymbol());
            values.put(GeolocalisationData.GEOJSON_PROPERTIES_NAME, getGeoJson().getProperties().getName());
            values.put(GeolocalisationData.GEOJSON_PROPERTIES_ADDRESS, getGeoJson().getProperties().getAddress());
            values.put(GeolocalisationData.GEOJSON_PROPERTIES_CITY, getGeoJson().getProperties().getCity());
            values.put(GeolocalisationData.GEOJSON_PROPERTIES_STATE, getGeoJson().getProperties().getState());
            values.put(GeolocalisationData.GEOJSON_PROPERTIES_UF, getGeoJson().getProperties().getUf());
            values.put(GeolocalisationData.GEOJSON_PROPERTIES_ZIPCODE, getGeoJson().getProperties().getZipcode());
            values.put(GeolocalisationData.GEOJSON_PROPERTIES_COUNTRY, getGeoJson().getProperties().getCountry());

            values.put(GeographyData.GEOJSON_TYPE, getGeoJson().getType());
        }

        return values;
    }

    public static SidebarItem fromCursor(Cursor cursor) {
        int id = Db.getInt(cursor, ID, -1);
        int categoryId = Db.getInt(cursor, CATEGORY_ID, -1);
        String token = Db.getString(cursor, TOKEN, "");
        String createdAt = Db.getString(cursor, DATE_CREATED, "");
        String updatedAt = Db.getString(cursor, DATE_UPDATED, "");
        String startAt = Db.getString(cursor, DATE_START_EVENT, "");
        String endAt = Db.getString(cursor, DATE_END_EVENT, "");
        String title = Db.getString(cursor, TITLE, "");
        String description = Db.getString(cursor, DESCRIPTION, "");
        String body = Db.getString(cursor, BODY, "");
        String main_image = Db.getString(cursor, MAIN_IMAGE, "");
        String cover_image = Db.getString(cursor, COVER_IMAGE, "");
        String coverBgColor = Db.getString(cursor, COVER_BG_COLOR, "");
        String url = Db.getString(cursor, URL, "");
        String qrcode = Db.getString(cursor, QRCODE, "");
        String type = Db.getString(cursor, TYPE, "DEFAULT");
        String phone = Db.getString(cursor, PHONE, "");
        //boolean isFree = Db.getBoolean(cursor, IS_FREE, true);
        //boolean isFree = (Db.getInt(cursor, IS_FREE, 1) == 1);
        boolean isFree = Db.getBoolean(cursor, IS_FREE, true);
        String marhsalledTickets = Db.getString(cursor, TICKET, "");
        String marhsalledProperties = Db.getString(cursor, PROPERTIES, "");

        Geometry geometry = new Geometry(cursor);
        GeolocalisationData properties = new GeolocalisationData(cursor);

        String geojson_type = Db.getString(cursor, GeographyData.GEOJSON_TYPE, "");
        GeographyData geojson = new GeographyData(geojson_type, geometry, properties);

        SidebarItem sidebarItem = new SidebarItem(id, categoryId, token, createdAt, updatedAt, startAt, endAt, isFree, title, description, body, phone, geojson, main_image, cover_image, coverBgColor, url, qrcode, type);
        sidebarItem.setTicket( unmarshallTickets(marhsalledTickets) );
        sidebarItem.setProperties(unmarshallProperties(marhsalledProperties));

        return sidebarItem;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public void setDateCreated(String createdAt) { this.createdAt = createdAt; }
    public String getDateCreated() { return createdAt; }
    public Date getDateCreatedAsDate(){
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if (TextUtils.isEmpty(createdAt))
                return null;

            return DateUtils.parseISO8601Date(createdAt);
            //return formatter.parse(createdAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public void setDateUpdate(String updatedAt) { this.updatedAt = updatedAt; }
    public String getDateUpdate() { return updatedAt; }
    public Date getDateUpdateAsDate(){
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if (TextUtils.isEmpty(updatedAt))
                return null;

            return DateUtils.parseISO8601Date(updatedAt);
            //return formatter.parse(updatedAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public void setDateStartEvent(String startAt) { this.startAt = startAt; }
    public String getDateStartEvent() { return startAt; }
    public Date getDateStartEventAsDate(){
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if (TextUtils.isEmpty(startAt))
                return null;

            return DateUtils.parseISO8601Date(startAt);
            //return formatter.parse(updatedAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public void setDateEndEvent(String startAt) { this.endAt = endAt; }
    public String getDateEndEvent() { return endAt; }
    public Date getDateEndEventAsDate(){
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if (TextUtils.isEmpty(endAt))
                return null;

            return DateUtils.parseISO8601Date(endAt);
            //return formatter.parse(endAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<SidebarProperties> getProperties() { return properties; }
    public void setProperties(List<SidebarProperties> properties) { this.properties = properties; }
    public String getFlatProperties() {
        if(properties == null)
            return "";

        Gson gson = new Gson();
        Type type = new TypeToken<List<SidebarProperties>>() {}.getType();
        return gson.toJson(properties, type);
    }
    private static List<SidebarProperties> unmarshallProperties(String marshalledValue) {
        if(marshalledValue == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<SidebarProperties>>() {}.getType();
        return gson.fromJson(marshalledValue, type);
    }

    public GeographyData getGeoJson() { return geojson; }
    public void setGeoJson(GeographyData geojson) { this.geojson = geojson; }

    public String getMainImage() { return this.mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public String getCoverBgColor() { return coverBgColor; }
    public void setCoverBgColor(String coverBgColor) { this.coverBgColor = coverBgColor; }

    public String getQrcode() { return qrcode; }
    public void setQrcode(String qrcode) { this.qrcode = qrcode; }

    public String getUrl() { return url; }
    public void setUrl(String _url) { this.url = _url; }

    public boolean getIsFree() { return isFree; }
    public void setIsFree(boolean isFree) { this.isFree = isFree; }

    public List<SidebarTicket> getTicket() { return ticket; }
    public void setTicket(List<SidebarTicket> ticket) { this.ticket = ticket; }

    public String getFlatTickets() {
        if(ticket == null)
            return "";

        Gson gson = new Gson();
        Type type = new TypeToken<List<SidebarTicket>>() {}.getType();
        return gson.toJson(ticket, type);
    }
    private static List<SidebarTicket> unmarshallTickets(String marshalledValue) {
        if(marshalledValue == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<SidebarTicket>>() {}.getType();
        return gson.fromJson(marshalledValue, type);
    }
}