package br.com.i9algo.taxiadv.ui.components;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.utils.time.CountTimerThread;
import br.com.i9algo.taxiadv.v2.utils.SoundUtil;

/**
 * Created by aStraube on 22/05/2016.
 */
public class GeopointView extends RelativeLayout {

    private final OnGeopointListener mListener;
    private ImageView mImgGeopoint;
    private ImageButton mBtClose;
    private CountTimerThread mTimer;


    public interface OnGeopointListener {
        public interface DelegatePlaylistMinimize {
            void delegateMinimizePlaylistView();
        }
        void onCloseGeopoint(GeopointView geo);
    }

    public GeopointView(Context context) {
        this(context, null);
    }
    public GeopointView(Context context, OnGeopointListener listener) {
        super(context);
        mListener = listener;
        onFinishInflate();
        SoundUtil.playSoundCoolNotify(context);

        setAnimation(AnimationUtils.loadAnimation(context, R.anim.left_in));

        mTimer = new CountTimerThread(new Handler(), localComplete, 20000);
        mTimer.start();
    }
    /*public GeopointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public GeopointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeAttributes(context, attrs, defStyleAttr);
    }*/

    /**
     * Initialize XML attributes.
     *
     * @param attrs to be analyzed.
     */
    /*private void initializeAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR);

        if (attrs != null) {

        } else {
            onFinishInflate();
        }
    }*/

    /**
     * Override method to map dragged view, secondView to view objects, to configure dragged
     * view height and to initialize DragViewHelper.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            mapGUI();
        }
    }

    @Override
    public void onViewRemoved(View child) {
        setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.left_out));
        super.onViewRemoved(child);
    }

    private void mapGUI() {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setLayoutParams(lp);

        instanciateImageGeopoint();
        instanciateBtClose();
        instanciateTimer();
    }

    private void instanciateImageGeopoint() {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.mImgGeopoint = new ImageView(getContext());
        this.mImgGeopoint.setLayoutParams(lp);
        //this.mImgGeopoint.setImageResource(R.drawable.geoponto_claro); // TODO essa imagem deve vir como parametro
        this.mImgGeopoint.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(this.mImgGeopoint);
    }

    private void instanciateBtClose() {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.setMargins(10, 10, 10, 10);
        this.mBtClose = new ImageButton(getContext());
        this.mBtClose.setLayoutParams(lp);
        this.mBtClose.setOnClickListener(onCloseClick);
        this.mBtClose.setBackgroundResource(R.drawable.button_close_black);
        addView(this.mBtClose);
    }

    private void instanciateTimer() {
        // TODO criar um timer view recrescente
    }

    private OnClickListener onCloseClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener != null)
                mListener.onCloseGeopoint(GeopointView.this);

            if (mTimer != null) {
                mTimer.finalize();
            }
        }
    };

    public Runnable localComplete = new Runnable()  {
        @Override
        public void run() {
            onCloseClick.onClick(null);
        }
    };

    public void setImage() {
        // TODO fazer
    }
}
