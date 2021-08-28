package br.com.i9algo.taxiadv.v2.injection.provides;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.View;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import br.com.i9algo.taxiadv.v2.views.BaseActivity;

@Singleton
public class ActivityHelper {

    @Inject Provider<Activity> activityProvider;

    @Inject
    public ActivityHelper() { }

    public BaseActivity getActivity() {
        return (BaseActivity)activityProvider.get();
    }

    public View findViewInActivity(int id) {
        return getActivity().findViewById(id);
    }

    // These need an activity reference because they may be used in oncreate
    public int getWindowWidth(final Activity a) {
        Point size = getSize(a);
        return size.x;
    }

    public int getWindowHeight(final Activity a) {
        Point size = getSize(a);
        return size.y;
    }

    private Point getSize(final Activity a) {
        Display display = a.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }



}
