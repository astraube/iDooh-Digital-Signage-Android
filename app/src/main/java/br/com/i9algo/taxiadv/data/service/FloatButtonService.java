package br.com.i9algo.taxiadv.data.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.views.slideshow.AdvertSlideshowActivity;

public class FloatButtonService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;
    WindowManager.LayoutParams params;
    boolean mHasDoubleClicked = false;
    long lastPressTime;

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.ic_logo_float);

        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        //this code is for dragging the chat head
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        // Get current time in nano seconds.
                        long pressTime = System.currentTimeMillis();


                        // If double click...
                        long pressoffset = (pressTime - lastPressTime);
                        Log.e("advService", "press time in millis: " + pressoffset);
                        Log.e("advService", "press time in seconds: " + TimeUnit.MILLISECONDS.toSeconds(pressoffset));
                        if (pressoffset <= 500) {
                            doBackAction();
                            mHasDoubleClicked = true;
                        }
                        else {     // If not double click....
                            mHasDoubleClicked = false;
                        }
                        lastPressTime = pressTime;
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatHead, params);
                        return true;
                }
                return false;
            }
        });
        windowManager.addView(chatHead, params);
    }

    private void doBackAction() {
        Log.e("advService", "doBackAction");
        Intent intent = new Intent(this, AdvertSlideshowActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null)
            windowManager.removeView(chatHead);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}