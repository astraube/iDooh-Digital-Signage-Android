package br.com.i9algo.taxiadv.v2.storage;

import android.database.Cursor;
import android.util.Log;

import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import br.com.i9algo.taxiadv.v2.helpers.DateFormatHelper;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarItem;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarItemList;
import br.com.i9algo.taxiadv.v2.models.viewmodel.GridItemViewModel;
import br.com.i9algo.taxiadv.v2.utils.Pref;
import rx.Observable;
import rx.functions.Func1;

public class SidebarDAO {

    private final SqlBrite sqlBrite;

    String getSidebarWithInnerObjects = "Select" + " * FROM " + SidebarItem.TABLE;

    String getGetSidebarWithInnerObjectsById = getSidebarWithInnerObjects + " WHERE " + SidebarItem.ID + " = ?";

    String getGetSidebarWithInnerObjectsByCategoryId = getSidebarWithInnerObjects + " WHERE " + SidebarItem.TABLE + "." + SidebarItem.CATEGORY_ID + " = ?";

    String getAllGridviewItemModelsByCategoryId = "Select " + SidebarItem.TABLE + "." + SidebarItem.ID + ", "
            + SidebarItem.TABLE + "." + SidebarItem.COVER_IMAGE + ", "
            + SidebarItem.TABLE + "." + SidebarItem.TITLE + ", "
            + SidebarItem.TABLE + "." + SidebarItem.URL + ", "
            + SidebarItem.TABLE + "." + SidebarItem.TYPE + ", "
            + SidebarItem.TABLE + "." + SidebarItem.DATE_START_EVENT
            + " from " + SidebarItem.TABLE
            + " where " + SidebarItem.TABLE + "." + SidebarItem.CATEGORY_ID + " = ?";

    String getAllGridviewItemModelsByCategoryIdByDateQuery = getAllGridviewItemModelsByCategoryId
            + " and ( "
            + " (" + SidebarItem.DATE_START_EVENT + " > datetime('now') or " + SidebarItem.DATE_END_EVENT + " > datetime('now')) "
            + " or "
            + " (start_at = '' and end_at = '') "
            + " )";
    ;

    public SidebarDAO(SqlBrite sqlBrite) {
        this.sqlBrite = sqlBrite;
    }

    public void insert(SidebarItem sidebar) {
        sqlBrite.beginTransaction();
        try {
            sqlBrite.insert(SidebarItem.TABLE, sidebar.toContentValues());
            sqlBrite.setTransactionSuccessful();
        } finally {
            sqlBrite.endTransaction();
        }
    }


    public void insertSidebarItemList(SidebarItemList sidebars) {
        sqlBrite.beginTransaction();
        try {
            if (sidebars != null && sidebars.getData()!= null && !sidebars.getData().getSidebars().isEmpty()) {
                for (SidebarItem sidebar : sidebars.getData().getSidebars()) {
                    sqlBrite.insert(SidebarItem.TABLE, sidebar.toContentValues());
                }
            }
            sqlBrite.setTransactionSuccessful();
        } finally {
            sqlBrite.endTransaction();
        }
    }

    public Observable<SidebarItem> getSidebarById(final int sidebarID) {
        Log.e("Adv", "sidebarDao: getSidebarById");
        System.out.println(getGetSidebarWithInnerObjectsById);
        return sqlBrite.createQuery(SidebarItem.TABLE, getGetSidebarWithInnerObjectsById, Integer.toString(sidebarID))
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Log.e("Adv", "sidebarDao: call");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, SidebarItem>() {
                    @Override
                    public SidebarItem call(Cursor cursor) {
                        Log.e("Adv", "sidebarDao: parseSidebarItem");
                        return parseSidebarItem(cursor);
                    }
                });
    }

    private SidebarItem parseSidebarItem(Cursor cursor) {
        Log.e("Adv", "sidebarDao: parseSidebarItem:parsing");
        SidebarItem sidebarItem = null;

        while (cursor.moveToNext()) {
            if (sidebarItem == null) {
                sidebarItem = SidebarItem.fromCursor(cursor);
            }
        }
        Log.e("Adv", "sidebarDao: parseSidebarItem:assembling");
        return sidebarItem;
    }

    public void delete(int categoryId) {
        sqlBrite.delete(SidebarItem.TABLE, SidebarItem.TABLE + ".id = ?", Integer.toString(categoryId));
    }

    public void update(SidebarItem category) {
        sqlBrite.update(SidebarItem.TABLE, category.toContentValues(), "id = ?", String.valueOf(category.getId()));
    }

    public Observable<ArrayList<SidebarItem>> getAllSidebarsByCategoryId(final int categoryId) {
        Log.e("adv", "starting get all categories");
        return sqlBrite.createQuery(SidebarItem.TABLE, getGetSidebarWithInnerObjectsByCategoryId)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Log.e("adv", "running get all categories query");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, ArrayList<SidebarItem>>() {
                    @Override
                    public ArrayList<SidebarItem> call(Cursor cursor) {
                        ArrayList<SidebarItem> arrayList = new ArrayList<>();
                        Log.e("adv", "getting categories");
                        while (cursor.moveToNext()) {
                            arrayList.add(SidebarItem.fromCursor(cursor));
                        }
                        return arrayList;
                    }
                });
    }

    public Observable<List<GridItemViewModel>> getGridItemListByCategory(int id) {
        Log.e("adv", "starting get all categories");
        //return sqlBrite.createQuery(SidebarItem.TABLE, getAllGridviewItemModelsByCategoryId, Integer.toString(id))
        //Log.e("adv", "@@@@@ - " + getAllGridviewItemModelsByCategoryIdByDateQuery);

        return sqlBrite.createQuery(SidebarItem.TABLE, getAllGridviewItemModelsByCategoryIdByDateQuery, Integer.toString(id))
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Log.e("adv", "running get all categories query");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, List<GridItemViewModel>>() {
                    @Override
                    public List<GridItemViewModel> call(Cursor cursor) {
                        List<GridItemViewModel> arrayList = new ArrayList<>();
                        Log.e("adv", "getting categories");
                        while (cursor.moveToNext()) {
                            arrayList.add(GridItemViewModel.fromCursor(cursor));
                        }
                        return arrayList;
                    }
                });
    }

    public void cleanSidebars() {
        sqlBrite.delete(SidebarItem.TABLE, null);
    }

    public void deleteDemo(int id) {
        //sqlBrite.delete(SidebarItem.TABLE, SidebarItem.TABLE + "." + Pref.IS_DEMO_CONTENT_ID + " = ?", Integer.toString(id));
    }
}
