package br.com.i9algo.taxiadv.v2.event;

public class SetIsDemoEvent {

    private boolean isDemoStatus;
    private int isDemoContentId;

    public SetIsDemoEvent(boolean isDemoStatus, int demoId) {
        this.isDemoStatus = isDemoStatus;
        this.isDemoContentId = demoId;
    }

    public boolean isDemoStatus() {
        return isDemoStatus;
    }
    public void setDemoStatus(boolean demoStatus) {
        isDemoStatus = demoStatus;
    }

    public int getDemoContentId() {
        return isDemoContentId;
    }
    public void setDemoContentId(int demoId) {
        isDemoContentId = demoId;
    }
}
