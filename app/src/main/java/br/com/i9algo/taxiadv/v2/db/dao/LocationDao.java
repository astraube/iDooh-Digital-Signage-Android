package br.com.i9algo.taxiadv.v2.db.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import br.com.i9algo.taxiadv.v2.db.entity.LocationEntity;

@Dao
public interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocationEntity location);

    @Query("DELETE FROM location_table")
    void deleteAll();

    @Query("UPDATE location_table SET sync = :sync where id = :locationId")
    void setSync(boolean sync, int locationId);

    @Query("SELECT * from location_table ORDER BY id DESC LIMIT 0, 1")
    LocationEntity getLast();

    @Query("SELECT * from location_table ORDER BY id ASC")
    List<LocationEntity> getAll();

    @Query("SELECT * from location_table where sync = :synced ORDER BY id ASC")
    List<LocationEntity> getAllBySync(boolean synced);
}
