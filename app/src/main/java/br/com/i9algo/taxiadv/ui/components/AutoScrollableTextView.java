package br.com.i9algo.taxiadv.ui.components;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Taxi ADV on 14/03/2016.
 */
public class AutoScrollableTextView extends TextView {

    public AutoScrollableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public AutoScrollableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize ();
    }

    public AutoScrollableTextView(Context context) {
        super(context);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initialize () {
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
        setSingleLine();
        setHorizontallyScrolling(true);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if(focused)
            super.onWindowFocusChanged(focused);
    }


    @Override
    public boolean isFocused() {
        return true;
    }
}