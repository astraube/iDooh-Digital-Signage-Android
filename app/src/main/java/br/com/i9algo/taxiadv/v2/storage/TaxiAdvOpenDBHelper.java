package br.com.i9algo.taxiadv.v2.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import br.com.i9algo.taxiadv.BuildConfig;
import br.com.i9algo.taxiadv.v2.download.DownloadItem;
import br.com.i9algo.taxiadv.v2.models.inbound.Category;
import br.com.i9algo.taxiadv.v2.models.inbound.SlideshowFillerListWrapper;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeofencedAdvert;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeographyData;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeolocalisationData;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.Geometry;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarItem;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowPlaylist;

public class TaxiAdvOpenDBHelper extends SQLiteOpenHelper {

    //private static final int VERSION = 10;
    private static final int VERSION = BuildConfig.VERSION_CODE;

    private static final String CREATE_CATEGORY_TABLE = ""
            + "CREATE TABLE IF NOT EXISTS " + Category.TABLE + "("
            + Category.ID                       + " INTEGER NOT NULL PRIMARY KEY ON CONFLICT REPLACE, "
            + Category.NAME                     + " TEXT NOT NULL DEFAULT '', "
            + Category.COVER_IMAGE              + " TEXT DEFAULT '" + Category.DEFAULT_COVER + "', "
            + Category.DESCRIPTION              + " TEXT NOT NULL DEFAULT '', "
            + Category.BGCOLOR                  + " TEXT DEFAULT '#FFFFFF', "
            + Category.ICON_URL                 + " TEXT NOT NULL DEFAULT '', "
            + Category.CREATED_AT              + " TEXT DEFAULT '', "
            + Category.UPDATED_AT              + " TEXT DEFAULT '', "
            + Category.GRID_COLUMNS             + " INTEGER DEFAULT 3 "
            + ")";


    private static final String CREATE_SLIDESHOWITEM_TABLE = ""
            + "CREATE TABLE IF NOT EXISTS "     + SlideshowItem.TABLE + "("
            + SlideshowItem.ID                  + " INTEGER NOT NULL PRIMARY KEY ON CONFLICT REPLACE, "
            + SlideshowItem.TOKEN               + " TEXT NOT NULL DEFAULT '', "
            + SlideshowItem.PLAYLIST_ID         + " INTEGER NOT NULL DEFAULT 0, "
            + SlideshowItem.TYPE                + " TEXT DEFAULT 'DEFAULT', "
            + SlideshowItem.CREATED_AT          + " TEXT DEFAULT '', "
            + SlideshowItem.UPDATED_AT          + " TEXT DEFAULT '', "
            + SlideshowItem.CAMPAIGN_ID         + " INTEGER DEFAULT 0, "
            + SlideshowItem.EXIBITION_TIME      + " INTEGER DEFAULT 0, "
            + SlideshowItem.TITLE               + " TEXT DEFAULT '', "
            + SlideshowItem.SUMMARY             + " TEXT DEFAULT '', "
            + SlideshowItem.MAIN_IMAGE          + " TEXT DEFAULT '', "
            + SlideshowItem.MAIN_IMAGE_SOURCE   + " TEXT DEFAULT '', "
            + SlideshowItem.NEWS_SOURCE_NAME    + " TEXT DEFAULT '', "
            + SlideshowItem.NEWS_SOURCE_IMAGE   + " TEXT DEFAULT '', "
            + SlideshowItem.ORDER_SLIDE         + " INTEGER DEFAULT 0, "
            + SlideshowItem.ACTION_MODEL        + " TEXT DEFAULT '', "
            + SlideshowItem.ACTION_MODEL_ID     + " INTEGER DEFAULT 0, "
            + SlideshowItem.URL_CONTENT         + " TEXT DEFAULT '' "
            + ")";

    private static final String CREATE_FILLER_TABLE = ""
            + "CREATE TABLE IF NOT EXISTS " + SlideshowFillerListWrapper.TABLE + "("
            + SlideshowItem.ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE, "
            + SlideshowItem.TOKEN               + " TEXT NOT NULL DEFAULT '', "
            + SlideshowItem.PLAYLIST_ID         + " INTEGER DEFAULT 0, "
            + SlideshowItem.TYPE                + " TEXT DEFAULT 'NEWS', "
            + SlideshowItem.CREATED_AT          + " TEXT DEFAULT '', "
            + SlideshowItem.UPDATED_AT          + " TEXT DEFAULT '', "
            + SlideshowItem.CAMPAIGN_ID         + " INTEGER DEFAULT 0, "
            + SlideshowItem.EXIBITION_TIME      + " INTEGER DEFAULT 0, "
            + SlideshowItem.TITLE               + " TEXT DEFAULT '', "
            + SlideshowItem.SUMMARY             + " TEXT DEFAULT '', "
            + SlideshowItem.MAIN_IMAGE          + " TEXT DEFAULT '', "
            + SlideshowItem.MAIN_IMAGE_SOURCE   + " TEXT DEFAULT '', "
            + SlideshowItem.NEWS_SOURCE_NAME    + " TEXT DEFAULT '', "
            + SlideshowItem.NEWS_SOURCE_IMAGE   + " TEXT DEFAULT '', "
            + SlideshowItem.ORDER_SLIDE         + " INTEGER DEFAULT 0, "
            + SlideshowItem.ACTION_MODEL        + " TEXT DEFAULT '', "
            + SlideshowItem.ACTION_MODEL_ID     + " INTEGER DEFAULT 0,  "
            + SlideshowItem.URL_CONTENT         + " TEXT DEFAULT '' "
            + ")";

    private static final String CREATE_PLAYLIST_TABLE = ""
            + "CREATE TABLE IF NOT EXISTS " + SlideshowPlaylist.TABLE + "("
            + SlideshowPlaylist.ID + " INTEGER NOT NULL PRIMARY KEY ON CONFLICT REPLACE,"
            + SlideshowPlaylist.NAME + " TEXT DEFAULT '', "
            + SlideshowPlaylist.CREATED_AT + " TEXT DEFAULT '', "
            + SlideshowPlaylist.UPDATED_AT + " TEXT NOT NULL DEFAULT '', "
            + SlideshowPlaylist.START_AT + " TEXT NOT NULL DEFAULT '', "
            + SlideshowPlaylist.EXPIRES_AT + " TEXT NOT NULL DEFAULT '', "
            + SlideshowPlaylist.START_TIME + " INTEGER NOT NULL DEFAULT 0, "
            + SlideshowPlaylist.END_TIME + " INTEGER NOT NULL DEFAULT 0 "
            + ")";

    private static final String CREATE_GEOFENCEADVERT_TABLE = ""
            + "CREATE TABLE IF NOT EXISTS " + GeofencedAdvert.TABLE + "("
            + GeofencedAdvert.ID + " INTEGER NOT NULL PRIMARY KEY ON CONFLICT REPLACE,"
            + GeofencedAdvert.URL + " TEXT NOT NULL DEFAULT '', "
            + GeofencedAdvert.NAME + " TEXT NOT NULL DEFAULT '', "
            + GeographyData.GEOJSON_TYPE + " TEXT NOT NULL DEFAULT '',"
            + Geometry.GEOJSON_GEOMETRY_TYPE + " TEXT NOT NULL DEFAULT '',"
            + Geometry.GEOJSON_GEOMETRY_LATITUDE + " FLOAT NOT NULL DEFAULT '',"
            + Geometry.GEOJSON_GEOMETRY_LONGITUDE + " FLOAT NOT NULL DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_COLOR + " TEXT NOT NULL DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_SIZE + " TEXT NOT NULL DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_SYMBOL + " TEXT NOT NULL DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_NAME + " TEXT NOT NULL DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_ADDRESS + " TEXT NOT NULL DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_CITY + " TEXT NOT NULL DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_STATE + " TEXT NOT NULL DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_UF + " TEXT NOT NULL DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_ZIPCODE + " TEXT NOT NULL DEFAULT '', "
            + GeolocalisationData.GEOJSON_PROPERTIES_COUNTRY + " TEXT NOT NULL DEFAULT '' "
            + ")";

    private static final String CREATE_SIDEBAR_TABLE = ""
            + "CREATE TABLE IF NOT EXISTS " + SidebarItem.TABLE + "("
            + SidebarItem.ID + " INTEGER NOT NULL PRIMARY KEY ON CONFLICT REPLACE,"
            + SidebarItem.CATEGORY_ID + " INTEGER NOT NULL DEFAULT 0, "
            + SidebarItem.TOKEN + " TEXT NOT NULL DEFAULT '', "
            + SidebarItem.DATE_CREATED + " TEXT NOT NULL DEFAULT '', "
            + SidebarItem.DATE_UPDATED + " TEXT NOT NULL DEFAULT '',"
            + SidebarItem.DATE_START_EVENT + " TEXT DEFAULT '',"
            + SidebarItem.DATE_END_EVENT + " TEXT DEFAULT '',"
            + SidebarItem.TITLE + " TEXT NOT NULL DEFAULT '',"
            + SidebarItem.DESCRIPTION + " TEXT DEFAULT '',"
            + SidebarItem.BODY + " TEXT DEFAULT '',"
            + SidebarItem.MAIN_IMAGE + " TEXT DEFAULT '', "
            + SidebarItem.COVER_IMAGE + " TEXT DEFAULT '',"
            + SidebarItem.COVER_BG_COLOR + " TEXT DEFAULT '',"
            + SidebarItem.QRCODE + " TEXT DEFAULT '',"
            + SidebarItem.URL + " TEXT DEFAULT '',"
            + SidebarItem.TYPE + " TEXT DEFAULT '',"
            + SidebarItem.IS_FREE + " INTEGER DEFAULT 1,"
            + SidebarItem.TICKET + " TEXT DEFAULT '', "
            + SidebarItem.PROPERTIES + " TEXT DEFAULT '', "
            + SidebarItem.PHONE + " TEXT DEFAULT '',"
            + GeographyData.GEOJSON_TYPE + " TEXT DEFAULT '',"
            + Geometry.GEOJSON_GEOMETRY_TYPE + " TEXT DEFAULT '',"
            + Geometry.GEOJSON_GEOMETRY_LATITUDE + " FLOAT DEFAULT '',"
            + Geometry.GEOJSON_GEOMETRY_LONGITUDE + " FLOAT DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_COLOR + " TEXT DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_SIZE + " TEXT DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_DATA_MARKER_SYMBOL + " TEXT DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_NAME + " TEXT DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_ADDRESS + " TEXT DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_CITY + " TEXT DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_STATE + " TEXT DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_UF + " TEXT DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_ZIPCODE + " TEXT DEFAULT '',"
            + GeolocalisationData.GEOJSON_PROPERTIES_COUNTRY + " TEXT DEFAULT '' "
            + ")";

    private static final String CREATE_DOWNLOAD_TASK_TABLE = ""
            + "CREATE TABLE IF NOT EXISTS " + DownloadItem.TABLE + "("
            + DownloadItem.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ON CONFLICT REPLACE ,"
            + DownloadItem.SLIDE_ID + " TEXT NOT NULL ON CONFLICT REPLACE, "
            + DownloadItem.ORIGINAL_URL + " TEXT DEFAULT '', "
            + DownloadItem.TASK_ID + " TEXT DEFAULT '', "
            + DownloadItem.FILE_LOCATION + " TEXT NOT NULL DEFAULT '', "
            + DownloadItem.STATUS + " TEXT NOT NULL DEFAULT '' "
            + ")";

    private static SQLiteDatabase _db;

    public TaxiAdvOpenDBHelper(Context context) {
        super(context, "taxiadv.db", null, VERSION);
        _db = getWritableDatabase(); // Faz chamar o onCreate
    }

    public boolean hasTableExist(SQLiteDatabase db, String tableName) {
        try {
            Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.close();
                    return true;
                }
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_SLIDESHOWITEM_TABLE);
        db.execSQL(CREATE_PLAYLIST_TABLE);
        db.execSQL(CREATE_SIDEBAR_TABLE);
        db.execSQL(CREATE_GEOFENCEADVERT_TABLE);
        db.execSQL(CREATE_FILLER_TABLE);
        db.execSQL(CREATE_DOWNLOAD_TASK_TABLE);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        if (!BuildConfig.DEBUG) {
            db.execSQL("DROP TABLE IF EXISTS " + Category.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SlideshowItem.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SlideshowPlaylist.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SidebarItem.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + GeofencedAdvert.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SlideshowFillerListWrapper.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DownloadItem.TABLE);

            onCreate(db);
        }
    }

    public static void deleteAllDatabases(boolean reCreate) {
        _db.execSQL("DROP TABLE IF EXISTS " + Category.TABLE);
        _db.execSQL("DROP TABLE IF EXISTS " + SlideshowItem.TABLE);
        _db.execSQL("DROP TABLE IF EXISTS " + SlideshowPlaylist.TABLE);
        _db.execSQL("DROP TABLE IF EXISTS " + SidebarItem.TABLE);
        _db.execSQL("DROP TABLE IF EXISTS " + GeofencedAdvert.TABLE);
        _db.execSQL("DROP TABLE IF EXISTS " + SlideshowFillerListWrapper.TABLE);
        _db.execSQL("DROP TABLE IF EXISTS " + DownloadItem.TABLE);

        if (reCreate) {
            _db.execSQL(CREATE_CATEGORY_TABLE);
            _db.execSQL(CREATE_SLIDESHOWITEM_TABLE);
            _db.execSQL(CREATE_PLAYLIST_TABLE);
            _db.execSQL(CREATE_SIDEBAR_TABLE);
            _db.execSQL(CREATE_GEOFENCEADVERT_TABLE);
            _db.execSQL(CREATE_FILLER_TABLE);
            _db.execSQL(CREATE_DOWNLOAD_TASK_TABLE);
        }
    }
}