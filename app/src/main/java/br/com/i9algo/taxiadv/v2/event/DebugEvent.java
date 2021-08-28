package br.com.i9algo.taxiadv.v2.event;

public class DebugEvent {

    public String debugText;

    public DebugEvent(String text) {
        this.debugText = text + "\n";
    }
}
