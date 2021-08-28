package br.com.i9algo.taxiadv.v2.event;

import android.content.Context;

import java.util.List;

import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;

public class DownloadListEvent extends BasicEvent {

    private List<SlideshowItem> thingsToDownload;

    private Context context;

    public DownloadListEvent(List<SlideshowItem> thingsToDownload, Context context) {
        this.thingsToDownload = thingsToDownload;
        this.context = context;
    }

    public List<SlideshowItem> getThingsToDownload() {
        return thingsToDownload;
    }

    public void setThingsToDownload(List<SlideshowItem> thingsToDownload) {
        this.thingsToDownload = thingsToDownload;
    }

    public Context getContext() {
        return context;
    }

    public DownloadListEvent() {
        super();
    }
}
