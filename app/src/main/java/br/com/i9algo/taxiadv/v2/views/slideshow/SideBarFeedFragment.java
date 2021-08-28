package br.com.i9algo.taxiadv.v2.views.slideshow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.github.jorgecastillo.State;
import com.github.jorgecastillo.listener.OnStateChangeListener;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.views.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SideBarFeedFragment extends BaseFragment implements OnStateChangeListener {

    private static final String LOG_TAG = SideBarFeedFragment.class.getSimpleName();


    @BindView(R.id.viewSwitcher)
    ViewSwitcher viewSwitcher;

    public SideBarFeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Logger.v("SideBarAdvertFragment", LOG_TAG" + "ID category: " + this.category + " - ID item: " + this.id);
        final View view = inflater.inflate(R.layout.content_sidebar_feed, container, false);
        ButterKnife.bind(SideBarFeedFragment.this, view);
        

        return view;
    }

    @Override
    public void onStateChange(int state) {
        switch (state) {
            case State.NOT_STARTED:
                //Logger.v(LOG_TAG", "State: NOT_STARTED");
                break;
            case State.STROKE_STARTED:
                //Logger.v(LOG_TAG", "State: STROKE_STARTED");
                break;
            case State.FILL_STARTED:
                //Logger.v(LOG_TAG", "State: FILL_STARTED");
                break;
            case State.FINISHED:
                //Logger.v(LOG_TAG", "State: FINISHED");
                viewSwitcher.showNext();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.v(LOG_TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.v(LOG_TAG, "onDestroy");
    }
}

