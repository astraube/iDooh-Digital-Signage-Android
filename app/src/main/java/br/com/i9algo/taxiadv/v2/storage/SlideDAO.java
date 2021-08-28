package br.com.i9algo.taxiadv.v2.storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import br.com.i9algo.taxiadv.v2.utils.Pref;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

public class SlideDAO {

    private static final String LOG_TAG = "SlideDAO";

    private final SqlBrite sqlBrite;

    String getBySlideIdQuery = "SELECT * from " + SlideshowItem.TABLE + "." + "* where " + SlideshowItem.TABLE + "." + SlideshowItem.ID + " = ?";
    String getAllSlidesQuery = "SELECT * from " + SlideshowItem.TABLE;
    String getAllSlidesQueryByPlaylistId = "SELECT * from " + SlideshowItem.TABLE + " where " + SlideshowItem.PLAYLIST_ID + " = ?";

    private Subscription updateSubscription;

    public SlideDAO(SqlBrite sqlBrite) {
        this.sqlBrite = sqlBrite;
    }

    public void insert(SlideshowItem slide, int id) {
        sqlBrite.beginTransaction();
        try {
            slide.setPlaylistId(id);
            sqlBrite.insert(SlideshowItem.TABLE, slide.toContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
            sqlBrite.setTransactionSuccessful();
        } finally {
            sqlBrite.endTransaction();
        }
    }


    public void insertSlideArray(List<SlideshowItem> slides, int id) {
        sqlBrite.beginTransaction();
        try {
            for (SlideshowItem slide : slides) {
                slide.setPlaylistId(id);
                sqlBrite.insert(SlideshowItem.TABLE, slide.toContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
            }
            sqlBrite.setTransactionSuccessful();
        } finally {
            sqlBrite.endTransaction();
        }
    }

    public Observable<SlideshowItem> getSlideById(final int playlistId) {
        return sqlBrite.createQuery(SlideshowItem.TABLE, getBySlideIdQuery, Integer.toString(playlistId))
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, SlideshowItem>() {
                    @Override
                    public SlideshowItem call(Cursor cursor) {
                        return SlideshowItem.fromCursor(cursor);
                    }
                });
    }

    public void delete(int id) {
        sqlBrite.delete(SlideshowItem.TABLE, SlideshowItem.TABLE + ".id = ?", Integer.toString(id));
    }

    public void deleteByPlaylistId(int id) {
        sqlBrite.delete(SlideshowItem.TABLE, SlideshowItem.TABLE + "." +SlideshowItem.PLAYLIST_ID + " = ?", Integer.toString(id));
    }


    public void update(SlideshowItem item) {
        sqlBrite.update(SlideshowItem.TABLE, item.toContentValues(), "id = ?", String.valueOf(item.getId()));
    }

    public Observable<List<SlideshowItem>> getAllSlides() {
        Logger.i(LOG_TAG, "getAllSlides()");
        return sqlBrite.createQuery(SlideshowItem.TABLE, getAllSlidesQuery)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Logger.v(LOG_TAG, "running get all Slide query");
                        Logger.d(LOG_TAG, "running get all Slide query");
                        Logger.i(LOG_TAG, "running get all Slide query");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, List<SlideshowItem>>() {
                    @Override
                    public List<SlideshowItem> call(Cursor cursor) {
                        Logger.e(LOG_TAG, "getting Slides");

                        List<SlideshowItem> arrayList = new ArrayList<>();
                        while (cursor.moveToNext()) {
                            arrayList.add(SlideshowItem.fromCursor(cursor));
                        }
                        return arrayList;
                    }
                });
    }

    public Observable<List<SlideshowItem>> getAllSlidesByPlaylistId(int playlistId) {
        Logger.e(LOG_TAG, "starting get all slides");
        return sqlBrite.createQuery(SlideshowItem.TABLE, getBySlideIdQuery, Integer.toString(playlistId))
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Logger.e(LOG_TAG, "running get all slides query");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, List<SlideshowItem>>() {
                    @Override
                    public List<SlideshowItem> call(Cursor cursor) {
                        List<SlideshowItem> arrayList = new ArrayList<>();
                        Logger.e(LOG_TAG, "getting Playlists");
                        while (cursor.moveToNext()) {
                            arrayList.add(SlideshowItem.fromCursor(cursor));
                        }
                        return arrayList;
                    }
                });
    }

    public void deleteDemo(int id) {
        //sqlBrite.delete(SlideshowItem.TABLE, SlideshowItem.TABLE + "." + Pref.IS_DEMO_CONTENT_ID + " = ?", Integer.toString(id));
    }
}
