package br.com.i9algo.taxiadv.ui.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.DigitalClock;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.i9algo.taxiadv.R;


public class ClockView extends LinearLayout {

    private TextView textView;
    private TextView txtClock;

    public ClockView(Context context) { this(context, null, 0); }
    public ClockView(Context context, AttributeSet attrs) { this(context, attrs, 0); }
    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        setOrientation(LinearLayout.VERTICAL);

        setClickable(true);
        setFocusable(true);

        // Time
        txtClock = new DigitalClock(getContext());
        LayoutParams txtClock_LayoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        txtClock_LayoutParams.gravity = Gravity.CENTER;
        txtClock.setGravity(Gravity.BOTTOM);
        txtClock.setTextSize((int) getResources().getDimension(R.dimen.taskbar_clock_text_size));
        txtClock.setTextColor(getResources().getColor(R.color.white));
        txtClock.setLayoutParams(txtClock_LayoutParams);
        addView(txtClock);

        // Date
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        String date = df.format(Calendar.getInstance().getTime());

        textView = new TextView(getContext());
        LayoutParams textView_LayoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textView_LayoutParams.gravity = Gravity.CENTER;
        textView.setGravity(Gravity.TOP);
        textView.setText(date);
        textView.setTextSize((int) getResources().getDimension(R.dimen.taskbar_clock_text_size));
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setLayoutParams(textView_LayoutParams);
        addView(textView);

        refreshDrawableState();
        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
