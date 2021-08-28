package br.com.i9algo.taxiadv.v2.event;

public class ResizePlaylistEvent {

    private ResizeEventType mType;

    public enum ResizeEventType {
        MINIMIZE,
        MAXIMIZE
    }

    public ResizePlaylistEvent(ResizeEventType type) {
        this.mType = type;
    }

    public ResizeEventType getType() {
        return this.mType;
    }
}
