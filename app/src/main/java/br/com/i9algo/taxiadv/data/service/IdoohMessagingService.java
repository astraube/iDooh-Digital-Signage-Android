package br.com.i9algo.taxiadv.data.service;

import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import br.com.i9algo.taxiadv.domain.models.Device;
import br.com.i9algo.taxiadv.libs.utilcode.util.AppUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.ShellUtils;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.helpers.RemoteActions;

public class IdoohMessagingService extends FirebaseMessagingService {

    private static final String LOG_TAG = IdoohMessagingService.class.getSimpleName();

    private static final String PAYLOAD_CMD_ADB = "PAYLOAD_CMD_ADB"; // EX: "input keyevent 26, am start -a android.intent.action.MAIN -n com.android.settings/.wifi.WifiSettings"
    private static final String PAYLOAD_ACTION = "PAYLOAD_ACTION";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Logger.e(LOG_TAG, "From: " + remoteMessage);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> payload =  remoteMessage.getData();
            Logger.e(LOG_TAG, "Message data getData: " + payload);
            //Logger.e(LOG_TAG, "Message data getFrom: " + remoteMessage.getFrom());
            //Logger.e(LOG_TAG, "Message data getMessageId: " + remoteMessage.getMessageId());
            //Logger.e(LOG_TAG, "Message data getMessageType: " + remoteMessage.getMessageType());
            //Logger.e(LOG_TAG, "Message data getTo: " + remoteMessage.getTo());

            if (payload.containsKey(PAYLOAD_CMD_ADB)) {
                String command = payload.get(PAYLOAD_CMD_ADB);
                if (TextUtils.isEmpty(command))
                    return;
                String[] acts = TextUtils.split(command, ",");
                if (acts != null && acts.length > 0) {

                    Logger.e(LOG_TAG, "Comando ADB--> " + acts.length);
                    //Logger.e(LOG_TAG, "Comando ADB--> " + acts[0]);

                    ShellUtils.CommandResult result = ShellUtils.execCmd(acts, true);
                    if (result.result == 0) {
                        String successMsg = result.successMsg;
                        Logger.e(LOG_TAG, "successMsg--> " + successMsg);
                    }
                } else {
                    Logger.e(LOG_TAG, "Apenas um comando ADB--> " + command);
                }
                return;

            } else if (payload.containsKey(PAYLOAD_ACTION)) {
                String action = payload.get(PAYLOAD_ACTION);
                if (TextUtils.isEmpty(action))
                    return;

                switch (action.toUpperCase()) {
                    case "SAVE_PRINT_SCREEN":
                        RemoteActions.saveDevicePrintScreen();
                        break;
                    case "SAVE_PRINT_SCREEN_ACTIVITY":
                        RemoteActions.saveActivityPrintScreen();
                        break;
                    case "APP_CLEAN":
                        RemoteActions.cleanAllData(true);
                        break;
                    case "APP_RESTART":
                        RemoteActions.appRestart();
                        break;
                    case "DEVICE_REBOOT":
                        RemoteActions.deviceReboot();
                        break;
                }
                return;
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Logger.e(LOG_TAG, "Message Notification getBody: " + remoteMessage.getNotification().getBody());
            //Logger.e(LOG_TAG, "Message Notification getTitle: " + remoteMessage.getNotification().getTitle());
            //Logger.e(LOG_TAG, "Message Notification getChannelId: " + remoteMessage.getNotification().getChannelId());
            //Logger.e(LOG_TAG, "Message Notification getTag: " + remoteMessage.getNotification().getTag());
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Logger.e(LOG_TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param refreshToken The new token.
     */
    private void sendRegistrationToServer(String refreshToken) {
        Logger.e(LOG_TAG, "Firebase refreshToken: " + refreshToken);

        Device.getInstance().setFCMToken(refreshToken);
    }


}