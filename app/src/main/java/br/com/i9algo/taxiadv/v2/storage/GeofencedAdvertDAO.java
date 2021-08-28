package br.com.i9algo.taxiadv.v2.storage;

import android.database.Cursor;
import android.util.Log;

import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;

import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeofencedAdvert;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeofencedAdvertArraylistWrapper;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeographyData;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarItem;
import rx.Observable;
import rx.functions.Func1;

public class GeofencedAdvertDAO {

    private final SqlBrite sqlBrite;

    String getGeofencedAdverts = "Select " + GeofencedAdvert.TABLE + ".* FROM " + GeofencedAdvert.TABLE;

    String getGeofencedAdvertById = getGeofencedAdverts + " WHERE " + GeofencedAdvert.TABLE + "." + GeofencedAdvert.ID + " = ?";

    public GeofencedAdvertDAO(SqlBrite sqlBrite) {
        this.sqlBrite = sqlBrite;
    }

    public void insert(GeofencedAdvert geofencedAdvert) {
        sqlBrite.beginTransaction();
        try {
            sqlBrite.insert(GeofencedAdvert.TABLE, geofencedAdvert.toContentValues());
            sqlBrite.setTransactionSuccessful();
        } finally {
            sqlBrite.endTransaction();
        }
    }


    public void insertGeofencedAdvertList(GeofencedAdvertArraylistWrapper geofencedAdverts) {
        sqlBrite.beginTransaction();
        try {
            for (GeofencedAdvert geofencedAdvert : geofencedAdverts.getData().getGeofencedAdverts()) {
                sqlBrite.insert(GeofencedAdvert.TABLE, geofencedAdvert.toContentValues());
            }
            sqlBrite.setTransactionSuccessful();
        } finally {
            sqlBrite.endTransaction();
        }
    }

    public Observable<GeofencedAdvert> getGeofencedAdvert(final int geofencedAdvertId) {
        Log.e("Adv", "GeofencedadvertDAO: getGeofencedAdvert");
        return sqlBrite.createQuery(GeofencedAdvert.TABLE, getGeofencedAdvertById, Integer.toString(geofencedAdvertId))
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Log.e("Adv", "GeofencedadvertDAO: call");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, GeofencedAdvert>() {
                    @Override
                    public GeofencedAdvert call(Cursor cursor) {
                        Log.e("Adv", "GeofencedadvertDAO: fromCursor");

                        if (cursor.moveToNext()){
                            return GeofencedAdvert.fromCursor(cursor);
                        }
                        return new GeofencedAdvert(0, "", "", new GeographyData("", null, null));
                    }
                });
    }

    public void delete(int geofencedAdvertId) {
        sqlBrite.delete(GeofencedAdvert.TABLE, GeofencedAdvert.TABLE + ".id = ?", Integer.toString(geofencedAdvertId));
    }

    public void update(GeofencedAdvert geofencedAdvert) {
        sqlBrite.update(GeofencedAdvert.TABLE, geofencedAdvert.toContentValues(), "id = ?", String.valueOf(geofencedAdvert.getId()));
    }

    public Observable<ArrayList<GeofencedAdvert>> getAllGeofencedAdverts() {
        Log.e("adv", "starting get all GeofencedAdverts");
        return sqlBrite.createQuery(GeofencedAdvert.TABLE, getGeofencedAdverts)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Log.e("adv", "running get all categories query");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, ArrayList<GeofencedAdvert>>() {
                    @Override
                    public ArrayList<GeofencedAdvert> call(Cursor cursor) {
                        ArrayList<GeofencedAdvert> arrayList = new ArrayList<>();
                        Log.e("adv", "getting categories");
                        while (cursor.moveToNext()) {
                            arrayList.add(GeofencedAdvert.fromCursor(cursor));
                        }
                        return arrayList;
                    }
                });
    }


    public void cleanSidebars() {
        sqlBrite.delete(SidebarItem.TABLE, null);
    }

    public void deleteDemo(int id) {
        //sqlBrite.delete(GeofencedAdvert.TABLE, GeofencedAdvert.TABLE + "." + GeofencedAdvert.DEMO_ID + " = ?", Integer.toString(id));
    }
}
