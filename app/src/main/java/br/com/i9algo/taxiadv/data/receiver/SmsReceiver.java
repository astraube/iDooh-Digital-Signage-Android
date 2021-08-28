package br.com.i9algo.taxiadv.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import br.com.i9algo.taxiadv.v2.helpers.Logger;


public class SmsReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = SmsReceiver.class.getSimpleName();

    public enum Command {

        SMS_SEND_LOCATION("<SMS_SL>"), // responder a SMS com a localizacao do device
        API_SEND_LOCATION("<API_SL>");

        private String stringValue;

        Command(String toString) {
            stringValue = toString;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
        Bundle extras = intent.getExtras();

        if (extras == null)
            return;

        Object[] pdus = (Object[]) extras.get("pdus");

        try {
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String fromAddress = message.getOriginatingAddress();
                String messageBody = message.getMessageBody().toString();

                Logger.i(LOG_TAG, "From: " + fromAddress + " message: " + messageBody);

                String keyIdentify = "[IDOOH]";
                String msgBodyUpperCase = messageBody.toUpperCase();

                if (msgBodyUpperCase.contains(keyIdentify)) {
                    String command = msgBodyUpperCase.substring(keyIdentify.length()).trim();

                    //Logger.i(LOG_TAG, "-----> " + command);

                    addCommand(context, intent, fromAddress, command);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            Logger.e("Exception caught",e.getMessage());
        }
    }

    private void addCommand(Context context, Intent intent, String fromAddress, String command) {
        if (command.equals(Command.SMS_SEND_LOCATION.toString())) {
            //Logger.i(LOG_TAG, "-----> " + Command.SMS_SEND_LOCATION);

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(fromAddress, null, "0.0, 0.0", null, null);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        if (command.equals(Command.API_SEND_LOCATION.toString())) {
            //Logger.i(LOG_TAG, "-----> " + Command.API_SEND_LOCATION);
            // TODO - comentado temporariamente
            /*try {
                String locationStr = LocationUtils.getLocationLastUpdate(context);
                GeoCoordinate location = LocationUtils.getLocationByString(context, locationStr);
                IdoohMediaDeviceController.sendGeopoint(location);
            } catch(Exception e){
                e.printStackTrace();
            }*/
        }
    }
}