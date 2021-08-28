/*
 * Copyright (c) 08/03/16 18:50.2016. Todos os direitos reservados a Andre Straube
 */

package br.com.i9algo.taxiadv.v2.views.widgets.playlistview;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import br.com.i9algo.taxiadv.v2.utils.GlideLoaderUtil;
import br.com.i9algo.taxiadv.v2.utils.time.CountRegressive;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaylistNewsViewV2 extends RelativeLayout implements PlaylistViewInterfaceV2 {

    @BindView(R.id.playlist_main_image)
    ImageView imgBody;
    @BindView(R.id.playlist_source_imageview)
    ImageView imgFonteLogo;
    @BindView(R.id.playlist_header_textview)
    TextView txtHeader;
    @BindView(R.id.playlist_body_textview)
    TextView txtBody;
    @BindView(R.id.playlist_content_box_linearlayout)
    LinearLayout containerBody;
    @BindView(R.id.determinateBar)
    ProgressBar determinateBar;

    private CountRegressive countDownTimer;
    private boolean mMinimized;
    private PlaylistNewsViewDelegate delegate;


    public PlaylistNewsViewV2(Context context) {
        this(context, null, 0);
    }

    public PlaylistNewsViewV2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlaylistNewsViewV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_news_views, this, true);
        ButterKnife.bind(this, view);
    }


    private Runnable tickMethod = new Runnable() {
        public void run() {
            //txtTimer.setText("" + countDownTimer.getTickSecond());
            determinateBar.setProgress((int) countDownTimer.getTickMillis());
        }
    };

    private Runnable finishMethod = new Runnable() {
        public void run() {
            countDownTimer.cancel();
            //Logger.e("Playlist", "delegateNextItemAction - finishMethod");
            determinateBar.setVisibility(GONE);
            delegate.delegateNextItemAction();
        }
    };


    @Override
    public void setItem(SlideshowItem item) {
        imgBody.setVisibility(INVISIBLE);
        imgFonteLogo.setVisibility(INVISIBLE);

        GlideLoaderUtil.loadImagePlaylist(this.getContext(), item.getMainImage(), imgBody);
        GlideLoaderUtil.loadImageMemoryCache(this.getContext(), item.getNewsSourceImage(), imgFonteLogo,
                R.drawable.placeholder_marca, 0, R.drawable.placeholder_marca);

        if (TextUtils.isEmpty(item.getHeader()) || TextUtils.isEmpty(item.getSummary())) {
            containerBody.setVisibility(View.GONE);
        } else {
            containerBody.setVisibility(View.VISIBLE);

            txtHeader.setText(item.getHeader()); // TODO
            txtBody.setText((Html.fromHtml(item.getSummary()).toString()));
            if (item.getMainImageSource() != null) {
                // txtFonteNome.setText((Html.fromHtml(item.getMainPhotoSource()).toString()));
            }
        }

        // Inicia timer de exibicao do item da playlist
        int segundos = item.getExibitionTime() + 1; // Acrescenta 1 (um) segundo, para o contador nao iniciar a contagem em 9.
        determinateBar.setProgress(0);
        determinateBar.setMax((int) TimeUnit.SECONDS.toMillis(segundos));
        determinateBar.setVisibility(VISIBLE);
        countDownTimer = new CountRegressive(new Handler(), tickMethod, finishMethod, TimeUnit.SECONDS.toMillis(segundos), 10);
        imgBody.setVisibility(VISIBLE);
        imgFonteLogo.setVisibility(VISIBLE);
        countDownTimer.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        invalidate();
        requestLayout();
    }

    @Override
    public void onPlayCurrentItem() {
        if (countDownTimer != null)
            countDownTimer.start();
    }

    @Override
    public void onPauseCurrentItem() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }


    public void setDelegate(PlaylistNewsViewDelegate delegate) {
        this.delegate = delegate;
    }

    public interface PlaylistNewsViewDelegate {

        void delegateNextItemAction();
    }
}
