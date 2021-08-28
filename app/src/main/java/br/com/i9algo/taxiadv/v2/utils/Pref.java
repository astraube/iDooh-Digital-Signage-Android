package br.com.i9algo.taxiadv.v2.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import br.com.i9algo.taxiadv.domain.models.Device;
import br.com.i9algo.taxiadv.libs.utilcode.util.StringUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.Utils;
import br.com.i9algo.taxiadv.v2.CustomApplication;

public class Pref {

	public static final String PREFERENCE_CAR_NUMBER = "br.com.i9algo.taxiadv.extra.pref.CAR_NUMBER";
	public static final String IS_DEMO = "IS_DEMO";
	public static final String IS_DEMO_CONTENT_ID = "IS_DEMO_CONTENT_ID";
	public static final String NETWORK_CONTENT_GROUP = "NETWORK_CONTENT_GROUP";

	private Pref() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	/**
	 * Editar valores da SharedPreferences
	 * @param key
	 * @param value
	 */
    public static void savePrefKeyValue(String key, String value ){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Utils.getApp().getApplicationContext());
        SharedPreferences.Editor e = sp.edit();
        e.putString( key, value );
        e.apply();
		e.commit();
    }

	/**
	 * Resgatar valores da SharedPreferences
	 * @param key
	 * @param defaultValue
	 * @return
	 */
    public static String retrievePrefKeyValue(String key, String defaultValue ){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( Utils.getApp().getApplicationContext() );
        String dValue = defaultValue != null ? defaultValue : "";
        String v = sp.getString(key, dValue);
        return( v );
    }

	/**
	 * Verificar se algum valor existe
	 * @param key
	 * @return
	 */
	public static boolean isExistsPrefKeyValue(String key){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( Utils.getApp().getApplicationContext() );
		String v = sp.getString(key, "");
		return (!TextUtils.isEmpty(v));
	}

	public static void putBoolean(final String key, final boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Utils.getApp().getApplicationContext());
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.apply();
		editor.commit();
	}

	public static boolean getBoolean(final String key) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Utils.getApp().getApplicationContext());
		return sharedPreferences.getBoolean(key, false);
	}

	public static final String getNetworkContentGroup() {
		// TODO - quando utilizar Constants.GROUP_ID, alterar BackEnd
		//String result = retrievePrefKeyValue(Pref.NETWORK_CONTENT_GROUP, Constants.GROUP_ID);

		String result = retrievePrefKeyValue(Pref.NETWORK_CONTENT_GROUP, "parana");
		return result;
	}
	public static void setNetworkContentGroup(String networkContentCode){
		String code = networkContentCode.trim().toLowerCase();
		code = StringUtils.deAccent(code);
		code = StringUtils.substringBackspace(code, "_");
		Pref.savePrefKeyValue(NETWORK_CONTENT_GROUP, code);
	}

	public static final String getCarNumber() {
		String result = retrievePrefKeyValue(Pref.PREFERENCE_CAR_NUMBER, "");
		return result;
	}
	public static final void setCarNumber(final String newValue) {
		savePrefKeyValue(Pref.PREFERENCE_CAR_NUMBER, newValue);
	}


    public static final Boolean isDemo() {
		return getBoolean(Pref.IS_DEMO);
	}
	/*public static final void setIsDemo(final Context context, final Boolean newValue) {
		CustomApplication.isDemo = newValue;
		putBoolean(Pref.IS_DEMO, newValue, context);
	}*/
    public static final void setIsDemo(final Boolean newValue, int demoId) {
        CustomApplication.isDemo = newValue;
        putBoolean(Pref.IS_DEMO, newValue);

        setDemoId(demoId);
    }

    /*
    // TODO - esta dando pau!!!
	public static int getDemoId(){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Utils.getApp().getApplicationContext());
		return sharedPreferences.getInt(IS_DEMO_CONTENT_ID, 0);
	}*/
	public static void setDemoId(int demoId){
		CustomApplication.isDemoContentId = demoId;
		Pref.savePrefKeyValue(IS_DEMO_CONTENT_ID, String.valueOf(demoId));
	}

	/**
	 * Serve para verificar se foi trocado o sim card do aparelho
	 *
	 * @return
	 */
	public static final String getDeviceImei() {
		return retrievePrefKeyValue(Device.IMEI, "");
	}
	public static final void setDeviceImei(final String newValue) {
		savePrefKeyValue(Device.IMEI, newValue);
	}


	public static boolean hasUid() {
		return isExistsPrefKeyValue(Device.UID);
	}

	public static String getUid() {
		return retrievePrefKeyValue(Device.UID, "");
	}

    public static final void setUid(String uid) {
        savePrefKeyValue(Device.UID, uid);
    }
}
