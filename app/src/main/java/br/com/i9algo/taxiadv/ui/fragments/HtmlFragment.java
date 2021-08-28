package br.com.i9algo.taxiadv.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.domain.enums.Analytics;
import br.com.i9algo.taxiadv.v2.helpers.AnalyticsTrackerProvider;
import br.com.i9algo.taxiadv.v2.views.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HtmlFragment extends BaseFragment {

	private final String LOG_TAG = HtmlFragment.class.getSimpleName();

    private String mHtmlPath = "file:///android_asset/index.html";

    @BindView(R.id.view_content)
    WebView mWebView = null;

    @BindView(R.id.progressBar)
    ProgressBar progressBar = null;

    @Inject
    public HtmlFragment() {
        super();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_html, container, false);
        ButterKnife.bind(HtmlFragment.this, view);

        // Analytics
        AnalyticsTrackerProvider.sendTap(getContext(), Analytics.CategoryName.UI, Analytics.ActionsName.VISUALIZOU_PROPAGANDA, getName(), 1);

        //loadHtml();
        
        return view;
    }

    public void setHtmlPath(String htmlPath) {
        this.mHtmlPath = htmlPath;
        loadHtml();
    }

    private void loadHtml() {
        if (TextUtils.isEmpty(this.mHtmlPath) || mWebView == null)
            return;

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.setVisibility(View.VISIBLE);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(false);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true); //Habilitar zoom
        mWebView.getSettings().setDisplayZoomControls(false); // Exibir controles de zoom
        mWebView.getSettings().setLoadWithOverviewMode(true);  //Nao sei
        mWebView.getSettings().setUseWideViewPort(true); //Nao sei
        mWebView.setInitialScale(1);
        mWebView.loadUrl(this.mHtmlPath);
    }
}
