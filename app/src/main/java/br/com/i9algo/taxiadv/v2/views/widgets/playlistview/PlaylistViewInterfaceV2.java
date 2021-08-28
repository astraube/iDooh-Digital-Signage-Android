package br.com.i9algo.taxiadv.v2.views.widgets.playlistview;

import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;

public interface PlaylistViewInterfaceV2 {

    void setVisibility(int visibility);

    void onPlayCurrentItem();

    void onPauseCurrentItem();

    void setItem(SlideshowItem item);
}
