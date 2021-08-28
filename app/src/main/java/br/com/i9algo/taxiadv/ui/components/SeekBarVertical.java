package br.com.i9algo.taxiadv.ui.components;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import androidx.core.view.ViewCompat;

/**
 * Created by Andre Straube on 23/03/2016.
 */
public class SeekBarVertical extends SeekBar {

    public SeekBarVertical(Context context) {
        super(context);
        initialize(context, null, 0);
    }
    public SeekBarVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }
    public SeekBarVertical(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs, defStyle);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR);

        /*if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalSeekBar, defStyleAttr, defStyleRes);
            a.recycle();
        }*/
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    public synchronized void setProgress(int progress)  // it is necessary for calling setProgress on click of a button
    {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-super.getHeight(), 0);
        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        boolean result = true;
        int progress = getMax() - (int) (getMax() * event.getY() / getHeight());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                result = super.onTouchEvent(event);

            case MotionEvent.ACTION_MOVE:
                setProgress(progress);
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                result = true;
                break;
            case MotionEvent.ACTION_UP:
                result = super.onTouchEvent(event);
                setProgress(progress);
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_CANCEL:
                result = true;
        }
        return result;
    }
}