package br.com.i9algo.taxiadv.v2;

import android.os.AsyncTask;
import android.util.Log;

import br.com.i9algo.taxiadv.v2.db.AppDatabase;
import br.com.i9algo.taxiadv.v2.db.dao.LocationDao;
import br.com.i9algo.taxiadv.v2.db.entity.LocationEntity;

import java.util.List;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    //private MediatorLiveData<List<LocationEntity>> mObservableLocations;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;

        List<LocationEntity> mAll = mDatabase.locationDao().getAllBySync(false);
        Log.e("DataRepository","------>" + mAll.size());


        /*mObservableLocations = new MediatorLiveData<>();
        mObservableLocations.addSource(mDatabase.locationDao().getAll(),
                locationEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableLocations.postValue(locationEntities);
                    }
                });*/
    }

    public static DataRepository getDatabase(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }


    /*public LiveData<List<LocationEntity>> getLocations() {
        return mObservableLocations;
    }*/
    public LocationEntity getLastLocation() {
        return mDatabase.locationDao().getLast();
    }
    public void setSyncLocation(int id) {
        mDatabase.locationDao().setSync(true, id);
    }
    public void insertLocation (LocationEntity location) {
        new insertLocationAsyncTask(mDatabase.locationDao()).execute(location);
    }
    private static class insertLocationAsyncTask extends AsyncTask<LocationEntity, Void, Void> {
        private LocationDao mAsyncTaskDao;
        insertLocationAsyncTask(LocationDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final LocationEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}