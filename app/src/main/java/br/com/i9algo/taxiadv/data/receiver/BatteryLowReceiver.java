package br.com.i9algo.taxiadv.data.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.v2.network.taxiadv.AdvService;
import br.com.i9algo.taxiadv.libs.utilcode.util.NetworkUtils;
import br.com.i9algo.taxiadv.v2.utils.SoundUtil;
import rx.Subscription;

public class BatteryLowReceiver extends BroadcastReceiver {

    private final String LOG_TAG = getClass().getSimpleName();

    @Inject
    AdvService service;

    @Inject
    public BatteryLowReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ((CustomApplication) context.getApplicationContext()).getSchedulerComponent().inject(this);

        Log.e(LOG_TAG, "onReceive");

        SoundUtil.playSoundBattery(context);

        if (!NetworkUtils.isConnected()) {
            return;
        }

        Log.e(LOG_TAG, "isOnline. About to send Info");
    }
}
