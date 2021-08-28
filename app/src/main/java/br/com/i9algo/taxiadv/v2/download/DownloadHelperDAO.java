package br.com.i9algo.taxiadv.v2.download;

import android.database.Cursor;
import android.util.Log;

import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import br.com.i9algo.taxiadv.domain.constants.Constants;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.helpers.defaults.DefaultSubscriber;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DownloadHelperDAO {

    private static final String LOG_TAG = "DownloadHelperDAO";

    private final SqlBrite sqlBrite;
    private String getAllDownloadableTasks = "Select * from " + DownloadItem.TABLE ;
    private String getDownloadableTaskByTaskId = "Select * from " + DownloadItem.TABLE + " where " + DownloadItem.TASK_ID + " = ?";
    private String getDownloadableTaskBySlideId = "Select * from " + DownloadItem.TABLE + " where " + DownloadItem.SLIDE_ID + " = ? and " + DownloadItem.STATUS + " = " + Constants.DOWNLOAD_COMPLETE + " ORDER BY ROWID ASC LIMIT 1 ";
    private String getDownloadableTaskByURL = "Select * from " + DownloadItem.TABLE + " where " + DownloadItem.ORIGINAL_URL + " = ? LIMIT 1";
    private String updateStatusAndFileQuery = "UPDATE " + DownloadItem.TABLE + " SET " + DownloadItem.STATUS + " = ? AND " + DownloadItem.FILE_LOCATION + " = ? WHERE " + DownloadItem.TASK_ID + " = ?";

    // this query will get the failed items that did not had a sucessfull download yet
    private String getFailedDownloadableTasks = "Select a.* from (" + getAllDownloadableTasks + " where " + DownloadItem.STATUS + " = 3) as a left join (" + getAllDownloadableTasks + " where " + DownloadItem.STATUS + " = 2) as b on a." + DownloadItem.SLIDE_ID + " = b." + DownloadItem.SLIDE_ID + " where b." + DownloadItem.SLIDE_ID + " is NULL";

    private Subscription updateSubscription;

    public DownloadHelperDAO(SqlBrite sqlBrite) {
        this.sqlBrite = sqlBrite;
    }

    public void insert(DownloadItem item) {
        sqlBrite.beginTransaction();
        try {
            sqlBrite.insert(item.TABLE, item.toContentValues());
            sqlBrite.setTransactionSuccessful();
        } finally {
            sqlBrite.endTransaction();
        }
    }

    public void delete(int id) {
        sqlBrite.delete(DownloadItem.TABLE, DownloadItem.TABLE + ".id = ?", Integer.toString(id));
    }

    public void update(DownloadItem item) {
        Logger.i(LOG_TAG, "Updating item with id " + item.getId());
        sqlBrite.update(DownloadItem.TABLE, item.toContentValues(), "id = ?", String.valueOf(item.getId()));
    }

    public Observable<DownloadItem> getItemByUrl(final String url) {
        Logger.i(LOG_TAG, "getItemByUrl - starting get download item for url " + url);
        return sqlBrite.createQuery(DownloadItem.TABLE, getDownloadableTaskByURL, url)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Logger.i(LOG_TAG, "getItemByUrl - running get download item url " + url);
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, DownloadItem>() {
                    @Override
                    public DownloadItem call(Cursor cursor) {
                        Logger.i(LOG_TAG, "getItemByUrl - getting download item url " + url);
                        DownloadItem item = null;
                        while (cursor.moveToNext()) {
                            item = DownloadItem.fromCursor(cursor);
                            Logger.i(LOG_TAG, "getItemByUrl - download item url " + item.getOriginalURL());
                            Logger.i(LOG_TAG, "getItemByUrl - download item file " + item.getFileLocation());
                        }
                        if (item == null) {
                            Logger.i(LOG_TAG, "getItemByUrl - item null for url " + url);
                        }else{
                            Logger.i(LOG_TAG, "getItemByUrl - item for url " + url + " is status" + item.getStatus() + " file " + item.getFileLocation());
                        }
                        return item;
                    }
                });
    }

    public Observable<DownloadItem> getItemByTaskId(final String taskId) {
        Logger.i(LOG_TAG, "starting get download item");
        return sqlBrite.createQuery(DownloadItem.TABLE, getDownloadableTaskByTaskId, taskId)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Logger.i(LOG_TAG, "getItemByTaskId - running get download item for task id" + taskId);
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, DownloadItem>() {
                    @Override
                    public DownloadItem call(Cursor cursor) {
                        Logger.i(LOG_TAG, "getItemByTaskId - getting download item");
                        DownloadItem item = null;
                        Logger.i(LOG_TAG, "cursor.getCount() = " + cursor.getCount());
                        if (cursor.moveToNext()) {
                            item = DownloadItem.fromCursor(cursor);
                            Logger.i(LOG_TAG, "getItemByTaskId - url " + item.getOriginalURL());
                            Logger.i(LOG_TAG, "getItemByTaskId - status " + item.getStatus());
                        }
                        return item;
                    }
                });
    }

    public Observable<List<DownloadItem>> getItems() {
        Logger.i(LOG_TAG, "getItems()");

        return sqlBrite.createQuery(DownloadItem.TABLE, getAllDownloadableTasks)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Logger.i(LOG_TAG, "running get all DownloadHelper query");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, List<DownloadItem>>() {
                    @Override
                    public List<DownloadItem> call(Cursor cursor) {
                        List<DownloadItem> arrayList = new ArrayList<>();
                        Logger.i(LOG_TAG, "getting DownloadHelper");
                        while (cursor.moveToNext()) {
                            arrayList.add(DownloadItem.fromCursor(cursor));
                        }
                        return arrayList;
                    }
                });
    }

    public Observable<List<DownloadItem>> getFailedItems() {
        Logger.i(LOG_TAG, "starting get all categories");
        return sqlBrite.createQuery(DownloadItem.TABLE, getFailedDownloadableTasks)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Logger.i(LOG_TAG, "running get all DownloadHelper query");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, List<DownloadItem>>() {
                    @Override
                    public List<DownloadItem> call(Cursor cursor) {
                        List<DownloadItem> arrayList = new ArrayList<>();
                        Logger.i(LOG_TAG, "getting DownloadHelper");
                        while (cursor.moveToNext()) {
                            arrayList.add(DownloadItem.fromCursor(cursor));
                        }
                        return arrayList;
                    }
                });
    }

    public void cleanTasks() {
        sqlBrite.delete(DownloadItem.TABLE, null);

    }

    private void updateStatusAndFile(long taskId, final int status, final String file) {
        Logger.e("adv", "updateStatusAndFile: taskId: " + taskId +  ", status: " + status + ", file: " + file);
        updateSubscription =
                getItemByTaskId(Long.toString(taskId))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultSubscriber<DownloadItem>() {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                e.printStackTrace();
                                Logger.e(LOG_TAG, "updateStatusAndFile.onErroring");
                                updateSubscription.unsubscribe();
                            }

                            @Override
                            public void onNext(DownloadItem object) {
                                super.onNext(object);
                                Logger.i(LOG_TAG, "updateStatusAndFile.OnNexting");
                                updateSubscription.unsubscribe();
                                object.setFileLocation(file);
                                object.setStatus(status);
                                Logger.i(LOG_TAG, "updateStatusAndFile.OnNexting - updating");
                                update(object);

                            }

                            @Override
                            public void onCompleted() {
                                Logger.i(LOG_TAG, "updateStatusAndFile.Starting service");
                                updateSubscription.unsubscribe();
                            }
                        });
    }

    public void updateTaskStatusSucess(int i, String path, boolean status) {
        Logger.i(LOG_TAG, "updateTaskStatusSucess = " + status);
        if (status) {
            updateStatusAndFile(i, Constants.DOWNLOAD_COMPLETE, path);
        } else {
            updateStatusAndFile(i, Constants.DOWNLOAD_ERROR, path);
        }
    }

    public Observable<DownloadItem> getItemBySlideId(int slideId) {
        Logger.i(LOG_TAG, "starting get download item");
        return sqlBrite.createQuery(DownloadItem.TABLE, getDownloadableTaskBySlideId, Integer.toString(slideId))
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        Logger.i(LOG_TAG, "running get download item");
                        return query.run();
                    }
                })
                .map(new Func1<Cursor, DownloadItem>() {
                    @Override
                    public DownloadItem call(Cursor cursor) {
                        Logger.i(LOG_TAG, "getting download item");
                        DownloadItem item = null;
                        Logger.i(LOG_TAG, "cursor.getCount() = " + cursor.getCount());
                        if (cursor.moveToNext()) {
                            Logger.i(LOG_TAG, "moved to next");
                            item = DownloadItem.fromCursor(cursor);
                        }
                        return item;
                    }
                });
    }

    public void insertList(List<DownloadItem> dlitemsToInsert) {
        sqlBrite.beginTransaction();
        try {
            for (DownloadItem item : dlitemsToInsert){
                sqlBrite.insert(item.TABLE, item.toContentValues());
            }
            sqlBrite.setTransactionSuccessful();
        } finally {
            sqlBrite.endTransaction();
        }
    }
}
