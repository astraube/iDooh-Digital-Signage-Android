package br.com.i9algo.taxiadv.v2.presenters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.ui.recyclers.adapter.CategoryAdapter;
import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.ui.fragments.AboutFragment;
import br.com.i9algo.taxiadv.ui.fragments.GrigFragmentDelegate;
import br.com.i9algo.taxiadv.v2.event.GeofenceEvent;
import br.com.i9algo.taxiadv.v2.helpers.AnalyticsTrackerProvider;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.helpers.defaults.DefaultSubscriber;
import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;
import br.com.i9algo.taxiadv.v2.models.DeviceUser;
import br.com.i9algo.taxiadv.v2.models.inbound.Category;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeofencedAdvert;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarItem;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowPlaylist;
import br.com.i9algo.taxiadv.v2.models.viewmodel.GridItemViewModel;
import br.com.i9algo.taxiadv.v2.network.taxiadv.AdvService;
import br.com.i9algo.taxiadv.v2.injection.provides.AppStateManager;
import br.com.i9algo.taxiadv.v2.injection.provides.LocationStateManager;
import br.com.i9algo.taxiadv.v2.injection.provides.ProgrammingState;
import br.com.i9algo.taxiadv.v2.storage.GeofencedAdvertDAO;
import br.com.i9algo.taxiadv.v2.views.slideshow.AdvertSlideshowViewInterface;
import br.com.i9algo.taxiadv.v2.views.slideshow.maintenance.MaintenanceFragment;
import br.com.i9algo.taxiadv.v2.views.widgets.playlistview.PlaylistViewV2;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AdvertSlideshowPresenter extends BasePresenter<AdvertSlideshowViewInterface> implements
        CategoryAdapter.OnClickDelegate,
        PlaylistViewV2.PlaylistViewDelegate,
        GrigFragmentDelegate,
        AboutFragment.AboutFragmentDelegate,
        MaintenanceFragment.Delegate {


    private final String LOG_TAG = getClass().getSimpleName();

    @Inject
    ProgrammingState programmingState;

    @Inject
    AppStateManager appStateManager;
    @Inject
    AdvService advService;

    @Inject
    MainThreadBus bus;

    @Inject
    GeofencedAdvertDAO geofencedAdvertDAO;

    @Inject
    LocationStateManager locationStateManager;

    Subscription receiveGeofenceSubscription;
    Subscription getPlaylistSubscription;

    @Inject
    public AdvertSlideshowPresenter() {
        super();
    }

    @Override
    public void bindView(AdvertSlideshowViewInterface view) {
        super.bindView(view);
        bus.register(this);
    }

    @Override
    public void delegateNavigationClickAction(Category item) {
        Log.v(LOG_TAG, "@@@ delegateNavigationClickAction: " + item.getName());
        if (click(item.getTableName(), item.getId())) {
            showNavigationClickAction(item);
        }
    }

    public void showNavigationClickAction(Category item) {
        Log.v(LOG_TAG, "@@@ showNavigationClickAction: " + item.getName());
        AnalyticsTrackerProvider.sendCategoryNavigationTap(view.getContext(), item.getName(), item.getId());

        view.showGridViewSidebar(item);
    }

    public Observable<SlideshowPlaylist> getCurrentPlaylist(String date, int timeintervalstart, int timeintervalend) {
        Log.e("Adv", "presenter:getDefaultPlaylist");
        return programmingState.getCurrentPlaylist(date, timeintervalstart, timeintervalend);
    }

    public Observable<List<SlideshowItem>> getFillers() {
        Log.e("Adv", "presenter:getFillers");
        return programmingState.getFillers();
    }

    @Override
    public void delegateClickAction(SlideshowItem item) {
        //Logger.d(LOG_TAG, "### delegateClickAction - " + item.getActionModel());

        //item.setActionModel("CATEGORY");
        //item.setActionModelId(1);
        //view.delegateActionSlide(item);

        if (click(SlideshowItem.TABLE, item.getId())) {
            AnalyticsTrackerProvider.sendSlideTap(view.getContext(), item.getTitle(), item.getId());

            if (!TextUtils.isEmpty(item.getActionModel()) && item.getActionModelId() > -1) {
                view.delegateActionSlide(item);
            } else {
                hideAllSidebars();
            }
        }
    }

    @Override
    public void delegateReloadPlaylistAction() {
        view.reloadPlaylist();
    }

    @Override
    public int getAudioLevel() {
        return view.getCurrentSoundLevel();
    }

    private void hideAllSidebars() {
        view.hideAllSidebarFragments();
    }

    public void clockAction() {
        if (click()) {
            AnalyticsTrackerProvider.sendClockTap(view.getContext());
            view.showAboutFragment();
        }
    }

    @Override
    public void delegateShowItem(int category, GridItemViewModel advert) {
        if (click("GridItemViewModel", advert.getSidebarId())) {
            AnalyticsTrackerProvider.sendGridViewItemTap(view.getContext(), category, advert.getItemName(), advert.getSidebarId());
            view.showSidebarFragment(category, advert.getSidebarId());
        }
    }

    @Override
    public void switchDemoDisplayMode() {

    }

    /*@Override
    public void showGeo() {
        view.showGeofenceAdvert(new GeofencedAdvert(1, "afe", "afe", new GeographyData()));
    }*/

    @Override
    public void handleCarPlate(String placa) {
        advService.sendCarPlate(DeviceUser.getUid(), placa)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e("Adv", "handleCarPlate:onError(Throwable e)");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object geofencedAdvert) {
                        super.onNext(geofencedAdvert);
                        Log.e("Adv", "handleCarPlate:onNext()");
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("Adv", "handleCarPlate:onCompleted()");

                    }
                });
    }

    @Override
    public List<SlideshowItem> getPlaylistFromPlaylistView() {
        return view.getPlaylistFromPlaylistView();
    }

    @Override
    public void handlePlaylistRefresh() {
        view.showLoadingScreen();
        getPlaylistSubscription = appStateManager.initProgrammingState().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e("Adv", "handlePlaylistRefresh:onError(Throwable e)");
                        e.printStackTrace();
                        view.showLoadingErrorScreen();
                        getPlaylistSubscription.unsubscribe();
                    }

                    @Override
                    public void onNext(Object geofencedAdvert) {
                        super.onNext(geofencedAdvert);
                        Log.e("Adv", "handlePlaylistRefresh:onNext()");
                        if (view.isLoading()) {
                            view.showLoadingSuccessfulScreen();
                        }
                        getPlaylistSubscription.unsubscribe();
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("Adv", "handlePlaylistRefresh:onCompleted()");
                        if (view.isLoading()) {
                            view.showLoadingSuccessfulScreen();
                        }
                        getPlaylistSubscription.unsubscribe();

                    }
                });
    }

    @Override
    public void showMaintenanceFragment() {
        view.showMaintenanceFragment();
    }

    @Override
    public void showFloat() {
        view.showFloat();
    }

    @Subscribe
    public void receiveGeofenceEvent(GeofenceEvent event) {
        if (receiveGeofenceSubscription != null) {
            receiveGeofenceSubscription.unsubscribe();
        }
        view.writeOnDebug("Passou pelo geofence id " + Integer.toString(event.getGeofenceid()));
        receiveGeofenceSubscription = geofencedAdvertDAO.getGeofencedAdvert(event.getGeofenceid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<GeofencedAdvert>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e("Adv", "receiveGeofenceEvent:onError(Throwable e)");
                        e.printStackTrace();
                        //view.showGeofenceAdvert(new GeofencedAdvert(1, "afe", "afe", new GeographyData()));
                    }

                    @Override
                    public void onNext(GeofencedAdvert geofencedAdvert) {
                        super.onNext(geofencedAdvert);
                        Log.e("Adv", "receiveGeofenceEvent:onNext()");
                        view.showGeofenceAdvert(geofencedAdvert);
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("Adv", "receiveGeofenceEvent:onCompleted()");

                    }
                });

    }

    @Override
    public void sendToAndroidConfigMenu() {
        view.sendToAndroidConfigMenu();
    }

    @Override
    public Context getContext() {
        return view.getContext();
    }

    @Override
    public void setDialogFlag(boolean flag) {
        view.setDialogFlag(flag);
    }
}
