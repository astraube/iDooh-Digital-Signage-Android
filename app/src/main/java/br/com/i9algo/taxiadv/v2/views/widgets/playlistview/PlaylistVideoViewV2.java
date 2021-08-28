/*
 * Copyright (c) 08/03/16 18:51.2016. Todos os direitos reservados a Andre Straube
 */

package br.com.i9algo.taxiadv.v2.views.widgets.playlistview;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

import br.com.i9algo.taxiadv.domain.models.PlaylistItem;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;

public class PlaylistVideoViewV2 extends VideoView implements
        PlaylistViewInterfaceV2,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {


    private PlaylistVideoViewDelegate delegate;

    public PlaylistVideoViewV2(Context context) {
        this(context, null, 0);
    }

    public PlaylistVideoViewV2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlaylistVideoViewV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeAttributes(attrs, defStyleAttr);
    }

    /**
     * Initialize XML attributes.
     *
     * @param attrs to be analyzed.
     */
    private void initializeAttributes(AttributeSet attrs, int defStyleAttr) {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            mapGUI();
        }
    }

    private void mapGUI() {
        // Bug de layout API 16
        setMinimumHeight(getMeasuredHeight());
        requestLayout();

        setOnPreparedListener(this);
        setOnCompletionListener(this);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Logger.e("Playlist", "delegateNextItemAction - onCompletion");
        delegate.delegateNextItemAction();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.w("@@@", "onPrepared - " + isPlaying());

        Log.w("@@@", "getTrackInfo - " + mp.getTrackInfo());

        // TODO a configuracao de volume deve vir do BD, junto com as infos do video
        // no video deve ter "audio ativo" "audio inativo"
        int soundLevel = delegate.delegateGetCurrentAudioLevel();
        mp.setVolume(soundLevel, soundLevel);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        requestLayout();
    }

    @Override
    public void onPlayCurrentItem() {
        super.start();
    }

    @Override
    public void onPauseCurrentItem() {
        super.pause();
    }

    @Override
    public void setItem(SlideshowItem item) {

        setVideoPath(item.getMainImage());

        start();

    }

    public void setItem(PlaylistItem itemPlayList) {
        if (itemPlayList.getSlideshowItem() != null) {
                setVideoPath(itemPlayList.getSlideshowItem().getMainImage());
        } else {
            setVideoPath(itemPlayList.getFileUrl());
        }
        start();

        //Log.w("@@@", "setItem 2 - " + isPlaying());
        //Log.w("@@@", "setItem 1 - " + getDuration());
    }

    public void setDelegate(PlaylistVideoViewDelegate delegate) {
        this.delegate = delegate;
    }

    public interface PlaylistVideoViewDelegate {

        void delegateNextItemAction();
        int delegateGetCurrentAudioLevel();
    }
}
