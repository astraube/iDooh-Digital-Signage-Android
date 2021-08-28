package br.com.i9algo.taxiadv.v2.helpers;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import br.com.i9algo.taxiadv.domain.constants.Constants;
import br.com.i9algo.taxiadv.domain.models.GeoCoordinate;
import br.com.i9algo.taxiadv.libs.utilcode.util.ShellUtils;
import br.com.i9algo.taxiadv.v2.CustomApplication;


/**
 * Utility methods used in this sample.
 */
public class LocationUtils {

    public final static String LOCATION_DEFAULT = "0.0, 0.0";
    public final static String KEY_LOCATION_LAST_UPDATE = "location-last-update";
    public final static String KEY_LOCATION_UPDATES_RESULT = "location-update-result";


    public static void requestPermissions(Activity activity) {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Logger.i("Displaying permission rationale to provide additional context.");

            // Request permission
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            Logger.i("Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    public static void enableGpsForResult(Activity activity) {
        activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
    }
    public static void turnGPSOn()
    {
        try {

            ShellUtils.execCmd("settings put secure location_providers_allowed +gps,network", true);

            /*String beforeEnable = Settings.Secure.getString (activity.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            String newSet = String.format ("%s,%s", beforeEnable, LocationManager.GPS_PROVIDER);
            try {
                Settings.Secure.putString (activity.getContentResolver(),
                        Settings.Secure.LOCATION_PROVIDERS_ALLOWED,
                        newSet);
            } catch(Exception e) {}




            // Funciona apenas se o aplicativo estiver instalado na pasta system
            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", true);
            activity.sendBroadcast(intent);



			ContentResolver cr = activity.getContentResolver();
			Settings.Secure.setLocationProviderEnabled(cr, LocationManager.GPS_PROVIDER, true);*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void turnGpsOff (Context context) {
        String beforeEnable = "";
        if (beforeEnable.isEmpty()) {
            String str = Settings.Secure.getString (context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (null == str) {
                str = "";
            } else {
                String[] list = str.split (",");
                str = "";
                int j = 0;
                for (int i = 0; i < list.length; i++) {
                    if (!list[i].equals (LocationManager.GPS_PROVIDER)) {
                        if (j > 0) {
                            str += ",";
                        }
                        str += list[i];
                        j++;
                    }
                }
                beforeEnable = str;
            }
        }
        try {
            Settings.Secure.putString (context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED,
                    beforeEnable);
        } catch(Exception e) {}
    }

    /**
     * Return the current state of the permissions needed.
     */
    public static boolean checkPermissions(Activity activity) {
        int permissionState1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionState2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState1 == PackageManager.PERMISSION_GRANTED || permissionState2 == PackageManager.PERMISSION_GRANTED;
    }

    public static Location setLastLocation(Context context, List<Location> locations) {
        Location lastLocation = getLocationResult(locations);

        setLastLocation(context, lastLocation);

        return lastLocation;
    }
    public static void setLastLocation(Context context, Location location) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_LOCATION_LAST_UPDATE, getLocationString(context, location))
                .apply();
    }
    public static String getLastLocation(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_LAST_UPDATE, null);
    }

    public static void setLocationUpdatesResult(Context context, List<Location> locations) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT, getLocationResultText(context, locations))
                .apply();
    }
    public static String getLocationUpdatesResult(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_LOCATION_UPDATES_RESULT, LOCATION_DEFAULT);
    }

    public static Location getLocationResult(List<Location> locations) {
        if (locations.isEmpty()) {
            return null;
        }
        //LocationRoom lastLocation = locations.get(locations.size() - 1);
        return locations.get(0);
    }

    /**
     * Returns te text for reporting about a list of  {@link Location} objects.
     *
     * @param locations List of {@link Location}s.
     */
    public static String getLocationResultText(Context context, List<Location> locations) {
        if (locations.isEmpty()) {
            return LOCATION_DEFAULT;
        }
        StringBuilder sb = new StringBuilder();
        for (Location location : locations) {
            sb.append("(");
            sb.append(location.getLatitude());
            sb.append(", ");
            sb.append(location.getLongitude());
            sb.append(")");
            sb.append("\n");
        }
        return sb.toString();
    }
    public static String getLocationString(Context context, Location location) {
        if (location == null) {
            return LOCATION_DEFAULT;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(location.getLatitude());
        sb.append(",");
        sb.append(location.getLongitude());
        return sb.toString();
    }
    public static GeoCoordinate getLocationByString(Context context, String locationStr) {
        if (locationStr.isEmpty()) {
            return null;
        }
        String[] latLng = locationStr.split(",");
        double latitude = Double.parseDouble(latLng[0].trim());
        double longitude = Double.parseDouble(latLng[1].trim());

        return new GeoCoordinate(latitude, longitude);
    }
    public static Location getLocationByString(String locationStr) {
        double latitude;
        double longitude;
        if (locationStr == null || locationStr.isEmpty()) {
            latitude = 0;
            longitude = 0;
        } else {
            String[] latLng = locationStr.split(",");
            latitude = Double.parseDouble(latLng[0].trim());
            longitude = Double.parseDouble(latLng[1].trim());
        }
        Location loc = new Location(locationStr);
        loc.setLatitude(latitude);
        loc.setLongitude(longitude);
        return loc;
    }
    public static GeoCoordinate getLocationCoordinatesByString(String locationStr) {
        double latitude;
        double longitude;
        if (locationStr.isEmpty()) {
            latitude = 0;
            longitude = 0;
        } else {
            String[] latLng = locationStr.split(",");
            latitude = Double.parseDouble(latLng[0].trim());
            longitude = Double.parseDouble(latLng[1].trim());
        }
        return new GeoCoordinate(latitude, longitude);
    }
}