package br.com.i9algo.taxiadv.domain.constants;

import com.google.firebase.database.FirebaseDatabase;

import br.com.i9algo.taxiadv.domain.models.Device;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.models.DeviceUser;

public class FirebaseVars {

    //private static final boolean OFFLINE_PERSISTENCE = true;

    //public static final String DB_CHILD_NETWORK = "/network";
    //public static final String DB_CHILD_DEVICE_INFO = "/device/info";
    public FirebaseDatabase getDbDefault() {
        return FirebaseDatabase.getInstance();
    }

    /*
    // https://firebase.google.com/docs/database/usage/sharding
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://idooh-public.firebaseio.com");
    DatabaseReference dbRef = db.getReference()
            .child(FirebaseVars.DB_PUBLIC_CHILD_MSG)
            .child("dfdfd");*/
    public static final String DB_PUBLIC = "https://idooh-public.firebaseio.com";
    public static final String DB_PUBLIC_CHILD_MSG = "/msg";
    public static FirebaseDatabase getDbPublic() {
        return FirebaseDatabase.getInstance(DB_PUBLIC);
    }

    public static final String DB_CONTENT = "https://idooh-content.firebaseio.com";
    public static final String DB_CONTENT_CHILD_GROUP_CWB = "/curitiba"; // teste temporario
    public static final String DB_CONTENT_CHILD_NEWSFLASH = "/newsflash";
    public static FirebaseDatabase getDbContent() {
        return FirebaseDatabase.getInstance(DB_CONTENT);
    }

    public static final String DB_GEO = "https://idooh-geo.firebaseio.com";
    public static final String DB_GEO_CHILD_REALTIME = "/realtime";
    public static final String DB_GEO_CHILD_HISTORY = "/history";
    public static FirebaseDatabase getDbGeo() {
        return FirebaseDatabase.getInstance(DB_GEO);
    }

    public static final String DB_DEVICE = "https://idooh-device.firebaseio.com/";
    public static final String DB_DEVICE_CHILD_APPROVAL = "approval";
    public static final String DB_DEVICE_CHILD_INFO = "/info";
    public static FirebaseDatabase getDbDevice() {
        return FirebaseDatabase.getInstance(DB_DEVICE);
    }

    // Storage
    public static final String GS_CHILD_DEVICE_PRINT = "/device/" + DeviceUser.getUid() + "/prints/";
}
