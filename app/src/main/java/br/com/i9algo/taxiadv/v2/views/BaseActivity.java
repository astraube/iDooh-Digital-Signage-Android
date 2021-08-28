package br.com.i9algo.taxiadv.v2.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import androidx.fragment.app.FragmentActivity;

import org.conscrypt.Conscrypt;

import java.security.Security;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.v2.presenters.BasePresenter;

public abstract class BaseActivity extends FragmentActivity implements BaseViewInterface {

    private PowerManager mPowerManager;
    private WindowManager mWindowManager;

    public PowerManager.WakeLock mWakeLock;

    protected FirebaseAnalytics mFirebaseAnalytics;

    BasePresenter presenter;

    static { Security.insertProviderAt(Conscrypt.newProvider("GmsCore_OpenSSL"), 1); }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        unlockScreen();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // FullScreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT < 16) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay();

        try {
            // Create a bright wake lock
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, getClass().getName());
            //mWakeLock.release();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //#########################
        // Firebase
        // Analytics
        mFirebaseAnalytics = ((CustomApplication)getApplicationContext()).getFirebaseAnalytics(this);
    }

    public CustomApplication getApp()
    {
        return (CustomApplication )this.getApplication();
    }

    @Override
    protected void onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = getApp().getFirebaseUser();
        /*
        if (currentUser == null)
            startActivity( LauncherActivity.createIntent(this) );
        */
        super.onStart();
    }

    public void forwardToView(Class forwardto) {
        Intent intent = new Intent(this, forwardto);
        startActivity(intent);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        return this.mFirebaseAnalytics;
    }


    private void clearReferences(){
        Activity currActivity = getApp().getCurrentActivity();
        if (this.equals(currActivity))
            getApp().setCurrentActivity(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getApp().setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        MixpanelAPI mixpanel = ((CustomApplication)getApplicationContext()).getMixpanelAPI();
        if (mixpanel != null)
            mixpanel.flush();

        clearReferences();

        super.onDestroy();
        if (null != presenter) {
            presenter.unbindView();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        w.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    public void closeSystemDialog() {
        Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(closeDialog);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View mDecorView = getWindow().getDecorView();
        if (hasFocus) {
            mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    // This snippet shows the system bars. It does this by removing all the
    // flags
    // except for the ones that make the content appear under the system bars.
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showSystemUI() {
        View mDecorView = getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
