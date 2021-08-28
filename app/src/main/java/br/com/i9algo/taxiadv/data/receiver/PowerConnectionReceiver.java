package br.com.i9algo.taxiadv.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.v2.event.PowerConnectionEvent;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;

public class PowerConnectionReceiver extends BroadcastReceiver {

	@Inject
	MainThreadBus bus;

	public PowerConnectionReceiver(MainThreadBus bus) {
		this.bus = bus;
		bus.register(this);
	}

	@Inject
	public PowerConnectionReceiver() { }

	public void onReceive(Context context , Intent intent) {
		//Logger.i("@@@", "--> "+intent.getAction());

		((CustomApplication) context.getApplicationContext()).getSchedulerComponent().inject(this);
		bus.post(new PowerConnectionEvent(intent));
	}
}
