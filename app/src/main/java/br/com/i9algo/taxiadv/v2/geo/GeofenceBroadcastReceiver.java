package br.com.i9algo.taxiadv.v2.geo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.GeofencingEvent;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.v2.event.GeofenceEvent;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    @Inject
    MainThreadBus bus;

    public GeofenceBroadcastReceiver(MainThreadBus bus) {
        this.bus = bus;
    }

    @Inject
    public GeofenceBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.e("geo", "onreceive");
        ((CustomApplication) context.getApplicationContext()).getSchedulerComponent().inject(this);
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        String transition = event.getTriggeringGeofences().toString() + "\n" + event.getTriggeringLocation().toString();
        Logger.e("geo", transition);
        bus.post(new GeofenceEvent(Integer.parseInt(event.getTriggeringGeofences().get(0).getRequestId())));
    }
}
