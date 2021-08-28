package br.com.i9algo.taxiadv.v2.download;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.domain.constants.Constants;
import br.com.i9algo.taxiadv.v2.event.DownloadListEvent;
import br.com.i9algo.taxiadv.v2.event.DownloadRetryListEvent;
import br.com.i9algo.taxiadv.v2.helpers.defaults.DefaultSubscriber;
import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BatchDownloadService extends IntentService {

    private final String LOG = "BatchDownload";

    @Inject
    MainThreadBus bus;

    @Inject
    DownloadHelperDAO dao;

    String servicestring = Context.DOWNLOAD_SERVICE;
    DownloadManager downloadmanager;

    Subscription getDownloadItemsSubscriber;

    Context context;

    public BatchDownloadService() {
        super("BatchDownloadService");
    }

    @Inject
    public BatchDownloadService(MainThreadBus bus) {
        super("BatchDownloadService");
        this.bus = bus;
        bus.register(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((CustomApplication) getApplicationContext()).getSchedulerComponent().inject(this);
        bus.register(this);

    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Subscribe
    public void receiveDownloadList(DownloadListEvent event) {
        Log.e(LOG, "receiveDownloadList");
        context = event.getContext();
        ((CustomApplication) context).getSchedulerComponent().inject(this);

        if (downloadmanager == null) {
            Log.e(LOG, "downloadmanager is null");
            downloadmanager = (DownloadManager) context.getSystemService(servicestring);
        }

        if (event.getThingsToDownload() != null) {
            Log.e(LOG, "there are things to download");
            downloadList(event.getThingsToDownload());
        }

    }

    private void downloadList(final List<SlideshowItem> listToDownload) {
        Log.e(LOG, "downloadList.starting");
        getDownloadItemsSubscriber =
                dao.getItems()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultSubscriber<List<DownloadItem>>() {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                e.printStackTrace();
                                Log.e(LOG, "downloadList.onErroring");
                                getDownloadItemsSubscriber.unsubscribe();
                            }

                            @Override
                            public void onNext(List<DownloadItem> object) {
                                super.onNext(object);

                                List<SlideshowItem> list = new ArrayList<SlideshowItem>();
                                Map<Integer, DownloadItem> downloadMaps = new HashMap<Integer, DownloadItem>();
                                for (DownloadItem i : object)
                                    downloadMaps.put(i.getSlideId(), i);

                                for (SlideshowItem item : listToDownload) {
                                    if (downloadMaps.containsKey(item.getId())) {
                                        //there is a download task for this slide
                                        DownloadItem dlitem = downloadMaps.get(item.getId());
                                        if (dlitem.getStatus() == Constants.DOWNLOAD_ERROR) {
                                            list.add(item); //shit hit the fan on this download, do it again
                                        } // else do nothing because it's either still downloading or it's already downloaded
                                    } else { // app has NOT attempted to download this item
                                        list.add(item); //download it!
                                    }
                                }

                                getDownloadItemsSubscriber.unsubscribe();
                                BatchDownloadService.this.downloadItemsPerList(list);
                            }

                            @Override
                            public void onCompleted() {
                                Log.e(LOG, "downloadList.onCompleted");
                                getDownloadItemsSubscriber.unsubscribe();
                            }
                        });


    }

    private void downloadItemsPerList(List<SlideshowItem> list) {
        List<DownloadItem> dlitemsToInsert = new ArrayList<>();
        for (SlideshowItem item : list) {
            Uri uri = Uri
                    .parse(item.getMainImage());
            Log.e(LOG, "downloadItem - attempting to download " + item.getMainImage());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            Long reference = downloadmanager.enqueue(request);
            dlitemsToInsert.add(new DownloadItem(item.getId(), item.getMainImage(), Integer.parseInt(Long.toString(reference)), "DOWNLOADING", Constants.DOWNLOAD_STARTING));
        }
        dao.insertList(dlitemsToInsert);
    }

    @Subscribe
    public void receiveDownloadRetryList(DownloadRetryListEvent event) {
        ((CustomApplication) context).getSchedulerComponent().inject(this);
        if (event.getThingsToDownload() != null) {
            if (downloadmanager == null) {
                Log.e(LOG, "downloadmanager is null");
                downloadmanager = (DownloadManager) context.getSystemService(servicestring);
            }
            Log.e(LOG, "there are things to download");
            retryDownload(event.getThingsToDownload());
        }
    }

    private void retryDownload(List<DownloadItem> thingsToDownload) {
        for (DownloadItem item : thingsToDownload) {
            Uri uri = Uri
                    .parse(item.getOriginalURL());
            Log.e(LOG, "retryDownload - attempting to download " + item.getOriginalURL());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            Long reference = downloadmanager.enqueue(request);
            DownloadItem newDownloadItem = new DownloadItem(item.getSlideId(), item.getOriginalURL(), Integer.parseInt(Long.toString(reference)), item.getFileLocation(), Constants.DOWNLOAD_STARTING);
            dao.insert(newDownloadItem);
        }
    }

}
