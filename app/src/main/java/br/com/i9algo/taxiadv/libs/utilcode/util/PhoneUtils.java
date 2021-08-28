package br.com.i9algo.taxiadv.libs.utilcode.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import br.com.i9algo.taxiadv.v2.helpers.ValueMap;

import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.SEND_SMS;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/02
 *     desc  : utils about phone
 * </pre>
 */
public final class PhoneUtils {

    private PhoneUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether the device is phone.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPhone() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    public static boolean isSIMAvailable() {
        TelephonyManager telMgr = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();

        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                return false;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                return false;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                return false;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                return false;
            case TelephonyManager.SIM_STATE_READY:
                return true;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    public static boolean hasTelephony()
    {
        TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null)
            return false;

        PackageManager pm = Utils.getApp().getPackageManager();

        if (pm == null)
            return false;

        boolean retval = false;
        try
        {
            Class<?> [] parameters = new Class[1];
            parameters[0] = String.class;
            Method method = pm.getClass().getMethod("hasSystemFeature", parameters);
            Object [] parm = new Object[1];
            parm[0] = "android.hardware.telephony";
            Object retValue = method.invoke(pm, parm);
            if (retValue instanceof Boolean)
                retval = ((Boolean) retValue).booleanValue();
            else
                retval = false;
        }
        catch (Exception e)
        {
            retval = false;
        }

        return retval;
    }

    /**
     * Return the unique device id.
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the unique device id
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getDeviceId() {
        TelephonyManager tm = getTelephonyManager();
        if (Utils.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        String deviceId = tm.getDeviceId();
        if (!TextUtils.isEmpty(deviceId)) return deviceId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String imei = tm.getImei();
            if (!TextUtils.isEmpty(imei)) return imei;
            String meid = tm.getMeid();
            return TextUtils.isEmpty(meid) ? "" : meid;
        }
        return "";
    }

    /**
     * Return the serial of device.
     *
     * @return the serial of device
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    @RequiresPermission(READ_PHONE_STATE)
    public static String getSerial() {
        String serial = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? Build.getSerial() : Build.SERIAL;
        if (serial != null && serial != "undefined" && !serial.isEmpty())
            return serial;
        else
            return getDeviceSerialNumber();
    }
    private static String getDeviceSerialNumber() {
        String serial = Build.SERIAL;

        if (serial == Build.UNKNOWN) {
            serial = Settings.Secure.getString(Utils.getApp().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        serial = (serial != Build.UNKNOWN || serial != "newer_value") ? serial : null;
        return serial;
    }

    public static String getPhoneNumber() {
        String result = Settings.Secure.ANDROID_ID;
        try {
            if (PhoneUtils.isSIMAvailable() && PhoneUtils.hasTelephony()) {
                if (ActivityCompat.checkSelfPermission(Utils.getApp(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Utils.getApp(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Utils.getApp(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                TelephonyManager telMgr = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
                result = telMgr.getLine1Number();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
        return result;
    }

    /**
     * Return the IMEI.
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the IMEI
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getIMEI() {
        TelephonyManager tm = getTelephonyManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Utils.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            return tm.getImei();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Class clazz = tm.getClass();
                //noinspection unchecked
                Method getImeiMethod = clazz.getDeclaredMethod("getImei");
                getImeiMethod.setAccessible(true);
                String imei = (String) getImeiMethod.invoke(tm);
                if (imei != null) return imei;
            } catch (Exception e) {
                Log.e("PhoneUtils", "getIMEI: ", e);
            }
        }
        String imei = tm.getDeviceId();
        if (imei != null && imei.length() == 15) {
            return imei;
        }
        return "";
    }

    /**
     * Return the IMEI.
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @param slotId of which deviceID is returned
     * @return the IMEI
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getIMEI(int slotId) {
        TelephonyManager tm = getTelephonyManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Utils.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            return tm.getImei(slotId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Class clazz = tm.getClass();
                //noinspection unchecked
                Method getImeiMethod = clazz.getDeclaredMethod("getImei", int.class);
                getImeiMethod.setAccessible(true);
                String imei = (String) getImeiMethod.invoke(tm, slotId);
                if (imei != null) return imei;
            } catch (Exception e) {
                Log.e("PhoneUtils", "getIMEI: ", e);
            }
        }
        return getIMEI();
    }

    /**
     * Return the MEID.
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the MEID
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getMEID() {
        if (Utils.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        TelephonyManager tm = getTelephonyManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tm.getMeid();
        }
        return tm.getDeviceId();
    }

    /**
     * Return the MEID.
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the MEID
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getMEID(final int slotId) {
        if (Utils.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        TelephonyManager tm = getTelephonyManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tm.getMeid(slotId);
        }
        return getMEID();
    }

    /**
     * Return the IMSI.
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the IMSI
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getIMSI() {
        if (Utils.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        TelephonyManager tm = getTelephonyManager();
        return tm.getSubscriberId();
    }

    /**
     * Returns the current phone type.
     *
     * @return the current phone type
     * <ul>
     * <li>{@link TelephonyManager#PHONE_TYPE_NONE}</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_GSM }</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_CDMA}</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_SIP }</li>
     * </ul>
     */
    public static int getPhoneType() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getPhoneType();
    }

    /**
     * Return whether sim card state is ready.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isSimCardReady() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * Return the sim operator name.
     *
     * @return the sim operator name
     */
    public static String getSimOperatorName() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getSimOperatorName();
    }

    /**
     * Return the sim operator using mnc.
     *
     * @return the sim operator
     */
    public static String getSimOperatorByMnc() {
        TelephonyManager tm = getTelephonyManager();
        String operator = tm.getSimOperator();
        if (operator == null) return "";
        switch (operator) {
            case "46000":
            case "46002":
            case "46007":
            case "46020":
                return "中国移动";
            case "46001":
            case "46006":
            case "46009":
                return "中国联通";
            case "46003":
            case "46005":
            case "46011":
                return "中国电信";
            default:
                return operator;
        }
    }

    /**
     * Return the phone status.
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return DeviceId = 99000311726612<br>
     * DeviceSoftwareVersion = 00<br>
     * Line1Number =<br>
     * NetworkCountryIso = cn<br>
     * NetworkOperator = 46003<br>
     * NetworkOperatorName = 中国电信<br>
     * NetworkType = 6<br>
     * PhoneType = 2<br>
     * SimCountryIso = cn<br>
     * SimOperator = 46003<br>
     * SimOperatorName = 中国电信<br>
     * SimSerialNumber = 89860315045710604022<br>
     * SimState = 5<br>
     * SubscriberId(IMSI) = 460030419724900<br>
     * VoiceMailNumber = *86<br>
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getPhoneStatus() {
        if (Utils.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        TelephonyManager tm = getTelephonyManager();
        String str = "";
        //noinspection ConstantConditions
        str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
        str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
        str += "Line1Number = " + tm.getLine1Number() + "\n";
        str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
        str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
        str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
        str += "NetworkType = " + tm.getNetworkType() + "\n";
        str += "PhoneType = " + tm.getPhoneType() + "\n";
        str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
        str += "SimOperator = " + tm.getSimOperator() + "\n";
        str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
        str += "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
        str += "SimState = " + tm.getSimState() + "\n";
        str += "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
        str += "VoiceMailNumber = " + tm.getVoiceMailNumber();
        return str;
    }

    @SuppressLint("NewApi")
    public static List<CellInfo> getAllCellInfo() {
        if (Build.VERSION.SDK_INT < 16)
            return null;

        TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(Utils.getApp(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return tm.getAllCellInfo();
    }

    public static ValueMap getAllTelephonyInfo() {
        ValueMap map = new ValueMap();

        try {
            if (ActivityCompat.checkSelfPermission(Utils.getApp(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            if (isSIMAvailable() && hasTelephony()) {
                TelephonyManager telMgr = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);

                if (telMgr.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                    Object result = ReflectUtils.callMethod("getCellLocation", telMgr);
                    GsmCellLocation cellLocation = (GsmCellLocation) result;
                    if(cellLocation!=null){
                        ValueMap m = new ValueMap();
                        m.put("GsmCid", cellLocation.getCid());
                        m.put("GsmPsc", cellLocation.getPsc());
                        m.put("GsmLac", cellLocation.getLac());
                        map.put("GsmCellLocation", m);
                    }
                }
                map.put("getAllCellInfo", ReflectUtils.callMethod(telMgr, "getAllCellInfo"));
                map.put("getNeighboringCellInfo", ReflectUtils.callMethod(telMgr, "getNeighboringCellInfo"));
                map.put("getCallState", ReflectUtils.callMethod(telMgr, "getCallState"));
                map.put("getCarrierConfig", ReflectUtils.callMethod(telMgr, "getCarrierConfig"));
                map.put("getDataActivity", ReflectUtils.callMethod(telMgr, "getDataActivity"));
                map.put("getDataNetworkType", ReflectUtils.callMethod(telMgr, "getDataNetworkType"));
                map.put("getDataState", ReflectUtils.callMethod(telMgr, "getDataState"));
                map.put("getDeviceSoftwareVersion", ReflectUtils.callMethod(telMgr, "getDeviceSoftwareVersion"));
                map.put("getForbiddenPlmns", ReflectUtils.callMethod(telMgr, "getForbiddenPlmns"));
                map.put("getGroupIdLevel1", ReflectUtils.callMethod(telMgr, "getGroupIdLevel1"));
                map.put("getImei", ReflectUtils.callMethod(telMgr, "getImei"));
                map.put("getLine1Number", ReflectUtils.callMethod(telMgr, "getLine1Number"));
                map.put("getMeid", ReflectUtils.callMethod(telMgr, "getMeid"));
                map.put("getMmsUAProfUrl", ReflectUtils.callMethod(telMgr, "getMmsUAProfUrl"));
                map.put("getMmsUserAgent", ReflectUtils.callMethod(telMgr, "getMmsUserAgent"));
                map.put("getNetworkCountryIso", ReflectUtils.callMethod(telMgr, "getNetworkCountryIso"));
                map.put("getNetworkOperator", ReflectUtils.callMethod(telMgr, "getNetworkOperator"));
                map.put("getNetworkOperatorName", ReflectUtils.callMethod(telMgr, "getNetworkOperatorName"));
                map.put("getNetworkSpecifier", ReflectUtils.callMethod(telMgr, "getNetworkSpecifier"));
                map.put("getNetworkType", ReflectUtils.callMethod(telMgr, "getNetworkType"));
                map.put("getPhoneCount", ReflectUtils.callMethod(telMgr, "getPhoneCount"));
                map.put("getPhoneType", ReflectUtils.callMethod(telMgr, "getPhoneType"));
                map.put("getServiceState", ReflectUtils.callMethod(telMgr, "getServiceState"));
                map.put("getSimCountryIso", ReflectUtils.callMethod(telMgr, "getSimCountryIso"));
                map.put("getSimOperator", ReflectUtils.callMethod(telMgr, "getSimOperator"));
                map.put("getSimOperatorName", ReflectUtils.callMethod(telMgr, "getSimOperatorName"));
                map.put("getSimSerialNumber", ReflectUtils.callMethod(telMgr, "getSimSerialNumber"));
                map.put("getSimState", ReflectUtils.callMethod(telMgr, "getSimState"));
                map.put("getSubscriberId", ReflectUtils.callMethod(telMgr, "getSubscriberId"));
                map.put("getVisualVoicemailPackageName", ReflectUtils.callMethod(telMgr, "getVisualVoicemailPackageName"));
                map.put("getVoiceMailAlphaTag", ReflectUtils.callMethod(telMgr, "getVoiceMailAlphaTag"));
                map.put("getVoiceMailNumber", ReflectUtils.callMethod(telMgr, "getVoiceMailNumber"));
                map.put("getVoiceNetworkType", ReflectUtils.callMethod(telMgr, "getVoiceNetworkType"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Skip to dial.
     *
     * @param phoneNumber The phone number.
     * @return {@code true}: operate successfully<br>{@code false}: otherwise
     */
    public static boolean dial(final String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        if (isIntentAvailable(intent)) {
            Utils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }
        return false;
    }

    /**
     * Make a phone call.
     * <p>Must hold {@code <uses-permission android:name="android.permission.CALL_PHONE" />}</p>
     *
     * @param phoneNumber The phone number.
     * @return {@code true}: operate successfully<br>{@code false}: otherwise
     */
    @RequiresPermission(CALL_PHONE)
    public static boolean call(final String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (isIntentAvailable(intent)) {
            Utils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }
        return false;
    }

    /**
     * Send sms.
     *
     * @param phoneNumber The phone number.
     * @param content     The content.
     * @return {@code true}: operate successfully<br>{@code false}: otherwise
     */
    public static boolean sendSms(final String phoneNumber, final String content) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        if (isIntentAvailable(intent)) {
            intent.putExtra("sms_body", content);
            Utils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }
        return false;
    }

    /**
     * Send sms silently.
     * <p>Must hold {@code <uses-permission android:name="android.permission.SEND_SMS" />}</p>
     *
     * @param phoneNumber The phone number.
     * @param content     The content.
     */
    @RequiresPermission(SEND_SMS)
    public static void sendSmsSilent(final String phoneNumber, final String content) {
        if (TextUtils.isEmpty(content)) return;
        PendingIntent sentIntent = PendingIntent.getBroadcast(Utils.getApp(), 0, new Intent("send"), 0);
        SmsManager smsManager = SmsManager.getDefault();
        if (content.length() >= 70) {
            List<String> ms = smsManager.divideMessage(content);
            for (String str : ms) {
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }

    private static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
    }

    private static boolean isIntentAvailable(final Intent intent) {
        return Utils.getApp()
                .getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }
}
