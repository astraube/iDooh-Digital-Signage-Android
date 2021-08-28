package br.com.i9algo.taxiadv.v2.network.taxiadv;

import android.location.Location;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;

import com.amazonaws.mobileconnectors.apigateway.ApiClientException;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.i9algo.taxiadv.domain.constants.FirebaseVars;
import br.com.i9algo.taxiadv.domain.models.Device;
import br.com.i9algo.taxiadv.domain.models.GeoCoordinate;
import br.com.i9algo.taxiadv.v2.helpers.LocationUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.NetworkUtils;
import br.com.i9algo.taxiadv.v2.storage.firebase.RemoteConfigs;

public class IdoohMediaDeviceController {

    //private static final boolean sendOnlyMac = false;

    public static void sendGeopoint(double latitude, double longitude) {
        GeoCoordinate geoCoordinate = new GeoCoordinate(latitude, longitude);
        IdoohMediaDeviceController.sendGeopoint(geoCoordinate);
    }
    public static void sendGeopoint(Location location) {
        GeoCoordinate geoCoordinate;
        if (location != null)
            geoCoordinate = new GeoCoordinate(location.getLatitude(), location.getLongitude());
        else
            geoCoordinate = LocationUtils.getLocationCoordinatesByString(null);

        IdoohMediaDeviceController.sendGeopoint(geoCoordinate);
    }
    public static void sendGeopoint(GeoCoordinate geoCoordinate) {
        if (!NetworkUtils.isConnected())
            return;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        String formattedDate = sdf.format(new Date());

        Device.getInstance().setCoordinates(geoCoordinate);
        //Device.getInstance().setCreatedAt(formattedDate);
        Device.getInstance().setSentAt(formattedDate);
        Device.getInstance().cleanDeviceBuildInfos();
        Device.getInstance().putValue("angle", 60); // TODO - pegar valor dinamico

        send(Device.getInstance());
    }

    public static void sendDataDevice(Device device, String dbCollection) {
        FirebaseDatabase db = FirebaseDatabase.getInstance(FirebaseVars.DB_DEVICE);
        DatabaseReference dbRef = db.getReference().child(dbCollection).child(device.getUid());
        //Logger.e(LOG_TAG, "@#=" + device.toJsonObject());
        dbRef.push().updateChildren(device.toObjectMap());
    }

    private static void send(Device device) {
        String dbType = RemoteConfigs.getDbSaveInWhich();

        //Logger.v(IdoohMediaDeviceController.class.getSimpleName(), " @#=> " + Device.getInstance().toJsonObject().toString());

        /** FIREBASE DATABADE **/
        if (dbType.equals("firebase_database") || dbType.equals("all")) {

            FirebaseDatabase db = FirebaseVars.getDbGeo();
            DatabaseReference dbRef = db.getReference().child(FirebaseVars.DB_GEO_CHILD_HISTORY).child(device.getDeviceSerial());
            //dbRef.keepSynced(true);

            String key = dbRef.push().getKey();
            DatabaseReference fDb = dbRef.child(key);

            try {
                fDb.updateChildren(device);

            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (dbType.equals("aws") || dbType.equals("all")) {
            ApiClientFactory factory = new ApiClientFactory();
            final IdoohMediaDeviceClient client = factory.build(IdoohMediaDeviceClient.class);

            try {
                // Call API
                client.deviceGeoPost(device);

            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            } catch (ApiClientException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*if (dbType.equals("firebase_datastore") || dbType.equals("all")) {
            Firestore db = FirestoreOptions.getDefaultInstance().getService();

            DocumentReference docRef = db.collection("device").document("geo");
            docRef.collection("history").document();

            // Add document data  with id "alovelace" using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("first", "Ada");
            data.put("last", "Lovelace");
            data.put("born", 1815);
            //asynchronously write data
            docRef.set(data);
            //docRef.set(device);
        }*/
    }
}
