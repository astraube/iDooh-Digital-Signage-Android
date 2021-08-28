package br.com.i9algo.taxiadv.v2.models;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;

import br.com.i9algo.taxiadv.libs.utilcode.util.DeviceUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.EncryptUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.StringUtils;
import br.com.i9algo.taxiadv.v2.utils.Pref;

public class DeviceUser {

    private static final String _prefixEmail = "_v2@idooh.com.br";

    private static String username = (DeviceUtils.getSerial() + _prefixEmail);

    private static String password = "$57dgdu8trdjo@#3";

    private static String mUid = null;

    private static String getIdentifierDefault() {
        String serial = DeviceUtils.getSerial();
        return EncryptUtils.encryptHmacSHA512ToString(getUsername(), serial);
    }

    public static String getEmail() { return username; }
    public static String getUsername() { return username; }

    public static String getPassword() { return password; }

    public static String getUid() {
        if (mUid != null && !StringUtils.isEmpty(mUid))
            return mUid;

        mUid = Pref.getUid();

        if (mUid != null && !StringUtils.isEmpty(mUid))
            return mUid;

        return DeviceUser.getIdentifierDefault();
    }

    /**
     * Update local UID prefs from Firebase AUTH UID
     * @param fbUser
     */
    public static void setUidByFirebase(FirebaseUser fbUser) {
        if (fbUser != null && !TextUtils.isEmpty(fbUser.getUid())) {
            Pref.setUid(fbUser.getUid());
            mUid = fbUser.getUid();
        }
    }
}
