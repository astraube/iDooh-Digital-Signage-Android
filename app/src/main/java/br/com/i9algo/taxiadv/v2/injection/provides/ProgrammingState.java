package br.com.i9algo.taxiadv.v2.injection.provides;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.i9algo.taxiadv.v2.event.DownloadListEvent;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;
import br.com.i9algo.taxiadv.v2.models.DeviceProfile;
import br.com.i9algo.taxiadv.v2.models.inbound.Programming;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowPlaylist;
import br.com.i9algo.taxiadv.v2.storage.FillerDAO;
import br.com.i9algo.taxiadv.v2.storage.PlaylistDAO;
import rx.Observable;

@Singleton
public class ProgrammingState {
    
    private final String LOG_TAG = "ProgrammingState";

    private DeviceProfile deviceProfile;

    private boolean intialized = false;

    Programming programming;

    @Inject
    PlaylistDAO playlistDAO;

    @Inject
    FillerDAO fillerDAO;

    @Inject
    MainThreadBus bus;

    @Inject
    public ProgrammingState() {
        super();
    }

    public void initialize(Programming programming, Context context) {
        Logger.v(LOG_TAG, "Initializing Programming State");
        playlistDAO.removeErrorStateContent();
        intialized = true;
        bus.register(this);
        this.programming = programming;

        if (programming != null && programming.getData() != null && !programming.getData().getPlaylist_array().isEmpty()) {
            Logger.v(LOG_TAG, "initialize - entrou aqui...");
            playlistDAO.insertPlaylistArray(programming.getData().getPlaylist_array());
            downloadResources(programming.getData().getPlaylist_array(), context);
        }
    }

    private void downloadResources(List<SlideshowPlaylist> playlist_array, Context context) {
        List<String> glideableURLs = new ArrayList<>();
        List<SlideshowItem> videoURLs = new ArrayList<>();

        // Glide options
        RequestOptions glideOptions = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.DATA);

        for (SlideshowPlaylist playlist : playlist_array) {
            for (SlideshowItem slide : playlist.getItems()) {
                if (slide.getMainImage() != null) {
                    Logger.v(LOG_TAG, "sorting slides: " + slide.getMainImage());
                    if (slide.isVideo()) {
                        Logger.v(LOG_TAG, "sorting slides - isVideo");
                        if (!slide.getMainImage().contains("android.resource")) {
                            Logger.v(LOG_TAG, "sorting slides - does not contain android.resource");
                            videoURLs.add(slide);
                        }
                    } else {
                        Logger.v(LOG_TAG, "sorting slides - glideable");
                        glideableURLs.add(slide.getMainImage());
                    }
                }
            }
        }

        for (String url : glideableURLs) {
            Logger.v(LOG_TAG, "slide is image, caching with glide - image url: " + url);

            Glide.with(context)
                    .load(url)
                    .apply(glideOptions)
                    .preload();
        }

        DownloadListEvent listEvent = new DownloadListEvent(videoURLs, context);
        bus.post(listEvent);
    }

    public Observable<SlideshowPlaylist> getCurrentPlaylist(String date, int timeintervalstart, int timeintervalend) {
        Logger.v(LOG_TAG, "programmingstate:getDefaultPlaylist");
        return playlistDAO.getCurrentPlaylist(date, timeintervalstart, timeintervalend);
    }

    public Observable<List<SlideshowItem>> getFillers() {
        Logger.v(LOG_TAG, "programmingstate:getFillers");
        return fillerDAO.getFillers();
    }
}
