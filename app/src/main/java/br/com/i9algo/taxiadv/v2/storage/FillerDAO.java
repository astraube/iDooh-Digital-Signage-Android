package br.com.i9algo.taxiadv.v2.storage;

import android.database.Cursor;
import android.util.Log;

import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.models.inbound.SlideshowFillerListWrapper;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import rx.Observable;
import rx.functions.Func1;

public class FillerDAO {

    private static final String LOG_TAG = "FillerDAO";

    private final SqlBrite sqlBrite;
    private String getAllFillersQuery = "Select * from " + SlideshowFillerListWrapper.TABLE;


    public FillerDAO(SqlBrite sqlBrite) {
        this.sqlBrite = sqlBrite;
    }

    public void insert(SlideshowFillerListWrapper fillers) {
        sqlBrite.beginTransaction();
        try {
            if (fillers != null && fillers.getData() != null && fillers.getData().getFillerList() != null) {
                for (SlideshowItem item : fillers.getData().getFillerList()) {
                    sqlBrite.insert(SlideshowFillerListWrapper.TABLE, item.toContentValues());
                }
                sqlBrite.setTransactionSuccessful();
            }
        } finally {
            sqlBrite.endTransaction();
        }
    }

    public void delete(int id) {
        sqlBrite.delete(SlideshowFillerListWrapper.TABLE, SlideshowFillerListWrapper.TABLE + ".id = ?", Integer.toString(id));
    }

    public void update(SlideshowItem item) {
        sqlBrite.update(SlideshowFillerListWrapper.TABLE, item.toContentValues(), "id = ?", String.valueOf(item.getId()));
    }

    public Observable<List<SlideshowItem>> getFillers() {
        Logger.i(LOG_TAG, "FillerDAO.getFillers()");
        return sqlBrite.createQuery(SlideshowFillerListWrapper.TABLE, getAllFillersQuery)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Logger.i(LOG_TAG, "running get all Fillers query");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, List<SlideshowItem>>() {
                    @Override
                    public List<SlideshowItem> call(Cursor cursor) {
                        Logger.i(LOG_TAG, "getting Fillers");

                        return FillerDAO.this.parse(cursor);
                    }
                });
    }

    private List<SlideshowItem> parse(Cursor cursor) {
        List<SlideshowItem> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            arrayList.add(SlideshowItem.fromCursor(cursor));
        }
        return arrayList;
    }

    public void cleanFillers() {
        sqlBrite.delete(SlideshowFillerListWrapper.TABLE, null);
    }
}
