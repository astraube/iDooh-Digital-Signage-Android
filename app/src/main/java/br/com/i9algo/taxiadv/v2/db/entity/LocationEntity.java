package br.com.i9algo.taxiadv.v2.db.entity;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "location_table")
public class LocationEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "latitude")
    private double mLatitude;

    @NonNull
    @ColumnInfo(name = "longitude")
    private double mLongitude;

    @NonNull
    @ColumnInfo(name = "created_at")
    private Date mCreatedAt;

    @NonNull
    @ColumnInfo(name = "sync")
    private boolean mSync = false;

    @Ignore
    public LocationEntity(@NonNull Location location) {
        this.mLatitude = location.getLatitude();
        this.mLongitude = location.getLongitude();
        this.mCreatedAt = new Date();
    }

    public LocationEntity(@NonNull double latitude, @NonNull double longitude, Date createdAt) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mCreatedAt = createdAt;
    }

    public int getId(){ return this.id; }
    public void setId(int id) { this.id = id; }

    public double getLatitude(){ return this.mLatitude; }
    public void setLatitude(double latitude) { this.mLatitude = latitude; }

    public double getLongitude(){ return this.mLongitude; }
    public void setLongitude(double longitude) { this.mLongitude = longitude; }

    public Date getCreatedAt() { return mCreatedAt; }
    public void setCreatedAt(Date createdAt) { this.mCreatedAt = createdAt; }

    public boolean getSync() { return mSync; }
    public void setSync(boolean sync) { this.mSync = sync; }
}
