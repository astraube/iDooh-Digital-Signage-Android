package br.com.i9algo.taxiadv.v2.views.slideshow;

import java.util.List;

import br.com.i9algo.taxiadv.v2.models.inbound.Category;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeofencedAdvert;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import br.com.i9algo.taxiadv.v2.models.viewmodel.GridItemViewModel;
import br.com.i9algo.taxiadv.v2.views.BaseViewInterface;

public interface AdvertSlideshowViewInterface extends BaseViewInterface {

    void showSidebarFragment(int category, int id);

    void closeGeoView();

    void hideParquesFragment();

    void hideAboutFragment();

    void hideDrinksFragment();

    void hideSidebarFragment();

    void showHtmlViewSidebar(Category category);

    void showGridViewSidebar(Category category);

    void showAboutFragment();

    void showHTMLSidebarFragment(GridItemViewModel advert);

    void hideAllSidebarFragments();

    void reloadPlaylist();

    void showGeofenceAdvert(GeofencedAdvert geofencedAdvert);

    void writeOnDebug(String geofenceid);

    void showLoadingScreen();

    void showLoadingErrorScreen();

    void showLoadingSuccessfulScreen();

    boolean isLoading();

    List<SlideshowItem> getPlaylistFromPlaylistView();

    boolean isMyServiceRunning(Class<?> serviceClass);

    void showFloat();

    int getCurrentSoundLevel();

    void delegateActionSlide(SlideshowItem item);

    void showMaintenanceFragment();

    void sendToAndroidConfigMenu();

    void setDialogFlag(boolean flag);
}
