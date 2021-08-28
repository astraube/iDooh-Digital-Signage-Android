package br.com.i9algo.taxiadv.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.libs.utilcode.util.NetworkUtils;

import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.v2.download.DownloadHelperDAO;
import br.com.i9algo.taxiadv.v2.download.DownloadItem;
import br.com.i9algo.taxiadv.v2.event.DownloadRetryListEvent;
import br.com.i9algo.taxiadv.v2.helpers.defaults.DefaultSubscriber;
import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 23/01/2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Inject
    DownloadHelperDAO dao;

    @Inject
    MainThreadBus bus;

    Subscription getDownloadItemsSubscriber;

    boolean lastStatus = false;

    private final String LOG = "NetworkChangeReceiver";


    public NetworkChangeReceiver(MainThreadBus bus) {
        this.bus = bus;
    }

    @Inject
    public NetworkChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent i) {
        ((CustomApplication) context.getApplicationContext()).getSchedulerComponent().inject(this);
        if (!lastStatus && NetworkUtils.isConnected()) {
            final Context finalContext = context;
            getDownloadItemsSubscriber =
                    dao.getFailedItems()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DefaultSubscriber<List<DownloadItem>>() {
                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    e.printStackTrace();
                                    Log.e(LOG, "getFailedItems.onErroring");
                                    getDownloadItemsSubscriber.unsubscribe();
                                }

                                @Override
                                public void onNext(List<DownloadItem> object) {
                                    super.onNext(object);
                                    Log.e(LOG, "getFailedItems.onNexting");
                                    DownloadRetryListEvent event = new DownloadRetryListEvent(object);
                                    getDownloadItemsSubscriber.unsubscribe();
                                    bus.post(event);
                                }

                                @Override
                                public void onCompleted() {
                                    Log.e(LOG, "getFailedItems.onCompleted");
                                    getDownloadItemsSubscriber.unsubscribe();
                                }
                            });
        }
        lastStatus = NetworkUtils.isConnected();
    }


}

