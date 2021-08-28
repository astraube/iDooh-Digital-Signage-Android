package br.com.i9algo.taxiadv.v2.injection.provides;

import android.util.Log;


import java.util.Calendar;

import javax.inject.Singleton;

import br.com.i9algo.taxiadv.v2.event.NewSessionEvent;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;

@Singleton
public class Session {

    private String sessionIdPrefix;
    private String sessionId;

    public Session(String prefix) {
        this.sessionIdPrefix = prefix;
        //Log.v(getClass().getSimpleName(), "-----> new Session()");
    }

    public void setSessionIdPrefix(String prefix) { this.sessionIdPrefix = prefix; }
    public String getSessionId() { return this.sessionId; }

    public void newSession() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(sessionIdPrefix);
        stringBuffer.append("_");
        stringBuffer.append(  String.valueOf(Calendar.getInstance().getTimeInMillis()) );
        this.sessionId = stringBuffer.toString();
        Logger.v(getClass().getSimpleName(), "-----> NEW SESSION ID: " + this.sessionId);

        MainThreadBus.getInstance().post(new NewSessionEvent(this.sessionId));
    }
}
