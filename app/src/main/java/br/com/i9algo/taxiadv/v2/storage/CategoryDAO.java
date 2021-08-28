package br.com.i9algo.taxiadv.v2.storage;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.sqlbrite.SqlBrite;

import br.com.i9algo.taxiadv.domain.constants.FirebaseVars;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.models.inbound.Category;

public class CategoryDAO {

    private static final String LOG_TAG = "CategoryDAO";

    private final SqlBrite sqlBrite;

    String getCategorybyIdQuery = "SELECT * from " + Category.TABLE + " where " + Category.ID + " = ?";
    String getallCategories = "SELECT * from " + Category.TABLE;

    public CategoryDAO(SqlBrite sqlBrite) {
        this.sqlBrite = sqlBrite;
    }

    public void insert(Category category) {
        sqlBrite.beginTransaction();
        try {
            sqlBrite.insert(Category.TABLE, category.toContentValues());
            sqlBrite.setTransactionSuccessful();
        } finally {
            sqlBrite.endTransaction();
        }
    }

    /**
     * 16/07/2019
     * desabilitado, dados estao no FIREBASE
     *
    public void insertCategoryArray(CategoriesArraylistWrapper categories) {
        sqlBrite.beginTransaction();
        try {
            if (categories != null && categories.getData()!= null && !categories.getData().getCategories().isEmpty()){
                for (Category category : categories.getData().getCategories()) {
                    sqlBrite.insert(Category.TABLE, category.toContentValues());
                }
            }
            sqlBrite.setTransactionSuccessful();
        } finally {
            sqlBrite.endTransaction();
        }
    }


     public Observable<Category> getCategory(final int categoryId) {
        return sqlBrite.createQuery(Category.TABLE, getCategorybyIdQuery, Integer.toString(categoryId))
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, Category>() {
                    @Override
                    public Category call(Cursor cursor) {
                        Logger.v(LOG_TAG, "@@@ - getCategory - " + categoryId);
                        //Logger.v(LOG_TAG, "@@@ - getCategory - " + (cursor!=null));
                        //Logger.v(LOG_TAG, "@@@ - getCategory - " + cursor.getCount());
                        if (cursor!=null && cursor.getCount() != 0) {
                            if (cursor.moveToFirst()) {
                                do {
                                    return Category.fromCursor(cursor);

                                } while (cursor.moveToNext());
                            }
                        }
                        return null;
                    }
                })
                .filter(new Func1<Category, Boolean>() {
                    @Override
                    public Boolean call(Category category) {
                        return category != null && category.getId() == categoryId;
                    }
                });
    }

    public void delete(int categoryId) {
        sqlBrite.delete(Category.TABLE, Category.TABLE + ".id = ?", Integer.toString(categoryId));
    }

    public void update(Category category) {
        sqlBrite.update(Category.TABLE, category.toContentValues(), "id = ?", String.valueOf(category.getId()));
    }
     */

    /**
     * 05/07/2019
     * desabilitado, dados estao no FIREBASE
     *
    public Observable<ArrayList<Category>> getAllCategories() {
        Logger.e(LOG_TAG, "starting get all categories");

        return sqlBrite.createQuery(Category.TABLE, getallCategories)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Logger.i(LOG_TAG, "running get all categories query");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, ArrayList<Category>>() {
                    @Override
                    public ArrayList<Category> call(Cursor cursor) {
                        Logger.i(LOG_TAG, "getting categories");

                        if (cursor!=null && cursor.getCount() != 0) {
                            if (cursor.moveToFirst()) {
                                ArrayList<Category> arrayList = new ArrayList<>();
                                do {
                                    arrayList.add(Category.fromCursor(cursor));

                                } while (cursor.moveToNext());

                                return arrayList;
                            }
                        }
                        return null;
                    }
                });
    }
     */

    /**
     * 16/07/2019
     * desabilitado, dados estao no FIREBASE
     *
    public void cleanCategories() {
        sqlBrite.delete(Category.TABLE, null);
    }

    public void deleteDemo(Integer id) {
        //sqlBrite.delete(Category.TABLE, Category.TABLE + "." + Pref.IS_DEMO_CONTENT_ID + " = ?", Integer.toString(id));
    }
     */
}
