package br.com.i9algo.taxiadv.domain.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import br.com.i9algo.taxiadv.BuildConfig;
import br.com.i9algo.taxiadv.libs.utilcode.util.DeviceUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.PhoneUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.SizeUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.StorageUtil;
import br.com.i9algo.taxiadv.libs.utilcode.util.Utils;
import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.v2.helpers.ValueMap;
import br.com.i9algo.taxiadv.v2.models.DeviceUser;
import br.com.i9algo.taxiadv.v2.utils.IDManagement;
import br.com.i9algo.taxiadv.v2.utils.Installation;
import br.com.i9algo.taxiadv.v2.utils.Pref;

@IgnoreExtraProperties
public class Device extends ValueMap {

    private static Device _instance = null;

    public static Device getInstance() {
        return Device._instance;
    }

    @SerializedName("debug")
    private boolean debug = BuildConfig.DEBUG;

    public static final String UID = "uid"; // User ID Firebase Auth
    public static final String FCM_TOKEN = "fcmToken"; // Firebase Token FCM

    public static final String SENT_AT = "sentAt";
    public static final String ID = "deviceId";
    public static final String SERIAL = "deviceSerial";
    public static final String MAC_ADDRESS = "deviceMacAddress";
    public static final String IMEI = "deviceImei";
    public static final String PHONE_NUMBER = "devicePhoneNumber";
    public static final String CAR_NUMBER = "deviceCarNumber";
    public static final String INSTALL_CODE = "deviceInstallationCode";
    public static final String ANDROID_USER_ID = "deviceAndroidUserId";

    // Device infos build
    public static final String INFOS = "deviceInfos";
    public static final String INFOS_BUILD = "build";
    public static final String INFOS_TELEPHONY = "telephony";
    public static final String INFOS_STORAGE= "storage";

    // For Geometry
    public static final String KEY_COORDINATES = "coordinates";


    public static void init(Context context) {
        new Device(context, false, false);
    }

    // For deserialization
    Device(Map<String, Object> delegate) {
        super(delegate);
        Device._instance = this;
    }

    public Device(){
        Device._instance = this;
    }

    @SuppressLint("MissingPermission")
    public Device(Context context, boolean onlyMac, boolean withDeviceBuildInfos) {
        Device._instance = this;

        putValue("debug", this.debug);

        this.setDeviceId(DeviceUtils.getSerial());
        this.setDeviceSerial(PhoneUtils.getSerial());

        if (onlyMac) {
            this.setDeviceMacAddress(DeviceUtils.getMacAddress());
            return;
        }
        if (withDeviceBuildInfos) {
            this.withDeviceBuildInfos();
        }
        this.setDeviceMacAddress(DeviceUtils.getMacAddress());
        this.setDevicePhoneNumber(PhoneUtils.getPhoneNumber());
        this.setDeviceImei(PhoneUtils.getIMEI());
        this.setDeviceCarNumber(Pref.getCarNumber());
        this.setDeviceInstallationCode(Installation.id(context));
        this.setDeviceAndroidUserId(DeviceUtils.getAndroidID());
    }

    // For Geometry
    public GeoCoordinate getCoordinates() {
        return (GeoCoordinate) Device._instance.get(KEY_COORDINATES);
    }
    public void setCoordinates(GeoCoordinate coordinates) {
        putValue(KEY_COORDINATES, coordinates);
    }

    @Override
    public Device putValue(String key, Object value) {
        super.putValue(key, value);
        return this;
    }

    public void cleanDeviceBuildInfos() {
        this.remove(INFOS_BUILD);
        this.remove(INFOS_TELEPHONY);
        this.remove(INFOS_STORAGE);
    }
    public void withDeviceBuildInfos() {
        this.putValue("versionCode", BuildConfig.VERSION_CODE);
        this.putValue("versionName", BuildConfig.VERSION_NAME);
        this.putValue(INFOS_BUILD, DeviceUtils.getAllDevideBuild().toObjectMap());
        this.putValue(INFOS_TELEPHONY, PhoneUtils.getAllTelephonyInfo().toObjectMap());
        this.putValue(INFOS_STORAGE, getAllStorageInfo().toJsonObject());
    }

    public ValueMap getAllStorageInfo() {
        ValueMap storage = new ValueMap();
        storage.put("totalExternalStorage", getTotalExternalStorage());
        storage.put("totalInternalStorage", getTotalInternalStorage());
        storage.put("availableExternalStorage", getAvailableExternalStorage());
        storage.put("availableInternalStorage", getAvailableInternalStorage());
        storage.put("internalStorageUsagePercentage", getInternalStorageUsagePercentage());
        storage.put("externalStorageUsagePercentage", getExternalStorageUsagePercentage());
        storage.put("batteryLevel", getBatteryLevel());
        return storage;
    }
    public String getTotalExternalStorage() { return SizeUtils.sizeToStringFormated(StorageUtil.getExternalTotalSpace()); }
    public String getTotalInternalStorage() { return SizeUtils.sizeToStringFormated(StorageUtil.getInternalTotalSpace()); }
    public String getAvailableExternalStorage() { return SizeUtils.sizeToStringFormated(StorageUtil.getExternalAvailableSpace()); }
    public String getAvailableInternalStorage() { return SizeUtils.sizeToStringFormated(StorageUtil.getInternalAvailableSpace()); }
    public String getInternalStorageUsagePercentage() { return Double.toString(StorageUtil.getInternalDiskSpaceUsagePercentage()) + "%"; }
    public String getExternalStorageUsagePercentage() { return Double.toString(StorageUtil.getExternalDiskSpaceUsagePercentage()) + "%"; }
    public String getBatteryLevel() { return IDManagement.getBatteryLevel( Utils.getApp() ) + " %"; }

    public String getUid() {
        return DeviceUser.getUid();
    }

    public String getFCMToken() {
        return getString(FCM_TOKEN);
    }
    public void setFCMToken(String refreshToken) {
        putValue(FCM_TOKEN, refreshToken);
    }

    public String getSentAt() {
        return getString(SENT_AT);
        //return sentAt;
    }
    public void setSentAt(String sentAt) {
        //this.sentAt = sentAt;
        putValue(SENT_AT, sentAt);
    }


    public ValueMap getDeviceInfos() {
        return getValueMap(INFOS);
        //return deviceInfos;
    }
    public void setDeviceInfos(ValueMap deviceInfos) {
        putValue(INFOS, deviceInfos);
    }
    public void setDeviceInfos(String key, ValueMap deviceInfos) {
        putValue(key, deviceInfos);
    }
    public void setDeviceInfos(String key, Map<String, Object> deviceInfos) {
        putValue(key, deviceInfos);
    }
    public void addDeviceInfos(String key, Object value) {
        if (this.getDeviceInfos() == null)
            putValue(INFOS, new ValueMap());

        this.getDeviceInfos().put(key, value);
    }

    public String getDeviceId() {
        return getString(ID);
        //return deviceId;
    }
    public void setDeviceId(String deviceId) {
        //this.deviceId = deviceId;
        putValue(ID, deviceId);
    }

    public String getDeviceSerial() {
        return getString(SERIAL);
        //return deviceSerial;
    }
    public void setDeviceSerial(String deviceSerial) {
        //this.deviceSerial = deviceSerial;
        putValue(SERIAL, deviceSerial);
    }

    public String getDeviceMacAddress() {
        return getString(MAC_ADDRESS);
        //return deviceMacAddress;
    }
    public void setDeviceMacAddress(String deviceMacAddress) {
        //this.deviceMacAddress = deviceMacAddress;
        putValue(MAC_ADDRESS, deviceMacAddress);
    }

    public String getDeviceImei() {
        return getString(IMEI);
        //return deviceImei;
    }
    public void setDeviceImei(String deviceImei) {
        //this.deviceImei = deviceImei;
        putValue(IMEI, deviceImei);
    }

    public String getDevicePhoneNumber() {
        return getString(PHONE_NUMBER);
        //return devicePhoneNumber;
    }
    public void setDevicePhoneNumber(String devicePhoneNumber) {
        //this.devicePhoneNumber = devicePhoneNumber;
        putValue(PHONE_NUMBER, devicePhoneNumber);
    }

    public String getDeviceCarNumber() {
        return getString(CAR_NUMBER);
        //return deviceCarNumber;
    }
    public void setDeviceCarNumber(String deviceCarNumber) {
        //this.deviceCarNumber = deviceCarNumber;
        putValue(CAR_NUMBER, deviceCarNumber);
    }

    public String getDeviceInstallationCode() {
        return getString(INSTALL_CODE);
        //return deviceInstallationCode;
    }
    public void setDeviceInstallationCode(String deviceInstallationCode) {
        //this.deviceInstallationCode = deviceInstallationCode;
        putValue(INSTALL_CODE, deviceInstallationCode);
    }

    public String getDeviceAndroidUserId() {
        return getString(ANDROID_USER_ID);
        //return deviceAndroidUserId;
    }
    public void setDeviceAndroidUserId(String deviceAndroidUserId) {
        //this.deviceAndroidUserId = deviceAndroidUserId;
        putValue(ANDROID_USER_ID, deviceAndroidUserId);
    }
}
