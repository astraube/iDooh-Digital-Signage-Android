package br.com.i9algo.taxiadv.ui.components;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.concurrent.TimeUnit;

import androidx.core.view.ViewCompat;
import br.com.i9algo.taxiadv.R;


/**
Runnable openListener = new Runnable()  {
    @Override
    public void run() {

    }
};

 Runnable closeListener = new Runnable()  {
    @Override
    public void run() {

    }
};

 Runnable seekChangeListener = new Runnable()  {
    @Override
    public void run() {

    }
};

 */
/**
 * Created by Andre Straube on 23/03/2016.
 */
public class SeekbarPopup extends LinearLayout implements Runnable, View.OnClickListener, SeekBar.OnSeekBarChangeListener {


    //Se tiver varios Seekbars na tela, ira fechar sempre o que nao esta sendo utilizado
    protected static SeekbarPopup currentSeekOpened = null;


    private Animation mShowAnimation, mTopimageShowAnim;
    private Animation mHideAnimation;

    private ContextThemeWrapper mImageTopContext;
    private ImageView mImageTop;

    private ContextThemeWrapper mTriggerContext;
    private ImageButton mBtAction;

    private ContextThemeWrapper mSeekbarContext;
    private SeekBarVertical mSeekbar;

    private Handler mHandler;
    private Runnable mOpenListener = null;
    private Runnable mCloseListener = null;
    private Runnable mSeekBarChangeListener = null;

    private boolean mAutoHideEnabled = false;
    private long mAutoHideTimeMS = 5000; // Tempo em Milisegundos

    private Drawable mImageTopDrawable;

    private int mTriggerBtStyleRes;
    private float mTriggerBtSize;
    private Drawable mTriggerBtImage;
    private int mTriggerBtBackground;

    private float mSeekBarSize;
    private int mSeekBarBackground;


    public SeekbarPopup(Context context) { this(context, null, 0); }
    public SeekbarPopup(Context context, AttributeSet attrs) { this(context, attrs, 0); }
    public SeekbarPopup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeAttributes(context, attrs, defStyleAttr);
    }


    /**
     * Initialize XML attributes.
     *
     * @param attrs to be analyzed.
     */
    private void initializeAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR);

        if (attrs != null) {
            TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.SeekBarPopup, defStyleAttr, 0);

            mAutoHideEnabled = attr.getBoolean(R.styleable.SeekBarPopup_sbp_autoHideEnabled, false);
            mAutoHideTimeMS = TimeUnit.SECONDS.toMillis(attr.getInt(R.styleable.SeekBarPopup_sbp_autoHideTime, 10));


            // Imagem do topo da popup
            mImageTopDrawable = attr.getDrawable(R.styleable.SeekBarPopup_sbp_topImage);


            /**
             * Estilos do botao de gatilho
             */
            mTriggerBtStyleRes = attr.getResourceId(R.styleable.SeekBarPopup_sbp_triggerButtonStyle, 0);
            mTriggerBtSize = attr.getDimension(R.styleable.SeekBarPopup_sbp_triggerButtonSize, getResources().getDimension(R.dimen.seekbar_trigger_button_size));
            mTriggerBtImage = attr.getDrawable(R.styleable.SeekBarPopup_sbp_triggerButtonImage);
            if (mTriggerBtImage == null) {
                mTriggerBtImage = getResources().getDrawable(R.drawable.add);
            }
            mTriggerBtBackground = attr.getResourceId(R.styleable.SeekBarPopup_sbp_triggerButtonBackground, 0);

            /**
             * Estilos do SeekBar
             */
            mSeekBarSize = attr.getDimension(R.styleable.SeekBarPopup_sbp_seekBarSize, getResources().getDimension(R.dimen.seekbar_size));
            mSeekBarBackground = attr.getResourceId(R.styleable.SeekBarPopup_sbp_seekBarBackground, R.drawable.seekbar_bg_default);
            initShowAnimation(attr);
            initHideAnimation(attr);

            attr.recycle();
        }
    }

    private void initShowAnimation(TypedArray attr) {
        int resourceId = attr.getResourceId(R.styleable.SeekBarPopup_sbp_seekBarShowAnimation, R.anim.seek_scale_up);
        mShowAnimation = AnimationUtils.loadAnimation(getContext(), resourceId);

        // Top Image Anim
        mTopimageShowAnim = AnimationUtils.loadAnimation(getContext(), R.anim.seek_scale_topimage);
    }

    private void initHideAnimation(TypedArray attr) {
        int resourceId = attr.getResourceId(R.styleable.SeekBarPopup_sbp_seekBarHideAnimation, R.anim.seek_scale_down);
        mHideAnimation = AnimationUtils.loadAnimation(getContext(), resourceId);

        // Top Image Anim
        //mTopimageHideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.seek_scale_topimage);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            mapGUI();
        }
    }

    private void createButtonTrigger() {
        mTriggerContext = new ContextThemeWrapper(getContext(), mTriggerBtStyleRes);
        LayoutParams lp = new LayoutParams((int)mTriggerBtSize, (int)mTriggerBtSize);
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        lp.setMargins(10, 0, 10, 0);

        mBtAction = new ImageButton(mTriggerContext, null, mTriggerBtStyleRes);
        mBtAction.setLayoutParams(lp);
        mBtAction.setClickable(true);
        mBtAction.setImageDrawable(mTriggerBtImage);
        mBtAction.setOnClickListener(this);
        if (mTriggerBtBackground != 0)
            mBtAction.setBackgroundResource(mTriggerBtBackground);

        addView(mBtAction);
    }

    private void createSeekBar() {
        // Verificar se o layout esta na vertical ou na horizontal
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, (int)mSeekBarSize);
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        lp.setMargins(10, 0, 10, 10);
        mSeekbar = new SeekBarVertical(getContext());
        mSeekbar.setLayoutParams(lp);
        mSeekbar.setPadding(10, 10, 10, 10);
        mSeekbar.setVisibility(View.GONE);
        mSeekbar.setOnSeekBarChangeListener(this);
        // TODO verificar se o layout esta na vertical ou na horizontal
        //mSeekbar.getLayoutParams().width = (int) mSeekBarSize;
        //mSeekbar.getLayoutParams().height = (int)mSeekBarSize;
        if (mSeekBarBackground != 0)
            mSeekbar.setBackgroundResource(mSeekBarBackground);


        addView(mSeekbar);
    }

    private void createTopImage() {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        lp.setMargins(10, 10, 10, 10);
        mImageTop = new ImageView(getContext());
        mImageTop.setLayoutParams(lp);
        mImageTop.setVisibility(View.GONE);
        if (mImageTopDrawable != null) {
            mImageTop.setImageDrawable(mImageTopDrawable);
        }

        addView(mImageTop);
    }

    private void mapGUI() {

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        this.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(lp);
        this.setMinimumWidth(70);

        //LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflater.inflate(R.layout.seekbar_popup_layout, this, true);

        mHandler = new Handler();

        /**
         * Imagem do topo da popup
         */
        createTopImage();

        /**
         * Configuracoes SeekBar
         */
        createSeekBar();

        /**
         * Configuracoes Botao de gatilho
         */
        createButtonTrigger();
    }

    /**
     * retornar valor do seekBar
     * @return
     */
    public int getSeekValue() { return mSeekbar.getProgress(); }

    public void setSeekValue(int value) { mSeekbar.setProgress(value); }

    public void setSeekMaxValue(int value) { mSeekbar.setMax(value); }

    public void setSeekIncrementProgress(int value) { mSeekbar.setKeyProgressIncrement(value); }

    public Animation getShowAnimation() {
        return mShowAnimation;
    }

    public Animation getHideAnimation() {
        return mHideAnimation;
    }

    public void setOpenListener(Runnable method) {
        mOpenListener = method;
    }

    public void setCloseListener(Runnable method) {
        mCloseListener = method;
    }

    public void setSeekBarChangeListener(Runnable method) {
        mSeekBarChangeListener = method;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private void playShowAnimation() {
        mHideAnimation.cancel();
        mSeekbar.startAnimation(mShowAnimation);
        mImageTop.startAnimation(mTopimageShowAnim);
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private void playHideAnimation() {
        mShowAnimation.cancel();
        mSeekbar.startAnimation(mHideAnimation);
    }

    /**
     * Timer de auto hide
     */
    private void startTimer() {
        if (mAutoHideEnabled) {
            mHandler.removeCallbacks(this);
            mHandler.postDelayed(this, mAutoHideTimeMS);
        }
    }
    private void stopTimer() {
        mHandler.removeCallbacks(this);
    }
    private void resetTimer() {
        this.stopTimer();
        this.startTimer();
    }
    /***************/

    @Override
    public void run() {
        hide();
    }

    public void toggle()  {
        if (mSeekbar.getVisibility() == VISIBLE)
            hide();
        else if ((mSeekbar.getVisibility() == INVISIBLE) || (mSeekbar.getVisibility() == GONE))
            show();
    }

    public void show() {
        /**
         * Se tiver varios Seekbars na tela, ira fechar sempre o que nao esta sendo utilizado
         */
        if (SeekbarPopup.currentSeekOpened != null)
            SeekbarPopup.currentSeekOpened.hide();

        SeekbarPopup.currentSeekOpened = this;
        /************************************/


        if (this.mHandler != null && this.mOpenListener != null)
            this.mHandler.post(this.mOpenListener);

        // Iniciar Timer de AutoHide
        startTimer();

        playShowAnimation();
        mSeekbar.setVisibility(VISIBLE);
        mImageTop.setVisibility(VISIBLE);
    }

    public void hide() {
        /**
         * Se tiver varios Seekbars na tela, ira fechar sempre o que nao esta sendo utilizado
         */
        if (SeekbarPopup.currentSeekOpened != null && SeekbarPopup.currentSeekOpened == this)
            SeekbarPopup.currentSeekOpened = null;
        /************************************/

        if (this.mHandler != null && this.mCloseListener != null)
            this.mHandler.post(this.mCloseListener);

        stopTimer();
        playHideAnimation();
        mSeekbar.setVisibility(GONE);
        mImageTop.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        //Log.v("@@@", "onClick");
        toggle();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //float f = (progress / 100.0F);
        //Log.v("@@@", "onProgressChanged - " + progress + " - " + fromUser);
        //Log.v("@@@", "onProgressChanged 2 - " + f + " - " + fromUser);
        //Log.v("@@@", "onProgressChanged 3 - " + (int)(f * 100) + " - " + fromUser);

        if (this.mHandler != null && this.mSeekBarChangeListener != null)
            this.mHandler.post(this.mSeekBarChangeListener);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //Log.v("@@@", "onStartTrackingTouch");
        stopTimer();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //Log.v("@@@", "onStopTrackingTouch");
        startTimer();
    }
}
