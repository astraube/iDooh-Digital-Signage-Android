package br.com.i9algo.taxiadv.ui.components;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.utils.time.CountUITimer;

/**
 * Created by andre on 21/01/2016.
 */
public class DynamicTextView extends TextView {

    private Map<Object, String> mListObjects = null;
    private ArrayList<String> listOfValues;
    private long timeUpdateText = 5*1000;
    private CountUITimer mTimer = null;
    private int mIndex = 0;
    public Handler uiHandler = new Handler();


    public DynamicTextView(Context context) { this(context, null, 0); }
    public DynamicTextView(Context context, AttributeSet attrs) { this(context, attrs, 0); }
    public DynamicTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }
    private void initialize() {
        this.mListObjects = new HashMap<Object, String>();
    }

    private void initTimer() {
        if (mTimer != null)
            mTimer.stop();

        if (mTimer == null && mListObjects != null && mListObjects.size() > 0)
            mTimer = new CountUITimer(uiHandler, runMethod, timeUpdateText, true);

        next();

        mTimer.start();
    }

    public long getTime() { return timeUpdateText; }
    public void setTime(long time) {
        this.timeUpdateText = time;
        initTimer();
    }

    public void addItem(Object key, String value) {
        mListObjects.put(key, value);
        updateListValues();

        initTimer();
    }
    public void removeItem(Object key) {
        mListObjects.remove(key);
        updateListValues();

        initTimer();
    }

    public void setListObjects(Map<Object, String> list) {
        mListObjects = list;
        updateListValues();

        next();

        if (mTimer == null)
            mTimer = new CountUITimer(uiHandler, runMethod, timeUpdateText, true);

        mTimer.start();
    }
    public Map<Object, String> getListObjects() { return mListObjects; }

    private void updateListValues() {
        if (mListObjects != null && mListObjects.size() > 0) {
            Collection<String> values = mListObjects.values();
            listOfValues = new ArrayList<String>(values);
        }
    }
    public void next() {
        loadAnim();

        if (listOfValues != null && listOfValues.size() > 0) {
            this.setText(listOfValues.get(this.mIndex));
        }

        this.mIndex++;

        if (this.mIndex == listOfValues.size())
            this.mIndex = 0;
    }

    private Runnable runMethod = new Runnable()
    {
        public void run()
        {
            next();
        }
    };

    private void loadAnim() {
        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.left_in);
        in.setDuration(500);
        setAnimation(in);
    }
}
