package br.com.i9algo.taxiadv.v2.injection.provides;

import android.content.Context;
import android.location.Location;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.i9algo.taxiadv.domain.constants.FirebaseVars;
import br.com.i9algo.taxiadv.domain.models.Device;
import br.com.i9algo.taxiadv.libs.utilcode.util.StringUtils;
import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.v2.glide.GlideApp;
import br.com.i9algo.taxiadv.v2.helpers.DateFormatHelper;
import br.com.i9algo.taxiadv.v2.helpers.ErrorStateContentHelper;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.logging.LogFileWriteHelper;
import br.com.i9algo.taxiadv.v2.models.DeviceUser;
import br.com.i9algo.taxiadv.v2.models.inbound.Programming;
import br.com.i9algo.taxiadv.v2.models.inbound.SlideshowFillerListWrapper;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarItem;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarItemList;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowPlaylist;
import br.com.i9algo.taxiadv.v2.network.taxiadv.AdvService;
import br.com.i9algo.taxiadv.v2.network.taxiadv.IdoohMediaDeviceController;
import br.com.i9algo.taxiadv.v2.storage.CategoryDAO;
import br.com.i9algo.taxiadv.v2.storage.FillerDAO;
import br.com.i9algo.taxiadv.v2.storage.GeofencedAdvertDAO;
import br.com.i9algo.taxiadv.v2.storage.PlaylistDAO;
import br.com.i9algo.taxiadv.v2.storage.SidebarDAO;
import br.com.i9algo.taxiadv.v2.utils.Pref;
import br.com.i9algo.taxiadv.libs.utilcode.util.StorageUtil;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Singleton
public class AppStateManager {

    private static final String LOG_TAG = "AppStateManager";

    @Inject
    ProgrammingState programmingState;
    @Inject
    DeviceState deviceState;
    @Inject
    AdvService advService;
    @Inject
    PlaylistDAO playlistDAO;
    @Inject
    CategoryDAO categoryDAO;
    @Inject
    SidebarDAO sidebarDAO;
    @Inject
    GeofencedAdvertDAO geofencedAdvertDAO;
    @Inject
    LocationStateManager locationStateManageger;
    @Inject
    FillerDAO fillerDAO;
    @Inject
    Context context;

    Subscription sendInfoSubscription;
    Subscription sendPositionSubscription;

    private boolean initialized = false;


    @Inject
    public AppStateManager() {
        super();
    }

    public boolean isInitialized() {
        return initialized;
    }

    //Observables
    public Observable<Programming> initProgrammingState() {

        Observable<Programming> observProgramming;

        if (!Pref.isDemo()) {
            String netGroup = Pref.getNetworkContentGroup();
            if (!StringUtils.isEmpty(netGroup))
                observProgramming = advService.getProgramming(DeviceUser.getUid(), netGroup);
            else
                observProgramming = advService.getProgramming(DeviceUser.getUid());
        } else {
            //int demoId = Pref.getDemoId();
            int demoId = CustomApplication.isDemoContentId;
            //Logger.i(LOG_TAG, "DEMO Programming: " + demoId);
            observProgramming = advService.getProgramming(DeviceUser.getUid(), demoId);
        }

        // Observable Playlist
        return observProgramming.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Programming, Observable<Programming>>() {
                    @Override
                    public Observable<Programming> call(Programming programming) {
                        playlistDAO.cleanPlaylistsAndSlides();

                        Logger.i(LOG_TAG, "About to initialise Programming");
                        LogFileWriteHelper.log("Application has fetched " + programming.getNumberOfPlaylists() + " playlists with " + programming.getTotalNumberOfSlidesOnAllPlaylists() + " slides in total.", context);
                        programmingState.initialize(programming, context);
                        return Observable.just(programming);
                    }
                });

        // TODO - desativado temporariamente a listagem de noticias
        /*return advService.getNews(DeviceUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<SlideshowFillerListWrapper, Observable<Programming>>() {
                    @Override
                    public Observable<Programming> call(SlideshowFillerListWrapper fillers) {
                        LogFileWriteHelper.log("Application has fetched " + fillers.getNumberOfFillers() + " filler news.", context);
                        //fillerDAO.cleanFillers();
                        //fillerDAO.insert(fillers);
                        //downloadFillers(fillers);
                        if (fillers.getData() != null) {
                            Logger.i(LOG_TAG, "fillers " + fillers.getData().getFillerList().size());
                        }
                        return observProgramming;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Programming, Observable<Programming>>() {
                    @Override
                    public Observable<Programming> call(Programming programming) {
                        playlistDAO.cleanPlaylistsAndSlides();

                        Logger.i(LOG_TAG, "About to initialise Programming");
                        LogFileWriteHelper.log("Application has fetched " + programming.getNumberOfPlaylists() + " playlists with " + programming.getTotalNumberOfSlidesOnAllPlaylists() + " slides in total.", context);
                        programmingState.initialize(programming, context);
                        return Observable.just(programming);
                    }
                });*/
    }

    public Observable<SlideshowFillerListWrapper> initFillers() {
        Logger.i(LOG_TAG, "initFillers");
        return advService.getNews(DeviceUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<SlideshowFillerListWrapper, Observable<SlideshowFillerListWrapper>>() {
                    @Override
                    public Observable<SlideshowFillerListWrapper> call(SlideshowFillerListWrapper fillers) {
                        LogFileWriteHelper.log("Application has fetched " + fillers.getNumberOfFillers() + " filler news.", context);
                        fillerDAO.cleanFillers();
                        fillerDAO.insert(fillers);
                        downloadFillers(fillers);

                        //Logger.i(LOG_TAG, "fillers---> " + fillers.getData().getFillerList().size());

                        return Observable.just(fillers);
                    }
                });
    }

    private void downloadFillers(SlideshowFillerListWrapper fillers) {
        RequestOptions glideOptions = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .dontTransform()
                .dontAnimate();

        if (fillers != null && fillers.getData() != null && fillers.getData().getFillerList() != null) {
            for (SlideshowItem item : fillers.getData().getFillerList()) {
                GlideApp.with(CustomApplication.getInstance().getApplicationContext())
                        .load(item.getMainImage())
                        .apply(glideOptions)
                        .preload();
            }
        }
    }

    public Observable<Boolean> initialize() {
        Logger.i(LOG_TAG, "Initializing State Manager");
        /**
         * 05/07/2019
         * desabilitado, dados estao no FIREBASE
         *
         return initCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<CategoriesArraylistWrapper, Observable<Programming>>() {
                    @Override
                    public Observable<Programming> call(CategoriesArraylistWrapper discard) {
                        return initProgrammingState();
                    }
                })*/
        return initProgrammingState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Programming, Observable<SidebarItemList>>() {
                    @Override
                    public Observable<SidebarItemList> call(Programming discard) {
                        return getSidebars();
                    }
                })
                .flatMap(new Func1<SidebarItemList, Observable<SlideshowFillerListWrapper>>() {
                    @Override
                    public Observable<SlideshowFillerListWrapper> call(SidebarItemList discard) {
                        return initFillers();
                    }
                })
//                .flatMap(new Func1<SidebarItemList, Observable<GeofencedAdvertArraylistWrapper>>() {
//                    @Override
//                    public Observable<GeofencedAdvertArraylistWrapper> call(SidebarItemList discard) {
//                        return getGeofendedAdverts();
//                    }})
                .flatMap(new Func1<SlideshowFillerListWrapper, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(SlideshowFillerListWrapper discard) {
                        initialized = true;
                        Logger.i(LOG_TAG, "app initialized");
                        return Observable.just(initialized);
                    }
                });
    }

    private Observable<SidebarItemList> getSidebars() {
        Logger.i(LOG_TAG, "Initializing Sidebars");

        Observable<SidebarItemList> sidebars;

        if (!Pref.isDemo()) {
            String netGroup = Pref.getNetworkContentGroup();
            if (!StringUtils.isEmpty(netGroup))
                sidebars = advService.getSidebars(DeviceUser.getUid(), netGroup);
            else
                sidebars = advService.getSidebars(DeviceUser.getUid());
        } else {
            //int demoId = Pref.getDemoId();
            int demoId = CustomApplication.isDemoContentId;
            Logger.i(LOG_TAG, "DEMO Sidebars: " + demoId);
            sidebars = advService.getSidebars(DeviceUser.getUid(), demoId);
        }

        return sidebars
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<SidebarItemList, Observable<SidebarItemList>>() {
                    @Override
                    public Observable<SidebarItemList> call(SidebarItemList sidebars) {
                        sidebarDAO.cleanSidebars();

                        if (sidebars.getData() != null) {

                            String s = new com.google.gson.Gson().toJson(sidebars.getData());

                            sidebarDAO.insertSidebarItemList(sidebars);

                            RequestOptions glideOptions = new RequestOptions()
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

                            for (SidebarItem sidebarItem : sidebars.getData().getSidebars()) {
                                //Logger.i(LOG_TAG, "--# " + sidebarItem.getCoverImage());

                                Glide.with(CustomApplication.getInstance().getApplicationContext())
                                        .load(sidebarItem.getCoverImage())
                                        .apply(glideOptions)
                                        .preload();

                                Glide.with(CustomApplication.getInstance().getApplicationContext())
                                        .load(sidebarItem.getMainImage())
                                        .apply(glideOptions)
                                        .preload();
                            }
                        }
                        return Observable.just(sidebars);
                    }
                });
    }

    public void initializeNoContent() {
        Logger.i(LOG_TAG, "initializing without content");
        //categoryDAO.insertCategoryArray(ErrorStateContentHelper.getErrorStateCategories());
        programmingState.initialize(ErrorStateContentHelper.getErrorStateProgramming(), context);
        Logger.i(LOG_TAG, "no Content initialized");
    }


    public void initializeOldContent() {
        // TODO - inserir conteudo DEFAULT
        Logger.i(LOG_TAG, "App will use old content");
    }

    /**
     * 05/07/2019
     * desabilitado, dados estao no FIREBASE
     *
    public Observable<ArrayList<Category>> getCategories() {
        return categoryDAO.getAllCategories();
    }*/

    public Observable<SlideshowPlaylist> initializeNoConnection() {
        return playlistDAO.getDefaultPlaylist(DateFormatHelper.getTodaysDateAsString());
    }

    public void handleAvailableDiskSpace(Context context) {
        Logger.i(LOG_TAG, "statemanager.handleAvailableDiskSpace:entering if");
        if (StorageUtil.hasLessThanHalfAvailableInternalDiskSpace() || StorageUtil.hasLessThanHalfAvailableExternalDiskSpace()) {
            Logger.i(LOG_TAG, "statemanager.handleAvailableDiskSpace: passed if");

            this.sendDeviceInfos(context);
        }
    }

    public void cleanDatabase() {
        //categoryDAO.cleanCategories();
        playlistDAO.cleanPlaylistsAndSlides();
        sidebarDAO.cleanSidebars();
    }

    public void sendDeviceInfos(Context context) {
        Device device = ((CustomApplication)context.getApplicationContext()).getDevice();
        device.withDeviceBuildInfos();

        IdoohMediaDeviceController.sendDataDevice(device, FirebaseVars.DB_DEVICE_CHILD_INFO);
    }
}
