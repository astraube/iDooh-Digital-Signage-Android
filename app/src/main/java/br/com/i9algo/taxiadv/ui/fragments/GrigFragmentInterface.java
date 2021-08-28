package br.com.i9algo.taxiadv.ui.fragments;

import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;
import br.com.i9algo.taxiadv.v2.models.inbound.Category;
import br.com.i9algo.taxiadv.v2.views.BaseFragment;
import br.com.i9algo.taxiadv.v2.views.slideshow.sidebar.SidebarCategoriesPresenter;

/**
 * Created by aStraube on 04/05/2016.
 */
public interface GrigFragmentInterface {

    BaseFragment getFragment();
    RecyclerView getRecyclerView();
    ProgressBar getProgressBar();
    Category getModel();
    GrigFragmentDelegate getDelegate();
    SidebarCategoriesPresenter getPresenter();
}
