package br.com.i9algo.taxiadv.v2.event;

import android.content.Intent;

public class PowerConnectionEvent extends BasicEvent {

    public Intent intent;

    public PowerConnectionEvent(Intent intent) {
        this.intent = intent;
    }
}
