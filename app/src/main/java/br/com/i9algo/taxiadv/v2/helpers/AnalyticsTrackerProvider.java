package br.com.i9algo.taxiadv.v2.helpers;

import android.os.Bundle;
import android.content.Context;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseUser;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.segment.analytics.Properties;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;
import br.com.i9algo.taxiadv.domain.models.Device;
import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.domain.constants.Constants;
import br.com.i9algo.taxiadv.domain.enums.Analytics;
import br.com.i9algo.taxiadv.v2.event.NewSessionEvent;
import br.com.i9algo.taxiadv.v2.models.DeviceUser;
import br.com.i9algo.taxiadv.v2.utils.Pref;

public final class AnalyticsTrackerProvider {

    private static final String LOG_TAG = "AnalyticsTracker";

    public static void sendTap(final Context context, final Analytics.CategoryName category, final Analytics.ActionsName action, final String label, final String value) {
        sendTap(context, category, action, label, value, null);
    }
    public static void sendTap(final Context context, final Analytics.CategoryName category, final Analytics.ActionsName action, final String label, final long value) {
        sendTap(context, category, action, label, value, null);
    }

    public static void sendTap(final Context context,
                               final Analytics.CategoryName category,
                               final Analytics.ActionsName action,
                               final String label,
                               final Object value,
                               final Map<String, Object> mapParams) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) { }

                try {
                    Map<String, Object> map = mapParams;
                    if (map == null) {
                        map = new HashMap<String, Object>();
                    }
                    Device device = ((CustomApplication) context.getApplicationContext()).getDevice();
                    map.putAll(device);
                    map.put(FirebaseAnalytics.Param.LOCATION, LocationUtils.getLastLocation(context));


                    /*************************************************
                     ##### segment.com tracker
                     *************************************************/
                    Properties props = new Properties();
                    props.putAll(map);
                    props.putCategory(category.toString());
                    props.putName(label);
                    ((CustomApplication)context.getApplicationContext()).segmentAnalytics.track(action.toString(), props);
                    /*************************************************/

                    // firebase analytics
                    Bundle bundleFbAnalytics = new Bundle();
                    for(Map.Entry<String, Object> entry : map.entrySet()) {
                        String k = entry.getKey();
                        Object v = entry.getValue();

                        //mapToMixpanel.put(k, v); // MAP to mixpanel

                        if (k != null && v != null)
                            bundleFbAnalytics.putString(k, v.toString());
                    }
                    bundleFbAnalytics.putString("CONTENT_ID", String.valueOf(value));
                    bundleFbAnalytics.putString(FirebaseAnalytics.Param.ITEM_ID, label);
                    bundleFbAnalytics.putString(FirebaseAnalytics.Param.ITEM_NAME, label);
                    bundleFbAnalytics.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category.toString());
                    bundleFbAnalytics.putString(FirebaseAnalytics.Param.CONTENT_TYPE, action.toString());
                    bundleFbAnalytics.putString(FirebaseAnalytics.Param.LOCATION, LocationUtils.getLastLocation(context));
                    bundleFbAnalytics.putString(FirebaseAnalytics.Param.GROUP_ID, Pref.getNetworkContentGroup());
                    bundleFbAnalytics.putString(FirebaseAnalytics.Param.COUPON, label);

                    FirebaseAnalytics fbAnalytics = ((CustomApplication)context.getApplicationContext()).getFirebaseAnalytics(context);
                    fbAnalytics.setUserId(userSessionId);
                    fbAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleFbAnalytics);
                    fbAnalytics.logEvent(action.toString(), bundleFbAnalytics);
                    //fbAnalytics.resetAnalyticsData();

                    // Track mixpanel
                    sendMixPanelEvent(context, action.toString(), map);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    private static void sendMixPanelEvent(Context context, String s, Map<String, Object> mapProps) {
        final MixpanelAPI mixpanel = ((CustomApplication) context.getApplicationContext()).getMixpanelAPI();
        mixpanel.setGroup(Constants.GROUP, Constants.GROUP_ID);
        mixpanel.trackMap(s, mapProps);
    }

    public static void loginMixPanel(Context context) {
        Device device = ((CustomApplication) context.getApplicationContext()).getDevice();

        MixpanelAPI mixpanel = ((CustomApplication) context.getApplicationContext()).getMixpanelAPI();
        mixpanel.identify(device.getDeviceId());

        MixpanelAPI.People people = mixpanel.getPeople();
        people.identify(device.getDeviceId());

        people.set("$uuid", DeviceUser.getUid());
        people.set("$email", DeviceUser.getEmail());
        people.set("$username", DeviceUser.getUsername());

        //try {
            people.setMap(device);
            mixpanel.registerSuperPropertiesMap(device);

        /*} catch (JSONException e) {
            Logger.e(LOG_TAG, "Unable to add properties to JSONObject", e);
        }*/
    }

    private static String userSessionId = "";

    public static void sendNewSession(Context context, NewSessionEvent event){
        //Logger.i(LOG_TAG, "sendTap - sendNewSession");

        userSessionId = event.getSessionId();

        sendTap(context, Analytics.CategoryName.UI, Analytics.ActionsName.NEW_SESSION_USER, "NewSessionId", event.getSessionId());
    }

    public static void sendClockTap(Context context){
        //Logger.i(LOG_TAG, "sendTap - sendClockTap");

        Analytics.ActionsName action = Analytics.ActionsName.TOCOU_RELOGIO;

        sendTap(context, Analytics.CategoryName.UI, Analytics.ActionsName.TOCOU_RELOGIO, "Tocou no relogio", 1);
    }

    public static void sendSlideVislualisedNotification(Context context, String slidetitle, int slideId) {
        sendSlideVislualisedNotification(context, slidetitle, slideId, 10);
    }
    public static void sendSlideVislualisedNotification(Context context, String slidetitle, int slideId, long time) {
        //Logger.i(LOG_TAG, "sendTap - sendSlideVislualisedNotification");

        Analytics.ActionsName action = Analytics.ActionsName.VISUALIZOU_PROPAGANDA;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Analytics.Keys.slideTitle.toString(), slidetitle);
        map.put(Analytics.Keys.slideID.toString(), String.valueOf(slideId));

        sendTap(context, Analytics.CategoryName.CONTENT, action, slidetitle + " - " + slideId, time, map);
    }

    public static void sendAnyInteraction(Context context, String slidetitle, int slideId) {
        //Logger.i(LOG_TAG, "sendTap - sendAnyInteraction - " + slidetitle);

        Analytics.ActionsName action = Analytics.ActionsName.TOCOU_TELA;

        Map<String, Object> map = new HashMap<String, Object>();
        //map.put("Cliente", "");
        map.put(Analytics.Keys.slideTitle.toString(), slidetitle);
        map.put(Analytics.Keys.slideID.toString(), String.valueOf(slideId));

        sendTap(context, Analytics.CategoryName.CONTENT, Analytics.ActionsName.TOCOU_TELA, slidetitle + " - " + slideId, slideId, map);
    }

    public static void sendSlideTap(Context context, String slidetitle, int slideId) {
        //Logger.i(LOG_TAG, "sendTap - sendSlideTap - " + slidetitle);

        Analytics.ActionsName action = Analytics.ActionsName.TOCOU_SLIDESHOW;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Analytics.Keys.slideTitle.toString(), slidetitle);
        map.put(Analytics.Keys.slideID.toString(), String.valueOf(slideId));

        sendTap(context, Analytics.CategoryName.CONTENT, action, slidetitle, slideId, map);
    }

    public static void sendCategoryNavigationTap(Context context, String categorytitle, int id) {
        //Logger.i(LOG_TAG, "sendTap - sendCategoryNavigationTap");

        Analytics.ActionsName action = Analytics.ActionsName.VISUALIZOU_CATEGORIA;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Analytics.Keys.categoryTitle.toString(), categorytitle);
        map.put(Analytics.Keys.categoryID.toString(), String.valueOf(id));

        sendTap(context, Analytics.CategoryName.CONTENT, action, categorytitle, id, map);
    }

    public static void sendGridViewItemTap(Context context, int category, String sidebarName, int sidebarId) {
        //Logger.i(LOG_TAG, "sendTap - sendGridViewItemTap");

        Analytics.ActionsName action = Analytics.ActionsName.VISUALIZOU_SIDEBAR;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Analytics.Keys.categoryID.toString(), String.valueOf(category));
        map.put(Analytics.Keys.sidebarName.toString(), sidebarName);
        map.put(Analytics.Keys.sidebarID.toString(), String.valueOf(sidebarId));

        sendTap(context, Analytics.CategoryName.CONTENT, action, sidebarName, sidebarId, map);
    }

    public static void sendReservarTap(Context context, int category, int sidebarId) {
        //Logger.i(LOG_TAG, "sendTap - sendReservarTap");

        Analytics.ActionsName action = Analytics.ActionsName.TOCOU_RESERVAR;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Analytics.Keys.categoryID.toString(), String.valueOf(category));
        map.put(Analytics.Keys.sidebarID.toString(), String.valueOf(sidebarId));

        sendTap(context, Analytics.CategoryName.CONTENT, action, "category_id: " + category + " sidebar_id " + sidebarId, sidebarId, map);
    }

    public static void sendReservarConfirmationTap(Context context, int category, int sidebarId) {
        //Logger.i(LOG_TAG, "sendTap - sendReservarConfirmationTap");

        Analytics.ActionsName action = Analytics.ActionsName.CONFIRMOU_RESERVAR;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Analytics.Keys.categoryID.toString(), String.valueOf(category));
        map.put(Analytics.Keys.sidebarID.toString(), String.valueOf(sidebarId));

        sendTap(context, Analytics.CategoryName.CONTENT, action, "category_id: " + category + " sidebar_id " + sidebarId, sidebarId, map);
    }

    public static void sendShareTap(Context context, int category, int sidebarId) {
        //Logger.i(LOG_TAG, "sendTap - sendReservarTap");

        Analytics.ActionsName action = Analytics.ActionsName.TOCOU_COMPARTILHAR;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Analytics.Keys.categoryID.toString(), String.valueOf(category));
        map.put(Analytics.Keys.sidebarID.toString(), String.valueOf(sidebarId));

        sendTap(context, Analytics.CategoryName.CONTENT, action, "category_id: " + category + " sidebar_id " + sidebarId, sidebarId, map);
    }

    public static void sendShareMethodTap(Context context, int category, int sidebarId, String shareMethod) {
        //Logger.i(LOG_TAG, "sendTap - sendShareMethodTap");

        Analytics.ActionsName action = Analytics.ActionsName.TOCOU_METODO_COMPARTILHAR;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Analytics.Keys.categoryID.toString(), String.valueOf(category));
        map.put(Analytics.Keys.sidebarID.toString(), String.valueOf(sidebarId));
        map.put(Analytics.Keys.shareMethod.toString(), shareMethod);

        sendTap(context, Analytics.CategoryName.CONTENT, action, "category_id: " + category + " sidebar_id: " + sidebarId + "method: " + shareMethod, 0, map);
    }

    public static void sendShareConfirmTap(Context context, int category, int sidebarId, String shareMethod) {
        //Logger.i(LOG_TAG, "sendTap - sendShareConfirmTap");

        Analytics.ActionsName action = Analytics.ActionsName.TOCOU_METODO_COMPARTILHAR;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Analytics.Keys.categoryID.toString(), String.valueOf(category));
        map.put(Analytics.Keys.sidebarID.toString(), String.valueOf(sidebarId));
        map.put(Analytics.Keys.shareMethod.toString(), shareMethod);

        sendTap(context, Analytics.CategoryName.CONTENT, action, "category_id: " + category + " sidebar_id: " + sidebarId + "method: " + shareMethod, 0, map);
    }
}
