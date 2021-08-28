package br.com.i9algo.taxiadv.v2.injection.module;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import br.com.i9algo.taxiadv.v2.download.DownloadHelperDAO;
import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;
import br.com.i9algo.taxiadv.v2.storage.CategoryDAO;
import br.com.i9algo.taxiadv.v2.storage.FillerDAO;
import br.com.i9algo.taxiadv.v2.storage.GeofencedAdvertDAO;
import br.com.i9algo.taxiadv.v2.storage.PlaylistDAO;
import br.com.i9algo.taxiadv.v2.storage.SidebarDAO;
import br.com.i9algo.taxiadv.v2.storage.SlideDAO;
import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private final SQLiteOpenHelper sqlOpenHelper;
    private SqlBrite sqlBrite;

    public DatabaseModule(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqlOpenHelper = sqLiteOpenHelper;
    }

    @Provides
    @Singleton
    SqlBrite providesSqlBrite() {
        return getSqlBrite();
    }

    @Provides
    @Singleton
    CategoryDAO providesCategoryDao(SqlBrite sqlBrite) {
        return new CategoryDAO(sqlBrite);
    }

    @Provides
    @Singleton
    SlideDAO providesSlideDao(SqlBrite sqlBrite) {
        return new SlideDAO(sqlBrite);
    }

    @Provides
    @Singleton
    FillerDAO providesFillerDao(SqlBrite sqlBrite) {
        return new FillerDAO(sqlBrite);
    }

    @Provides
    @Singleton
    DownloadHelperDAO providesDownloadDao(SqlBrite sqlBrite) {
        return new DownloadHelperDAO(sqlBrite);
    }


    @Provides
    @Singleton
    PlaylistDAO providesPlaylistDao(SqlBrite sqlBrite) {
        return new PlaylistDAO(sqlBrite, providesSlideDao(sqlBrite));
    }

    @Provides
    @Singleton
    GeofencedAdvertDAO providesGeofenceDao(SqlBrite sqlBrite) {
        return new GeofencedAdvertDAO(sqlBrite);
    }

    @Provides
    @Singleton
    SidebarDAO providesSidebarDao(SqlBrite sqlBrite) {
        return new SidebarDAO(sqlBrite);
    }

    private SqlBrite getSqlBrite() {
        if (sqlBrite == null) {
            sqlBrite = SqlBrite.create(sqlOpenHelper);
            sqlBrite.setLoggingEnabled(true);
        }
        return sqlBrite;
    }

}
