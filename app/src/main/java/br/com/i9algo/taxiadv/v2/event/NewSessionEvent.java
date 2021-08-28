package br.com.i9algo.taxiadv.v2.event;

public class NewSessionEvent {

    private String sessionId;

    public NewSessionEvent(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }
}
