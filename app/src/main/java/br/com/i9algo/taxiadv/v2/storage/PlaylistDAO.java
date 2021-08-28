package br.com.i9algo.taxiadv.v2.storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.squareup.sqlbrite.SqlBrite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.v2.helpers.DateFormatHelper;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowPlaylist;
import br.com.i9algo.taxiadv.v2.utils.Pref;
import rx.Observable;
import rx.functions.Func1;

public class PlaylistDAO {

    private static final String LOG_TAG = "PlaylistDAO";

    private final SqlBrite sqlBrite;
    private final SlideDAO slideDAO;

    String getCompletePlaylist = "SELECT " + SlideshowPlaylist.TABLE + ".*, "
            + SlideshowItem.TABLE + "." + SlideshowItem.ID + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.ID + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.TOKEN + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.TOKEN + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.PLAYLIST_ID + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.PLAYLIST_ID + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.TYPE + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.TYPE  + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.CREATED_AT + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.CREATED_AT + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.UPDATED_AT + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.UPDATED_AT + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.CAMPAIGN_ID + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.CAMPAIGN_ID + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.EXIBITION_TIME + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.EXIBITION_TIME + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.TITLE + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.TITLE + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.SUMMARY + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.SUMMARY + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.MAIN_IMAGE + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.MAIN_IMAGE + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.MAIN_IMAGE_SOURCE + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.MAIN_IMAGE_SOURCE + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.NEWS_SOURCE_NAME + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.NEWS_SOURCE_NAME + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.NEWS_SOURCE_IMAGE + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.NEWS_SOURCE_IMAGE + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.ORDER_SLIDE + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.ORDER_SLIDE + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.ACTION_MODEL + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.ACTION_MODEL + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.ACTION_MODEL_ID + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.ACTION_MODEL_ID + ", "
            + SlideshowItem.TABLE + "." + SlideshowItem.URL_CONTENT + " as " + SlideshowItem.TABLE + "_" + SlideshowItem.URL_CONTENT
            + " FROM " + SlideshowPlaylist.TABLE
            + " INNER JOIN " + SlideshowItem.TABLE
            + " ON " + SlideshowPlaylist.TABLE + "." + SlideshowPlaylist.ID + " = " + SlideshowItem.TABLE + "." + SlideshowItem.PLAYLIST_ID;

    String getbyPlaylistIdQuery = getCompletePlaylist +
            " where " + SlideshowPlaylist.TABLE + "." + SlideshowPlaylist.ID + " = ?";

    String getDemoSlides = "SELECT * from " + SlideshowItem.TABLE + " where " + SlideshowItem.PLAYLIST_ID + " = 0 ";

    String getPlaylistByDateQuery = getCompletePlaylist
            + " where (" + SlideshowPlaylist.TABLE + "." + SlideshowPlaylist.START_TIME + " BETWEEN  ?"
            + " and ?)"
            + " and (" + SlideshowPlaylist.START_TIME + " => ?)"
            + " and (" + SlideshowPlaylist.END_TIME + " >= ? )";
    ;

    String getPlaylistByTime = getCompletePlaylist +
            " where " + SlideshowPlaylist.TABLE + "." + SlideshowPlaylist.START_TIME + " <= ?"
            + " and (" + SlideshowPlaylist.TABLE + "." + SlideshowPlaylist.EXPIRES_AT + " >= ?)"
            + " and (" + SlideshowPlaylist.START_TIME + " <= ?)"
            + " and (" + SlideshowPlaylist.END_TIME + " >=? )";

    String getPlaylistByTimeNullable = getCompletePlaylist +
            " where " + SlideshowPlaylist.TABLE + "." + SlideshowPlaylist.START_TIME + " <= ?"
            + " and ("
            + SlideshowPlaylist.TABLE + "." + SlideshowPlaylist.EXPIRES_AT + " >= ? or " + SlideshowPlaylist.EXPIRES_AT + " = '')"
            + " and ("
            + SlideshowPlaylist.START_TIME + " <= ?)"
            + " and (" + SlideshowPlaylist.END_TIME + " >=? )";

    String getPlaylistByTimeWithDefaultPlaylist = getPlaylistByTime
            + " UNION SELECT * FROM (" + getPlaylistByTimeNullable
            + ") WHERE NOT EXISTS ("
            + getPlaylistByTime
            + ")"
            + "order by " + SlideshowItem.TABLE + "_" + SlideshowItem.ORDER_SLIDE;

    public PlaylistDAO(SqlBrite sqlBrite, SlideDAO slideDAO) {
        this.sqlBrite = sqlBrite;
        this.slideDAO = slideDAO;
    }

    public void insert(SlideshowPlaylist playlist) {
        sqlBrite.beginTransaction();
        try {
            sqlBrite.insert(SlideshowPlaylist.TABLE, playlist.toContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
            //slideDAO.deleteByPlaylistId(playlist.getId());
            slideDAO.insertSlideArray(playlist.getItems(), playlist.getId());
            sqlBrite.setTransactionSuccessful();
        } finally {
            sqlBrite.endTransaction();
        }
    }

    public void insertPlaylistArray(List<SlideshowPlaylist> playlists) {
        Logger.e(LOG_TAG, "insertPlaylistArray");
        sqlBrite.beginTransaction();
        try {
            if (playlists != null && !playlists.isEmpty()) {
                Logger.v(LOG_TAG, "insertPlaylistArray - entrou aqui...");

                for (SlideshowPlaylist playlist : playlists) {
                    Logger.v(LOG_TAG, "new playlist - dateUpdated: " + playlist.getUpdatedAt());

                    //Observable<SlideshowPlaylist> localPl = getPlaylistById(playlist.getId());
                    //Logger.e(LOG_TAG, "local playlist - dateUpdated: " + playlist.getDateUpdated());

                    insert(playlist);
                }
            }
            sqlBrite.setTransactionSuccessful();
        } finally {
            sqlBrite.endTransaction();
        }
    }

    public Observable<SlideshowPlaylist> getPlaylistById(final int playlistId) {
        return sqlBrite.createQuery(SlideshowPlaylist.TABLE, getbyPlaylistIdQuery, Integer.toString(playlistId))
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, SlideshowPlaylist>() {
                    @Override
                    public SlideshowPlaylist call(Cursor cursor) {
                        return SlideshowPlaylist.fromCursor(cursor);
                    }
                });
    }

    public Observable<SlideshowPlaylist> getDemoPlaylist() {
        return sqlBrite.createQuery(SlideshowPlaylist.TABLE, getDemoSlides)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, SlideshowPlaylist>() {
                    @Override
                    public SlideshowPlaylist call(Cursor cursor) {
                        List<SlideshowItem> demoSlides = new ArrayList<SlideshowItem>();
                        while (cursor.moveToNext()) {
                            demoSlides.add(SlideshowItem.fromCursor(cursor));
                        }
                        SlideshowPlaylist demoPlaylist = new SlideshowPlaylist(
                                demoSlides, 0, "",
                                DateFormatHelper.getTodaysDateAsString(),
                                DateFormatHelper.getTodaysDateAsString(),
                                DateFormatHelper.getTodaysDateAsString(),
                                DateFormatHelper.getTodaysDateAsString(), "0", "0");
                        return demoPlaylist;
                    }
                });
    }

    public void delete(int id) {
        sqlBrite.delete(SlideshowPlaylist.TABLE, SlideshowPlaylist.TABLE + ".id = ?", Integer.toString(id));
    }

    public void update(SlideshowPlaylist playlist) {
        sqlBrite.update(SlideshowPlaylist.TABLE, playlist.toContentValues(), "id = ?", String.valueOf(playlist.getId()));
    }

    public Observable<List<SlideshowPlaylist>> getAllPlaylists() {
        Logger.i(LOG_TAG, "getAllPlaylists - " + getCompletePlaylist);
        return sqlBrite.createQuery(SlideshowPlaylist.TABLE, getCompletePlaylist)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Logger.i(LOG_TAG, "running get all Playlists query");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, List<SlideshowPlaylist>>() {
                    @Override
                    public List<SlideshowPlaylist> call(Cursor cursor) {
                        Logger.i(LOG_TAG, "getting Playlists");

                        List<SlideshowPlaylist> arrayList = new ArrayList<>();
                        while (cursor.moveToNext()) {
                            arrayList.add(SlideshowPlaylist.fromCursor(cursor));
                        }
                        return arrayList;
                    }
                });
    }

    public Observable<SlideshowPlaylist> getNextPlaylist() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormatHelper.DATE_FORMAT);
        Date now = Calendar.getInstance().getTime();
        String date = sdf.format(now);
        return sqlBrite.createQuery(SlideshowPlaylist.TABLE, getPlaylistByDateQuery, date, Long.toString(now.getTime()))
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, SlideshowPlaylist>() {
                    @Override
                    public SlideshowPlaylist call(Cursor cursor) {
                        return SlideshowPlaylist.fromCursor(cursor);
                    }
                });
    }

    public Observable<SlideshowPlaylist> getDefaultPlaylist(String date) {
        //Logger.i(LOG_TAG, "getDefaultPlaylist - " + getPlaylistByTime);

        return sqlBrite.createQuery(SlideshowPlaylist.TABLE, getPlaylistByTime, date, date, Integer.toString(0), Integer.toString(0))
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Logger.i(LOG_TAG, "playlistdao:getDefaultPlaylist:query");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, SlideshowPlaylist>() {
                    @Override
                    public SlideshowPlaylist call(Cursor cursor) {
                        Logger.i(LOG_TAG, "playlistdao:getDefaultPlaylist:fromCursor");
                        return PlaylistDAO.this.parse(cursor);
                    }
                });
    }

    public Observable<SlideshowPlaylist> getCurrentPlaylist(String date, int timeintervalstart, int timeintervalend) {
        //Logger.i(LOG_TAG, "getCurrentPlaylist - " + getPlaylistByTimeWithDefaultPlaylist);

        return sqlBrite.createQuery(SlideshowPlaylist.TABLE, getPlaylistByTimeWithDefaultPlaylist, date, date, Integer.toString(timeintervalstart), Integer.toString(timeintervalend), date, date, Integer.toString(0), Integer.toString(0), date, date, Integer.toString(timeintervalstart), Integer.toString(timeintervalend))
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        //Logger.i(LOG_TAG, "playlistdao:getCurrentPlaylist:query");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, SlideshowPlaylist>() {
                    @Override
                    public SlideshowPlaylist call(Cursor cursor) {
                        //Logger.i(LOG_TAG, "playlistdao:getCurrentPlaylist:fromCursor");

                        return PlaylistDAO.this.parse(cursor);
                    }
                });

    }


    private SlideshowPlaylist parse(Cursor cursor) {
        SlideshowPlaylist playlist = null;
        List<SlideshowItem> slideshowItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            if (playlist == null) {
                playlist = SlideshowPlaylist.fromCursor(cursor);
            }
            SlideshowItem slideshowItem = SlideshowItem.fromCursor(cursor, SlideshowItem.TABLE);
            if (!slideshowItems.contains(slideshowItem)) {
                slideshowItems.add(slideshowItem);
            }
        }

        if (playlist != null) {
            playlist.setItems(slideshowItems);
        }
        return playlist;
    }

    @Override
    public String toString() {
        return "PlaylistDAO{" +
                "slideDAO=" + slideDAO +
                ", sqlBrite=" + sqlBrite +
                '}';
    }

    public void cleanPlaylistsAndSlides() {
        Logger.v(LOG_TAG, "cleanPlaylistsAndSlides");
        sqlBrite.delete(SlideshowPlaylist.TABLE, null);
        sqlBrite.delete(SlideshowItem.TABLE, null);
    }

    public void removeErrorStateContent() {
        sqlBrite.delete(SlideshowItem.TABLE, SlideshowItem.ID + " = ?", "1");
        sqlBrite.delete(SlideshowPlaylist.TABLE, SlideshowPlaylist.ID + " = ?", "1");
    }
}
