package br.com.i9algo.taxiadv.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.utils.GlideLoaderUtil;
import br.com.i9algo.taxiadv.v2.views.BaseFragment;

public class ImageFragment extends BaseFragment {

    private ImageView imgView;
    private ProgressBar progressBar;
    private String mUrl;

    public ImageFragment() {
        super();
    }

    public void setModel(String url) {
        mUrl = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        imgView = (ImageView) view.findViewById(R.id.view_content);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        load();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void load() {
        progressBar.setVisibility(View.VISIBLE);

        //UniversalLoaderUtil.loadImageView(getActivity(), mUrl, imgView, progressBar);
        GlideLoaderUtil.loadImageView(this.getContext(), mUrl, imgView);
    }
}
