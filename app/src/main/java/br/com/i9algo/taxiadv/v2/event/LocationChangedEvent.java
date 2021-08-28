package br.com.i9algo.taxiadv.v2.event;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

public class LocationChangedEvent {

  private final Context context;
  private final Location location;

  public LocationChangedEvent(@NonNull Context context, @NonNull Location location) {
    this.context = context;
    this.location = location;
  }

  public Context getContext() {
    return this.context;
  }

  public Location getLocation() {
    return this.location;
  }
  public double getLatitude() {
    return this.location.getLatitude();
  }
  public double getLongitude() {
    return this.location.getLongitude();
  }

  @Override public String toString() {
    return new StringBuilder("(") //
            .append(this.location.getLatitude()) //
            .append(", ") //
            .append(this.location.getLongitude()) //
            .append(")") //
            .toString();
  }
}
