package br.com.i9algo.taxiadv.v2.views.widgets.playlistview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.domain.enums.AdvType;
import br.com.i9algo.taxiadv.v2.components.QRGen.QRCode;
import br.com.i9algo.taxiadv.v2.download.DownloadHelperDAO;
import br.com.i9algo.taxiadv.v2.download.DownloadItem;
import br.com.i9algo.taxiadv.v2.event.ResizePlaylistEvent;
import br.com.i9algo.taxiadv.v2.helpers.AnalyticsTrackerProvider;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.helpers.defaults.DefaultSubscriber;
import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;
import br.com.i9algo.taxiadv.v2.logging.LogFileWriteHelper;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowPlaylist;
import br.com.i9algo.taxiadv.v2.utils.AssetsUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlaylistViewV2 extends RelativeLayout implements PlaylistNewsViewV2.PlaylistNewsViewDelegate, PlaylistVideoViewV2.PlaylistVideoViewDelegate {

    private String LOG_TAG = getClass().getSimpleName();

    private boolean hasShownAnything = false;

    private int originalHeight = 0;
    private int originalWidth = 0;
    private int minWidth = 0;
    private static final float SLIDE_TOP = 0f;
    private static final float SLIDE_BOTTOM = 1f;
    private boolean mMinimized = false;

    private PlaylistViewDelegate clickDelegate;

    SlideshowPlaylist playlist;

    private List<SlideshowItem> mFillers;
    private List<SlideshowItem> mFillersTemp;

    private PlaylistVideoViewV2 mVideoView;
    private PlaylistNewsViewV2 mNewsViews;
    private PlaylistViewInterfaceV2 mCurrentView = null;
    private int indexItem = 0;
    private int timesPlaylistHasLooped = 0;
    private DownloadHelperDAO downloadDao;
    private Subscription updateSubscription;
    private ImageView imgQrcode;
    private int qrcodeSize = 200;

    public PlaylistViewV2(Context context) {
        super(context);

    }

    public PlaylistViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaylistViewV2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // view stuff

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        minWidth = getContext().getResources().getDimensionPixelSize(R.dimen.playlist_min_width);
        mVideoView = (PlaylistVideoViewV2) findViewById(R.id.videoView);
        mVideoView.setDelegate(this);
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogFileWriteHelper.log("Failed to play the following video: " + getCurrentItem().getMainImage(), getContext());
                delegateNextItemAction();
                return true;
            }
        });

        mNewsViews = (PlaylistNewsViewV2) findViewById(R.id.newsView);
        mNewsViews.setDelegate(this);

        imgQrcode = newImageView(qrcodeSize);
    }

    private ImageView newImageView(int height) {
        LayoutParams lp = new LayoutParams(height, height);
        lp.setMargins(0, 20, 20, 0);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_END);
        }
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView imgView = new ImageView(getContext());
        imgView.setLayoutParams(lp);
        imgView.setVisibility(GONE);
        addView(imgView);

        return imgView;
    }
    private Bitmap getQrcode(String code, int qrSize) {
        QRCode qr = QRCode.from(code);
        return qr.withSize(qrSize, qrSize).bitmap();
    }


    // *********** methods explosed to presenter ***********

    public void minimize() {
        onMinimize();
    }

    public void maximize() {
        onMaximize();
    }

    // Playlist management stuff

    private void play(SlideshowItem item) {
        SlideshowItem tempItem = item;

        LogFileWriteHelper.log("Playlist enqueued item: " + item.toString(), this.getContext());

        mNewsViews.setVisibility(GONE);
        mVideoView.setVisibility(GONE);


        AdvType type = (!TextUtils.isEmpty(item.getType())) ? AdvType.valueOf(item.getType().toUpperCase()) : AdvType.DEFAULT;
        //Logger.i(LOG_TAG, "Type - " + type);
        //Logger.i(LOG_TAG, "Type - " + type.equals(AdvType.NEWS));

        if (type.equals(AdvType.NEWS)) {
            if (mFillers.isEmpty()) {
                Logger.i(LOG_TAG, "Database hsa no fillers, skipping item.");
                LogFileWriteHelper.log("Database hsa no fillers, skipping item.", this.getContext());
                //delegateNextItemAction();
            } else {
                tempItem = getRandomFiller();
                LogFileWriteHelper.log("Fetched filler: " + tempItem.toString(), this.getContext());
            }
        } else {
            tempItem = item;
        }
        final SlideshowItem stageItem = tempItem;
        if (stageItem.isVideo()) {
            if (stageItem.getMainImage().contains("android.resource")) {
                mCurrentView = mVideoView;
                SlideshowItem definitiveItem = stageItem;
                mCurrentView.setItem(definitiveItem);
                AnalyticsTrackerProvider.sendSlideVislualisedNotification(getContext(), definitiveItem.getTitle(), definitiveItem.getId());
                mCurrentView.setVisibility(VISIBLE);
                hasShownAnything = true;
            } else {
                updateSubscription = downloadDao.getItemBySlideId(stageItem.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultSubscriber<DownloadItem>() {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                e.printStackTrace();
                                Logger.e(LOG_TAG, "downloadDAO - getItemBySlideId.onErroring");
                                updateSubscription.unsubscribe();
                            }

                            @Override
                            public void onNext(DownloadItem object) {
                                super.onNext(object);
                                Logger.e(LOG_TAG, "downloadDAO - getItemBySlideId.OnNexting");
                                if (object != null) {
                                    mCurrentView = mVideoView;
                                    SlideshowItem definitiveItem = stageItem;
                                    definitiveItem.setMainImage(object.getFileLocation());
                                    mCurrentView.setItem(definitiveItem);
                                    AnalyticsTrackerProvider.sendSlideVislualisedNotification(getContext(), definitiveItem.getTitle(), definitiveItem.getId());
                                    mCurrentView.setVisibility(VISIBLE);
                                    hasShownAnything = true;
                                } else {
                                    delegateNextItemAction();
                                }
                                updateSubscription.unsubscribe();
                            }

                            @Override
                            public void onCompleted() {
                                Logger.e(LOG_TAG, "downloadDAO - getItemBySlideId.onCompleted");
                                updateSubscription.unsubscribe();
                            }
                        });
            }
        } else {
            //TODO CREATE GLIDE VERIFICATION
            mCurrentView = mNewsViews;
            mCurrentView.setItem(stageItem);
            AnalyticsTrackerProvider.sendSlideVislualisedNotification(getContext(), stageItem.getTitle(), stageItem.getId());
            mCurrentView.setVisibility(VISIBLE);
        }

        if (!TextUtils.isEmpty(item.getUrl())) {
            imgQrcode.setImageBitmap( getQrcode(item.getUrl(), qrcodeSize) );
            imgQrcode.setVisibility(VISIBLE);
        } else {
            imgQrcode.setVisibility(GONE);
        }

    }

    private SlideshowItem getRandomFiller() {
        Random randomGenerator = new Random();

        if (mFillersTemp.size() < 1) {
            mFillersTemp = new ArrayList<>(mFillers);
        }
        int index = randomGenerator.nextInt(mFillersTemp.size());
        SlideshowItem returnItem = mFillersTemp.get(index);
        mFillersTemp.remove(index);
        return returnItem;
    }

    // Control methods, currently not used

    public void onPlayCurrentItem() {
        if (mCurrentView != null)
            mCurrentView.onPlayCurrentItem();
    }

    public void onPauseCurrentItem() {
        if (mCurrentView != null)
            mCurrentView.onPauseCurrentItem();
    }

    // Delegator

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getCurrentItem() != null)
            clickDelegate.delegateClickAction(getCurrentItem());
        return false;
    }

    // *************** resize stuff ****************
    public boolean smoothSlideTo(@NonNull float slideOffset) {
        final int topBound = getPaddingTop();
        int x = (int) (slideOffset * (getWidth() - getOriginalWidth()));
        int y = (int) (topBound + slideOffset * getVerticalDragRange());
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    private void notifyMaximizeToListener() {
        MainThreadBus.getInstance().post(new ResizePlaylistEvent(ResizePlaylistEvent.ResizeEventType.MAXIMIZE));
    }

    private void notifyMinimizeToListener() {
        MainThreadBus.getInstance().post(new ResizePlaylistEvent(ResizePlaylistEvent.ResizeEventType.MINIMIZE));
    }

    private void onMinimize() {
        Logger.v(LOG_TAG, "onMinimize");

        if (isMinimized() || mCurrentView == null)
            return;

        mMinimized = true;

        try {
            getLayoutParams().width = minWidth;
            getLayoutParams().height = getOriginalHeight();

            smoothSlideTo(SLIDE_BOTTOM);
            requestLayout();
            invalidate();
            notifyMinimizeToListener();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressLint("WrongCall")
    private void onMaximize() {
        Logger.v(LOG_TAG, "onMaximize");

        if (isMaximized() || mCurrentView == null)
            return;

        mMinimized = false;

        try {
            LayoutParams params = (LayoutParams) getLayoutParams();
            params.width = getOriginalWidth();
            params.topMargin = 0;
            setLayoutParams(params);
            onMeasure(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            smoothSlideTo(SLIDE_TOP);
            notifyMaximizeToListener();
            requestLayout();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public int getOriginalHeight() {
        if (originalHeight == 0) {
            originalHeight = getMeasuredHeight();
        }
        return originalHeight;
    }

    public int getOriginalWidth() {
        if (originalWidth == 0) {
            originalWidth = getMeasuredWidth();
        }
        return originalWidth;
    }

    public boolean isMinimized() {
        return mMinimized;
    }

    public boolean isMaximized() {
        return !mMinimized;
    }

    private float getVerticalDragRange() {
        return getHeight() - getOriginalHeight();
    }

    private float getVerticalDragOffset() {
        return getTop() / getVerticalDragRange();
    }

    // Delegator class

    public void setDelegate(PlaylistViewDelegate clickDelegate) {
        this.clickDelegate = clickDelegate;
    }

    public void setPlaylist(SlideshowPlaylist playlist) {
        this.playlist = playlist;
    }

    public void setmFillers(List<SlideshowItem> fillers) {
        mFillers = new ArrayList<>(fillers);
        mFillersTemp = new ArrayList<>(fillers);
    }

    @Override
    public void delegateNextItemAction() {
        play( getNextItem() );
    }

    @Override
    public int delegateGetCurrentAudioLevel() {
        return clickDelegate.getAudioLevel();
    }

    private SlideshowItem getNextItem() {
        indexItem++;

        //Logger.e(LOG_TAG, "indexItem: " + indexItem);

        if (indexItem >= playlist.getItems().size()) {
            timesPlaylistHasLooped++;
            //Logger.e(LOG_TAG, "times playlist looped: " + timesPlaylistHasLooped);
        }

        if (timesPlaylistHasLooped >= 1) {
            //Logger.e(LOG_TAG, "timesPlaylistHasLooped >= 1 : " + (timesPlaylistHasLooped >= 1));
            //Logger.e(LOG_TAG, "checking new loop");
            indexItem = 0;
            timesPlaylistHasLooped = 0;

            if (hasShownAnything) {
                hasShownAnything = false;
                clickDelegate.delegateReloadPlaylistAction();
            } else {
                //Logger.i(LOG_TAG, "getNextItem - else");

                SlideshowItem item;
                item = new SlideshowItem();
                item.setTitle("IDOOH");
                //item.setMainImageURL("android.resource://br.com.i9algo.taxiadv/" + R.raw.placeholder_marca);
                item.setMainImage(AssetsUtil.getPathFile("assets://placeholder_marca.jpg"));
                item.setType(AdvType.DEFAULT.toString());
                item.setOrder(1);
                item.setPlaylistId(1);
                item.setId(1);
                item.setExibitionTime(10);
                return item;
            }

        }
        return getCurrentItem();
    }

    public SlideshowItem getCurrentItem() {
        SlideshowItem model = null;

        if (playlist != null && playlist.getItems() != null && playlist.getItems().size() > 0) {
            model = playlist.getItems().get(indexItem);
        }
        return model;
    }

    public List<SlideshowItem> getCurrentPlaylist() {
        return playlist.getItems();
    }

    public void setDownloadDao(DownloadHelperDAO downloadDao) {
        this.downloadDao = downloadDao;
    }

    public void start() {
        hasShownAnything = false;
        play(getCurrentItem());
    }

    public interface PlaylistViewDelegate {

        void delegateClickAction(SlideshowItem item);

        void delegateReloadPlaylistAction();

        int getAudioLevel();
    }

}
