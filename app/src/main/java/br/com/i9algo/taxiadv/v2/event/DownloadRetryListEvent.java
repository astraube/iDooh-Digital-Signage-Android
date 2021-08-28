package br.com.i9algo.taxiadv.v2.event;

import java.util.List;

import br.com.i9algo.taxiadv.v2.download.DownloadItem;

public class DownloadRetryListEvent extends BasicEvent {

    private List<DownloadItem> thingsToDownload;

    public DownloadRetryListEvent(List<DownloadItem> thingsToDownload) {
        this.thingsToDownload = thingsToDownload;
    }

    public List<DownloadItem> getThingsToDownload() {
        return thingsToDownload;
    }

    public void setThingsToDownload(List<DownloadItem> thingsToDownload) {
        this.thingsToDownload = thingsToDownload;
    }

    public DownloadRetryListEvent() {
        super();
    }
}
