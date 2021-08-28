package br.com.i9algo.taxiadv.ui.recyclers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.i9algo.taxiadv.ui.recyclers.decoration.StickySpacingItemDecoration;


public class CategoryListView extends RelativeLayout {

    private final String LOG_TAG = getClass().getSimpleName();

    private ProgressBar mListProgress;
    private RecyclerView mRecyclerList;
    private RecyclerView.Adapter mAdapter;

    public CategoryListView(Context context) { this(context, null, 0); }
    public CategoryListView(Context context, AttributeSet attrs) { this(context, attrs, 0); }
    public CategoryListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void initialize() {
        startRefresh();

        ViewGroup.LayoutParams root_LayoutParams = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        root_LayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        root_LayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        this.setLayoutParams(root_LayoutParams);

        // RecyclerView
        mRecyclerList = new RecyclerView(getContext());
        RelativeLayout.LayoutParams mRecyclerList_lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mRecyclerList.setLayoutParams(mRecyclerList_lp);
        this.addView(mRecyclerList);

        // ProgressBar
        mListProgress = new ProgressBar(getContext());
        RelativeLayout.LayoutParams mListProgress_lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mListProgress_lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mListProgress.setLayoutParams(mListProgress_lp);
        this.addView(mListProgress);

        refreshDrawableState();
        invalidate();
        init();
    }

    public void onResume() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    private void init() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerList.setLayoutManager(mLayoutManager);
        //mRecyclerList.addItemDecoration(new EqualSpacingItemDecoration(10, EqualSpacingItemDecoration.HORIZONTAL));
        mRecyclerList.addItemDecoration(new StickySpacingItemDecoration());
        mRecyclerList.setItemAnimator(new DefaultItemAnimator());
        mRecyclerList.setHasFixedSize(true);

        stopRefresh();
    }

    public void setAdapter(@NonNull RecyclerView.Adapter adapter) {
        startRefresh();

        mAdapter = adapter;
        mAdapter.notifyDataSetChanged();
        mRecyclerList.setAdapter(mAdapter);
        mRecyclerList.invalidate();
        mAdapter.notifyDataSetChanged();
        invalidate();

        stopRefresh();
    }

    public void startRefresh() {
        if (mAdapter != null && mRecyclerList != null)
            mRecyclerList.requestLayout();

        if (mListProgress != null)
            mListProgress.setVisibility(View.VISIBLE);
    }

    public void stopRefresh() {
        if (mAdapter != null && mRecyclerList != null)
            mRecyclerList.requestLayout();

        if (mListProgress != null)
            mListProgress.setVisibility(View.GONE);
    }
}