package br.com.i9algo.taxiadv.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.domain.enums.AdvType;
import br.com.i9algo.taxiadv.v2.models.viewmodel.GridItemViewModel;
import br.com.i9algo.taxiadv.v2.utils.AssetsUtil;
import br.com.i9algo.taxiadv.v2.utils.ImageCacheUtil;
import br.com.i9algo.taxiadv.v2.views.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadMoreFragment extends BaseFragment {

	private final String LOG_TAG = getClass().getSimpleName();

	@BindView(R.id.view_content)
	ImageView imgView;

	@BindView(R.id.progressBar)
    ProgressBar progressBar;

	@BindView(R.id.titleView)
    TextView titleView;

	@BindView(R.id.webView)
    WebView mWebView;

	@BindView(R.id.scrollView)
	ScrollView scrollView;

	@BindView(R.id.imgScrollView)
	ImageView imgScrollView;

    private GridItemViewModel mObjectModel;

	@Inject
    public ReadMoreFragment () { super();   }

	public void setModel (GridItemViewModel advModel) {

		// Util para enviar relatorios para Google Analytics
		// e identificar qual fragment esta ativa
		setName(advModel.getItemName());
		this.mObjectModel = advModel;
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_more, container, false);
		ButterKnife.bind(ReadMoreFragment.this, view);
        
        imgView.setVisibility(View.VISIBLE);
		imgView.setDrawingCacheEnabled(true);
        
        load();

        
        return view;
    }

    public void load()  {
		AdvType type = (!TextUtils.isEmpty(this.mObjectModel.getType())) ? AdvType.valueOf(this.mObjectModel.getType().toUpperCase()) : AdvType.DEFAULT;

    	if (type.equals(AdvType.HTML)) {
    		mWebView.loadUrl(this.mObjectModel.getURL());
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

    	} else if (type.equals(AdvType.IMAGE) || type.equals(AdvType.DEFAULT)) {
			// Verifica se e uma imagem localizada em ASSETS
			boolean isAsset = AssetsUtil.isAssetFile(this.mObjectModel.getURL());
			if (isAsset) {
				String imgSource = AssetsUtil.getPathFile(this.mObjectModel.getURL());
				loadImgOnWebview(imgSource);
				return;
			}

			File f = ImageCacheUtil.getCachePathFile(this.mObjectModel.getURL());
			if (f != null) {
				loadImgOnWebview(f.getAbsolutePath());
			}
        }
    }

	private void loadImgOnWebview(String cachePath) {
		//Log.v(TAG, "ReadMore.loadImageFromCache: " + cachePath);

		File f = new File(cachePath);

		if (TextUtils.isEmpty(cachePath) || !f.exists()) {
			// TODO -melhorar isso
			cachePath = AssetsUtil.getPathFile("assets://marcasua_marca_aqui.png"); // TODO colocar uma imagem de falha.
		}
		loadWebViewZoom(cachePath);
	}

	/**
	 * Carregar a imagem com o componente de ZoomView
	 * @param imgPath
     */
	private void loadImageViewZoom (final String imgPath) {
		//Log.v(TAG, "ReadMore.loadImageViewZoom: " + imgPath);

		//PhotoViewAttacher mAttacher;
		//Matrix mCurrentDisplayMatrix = null;

		// TODO verificar se a imagem e maior que 4000x4000
		/*if (bitmap.getIntrinsicHeight() > 4000 || bitmap.getIntrinsicWidth() > 4000) {
			Toast.makeText(this, "Imagem muito grande!", Toast.LENGTH_LONG).show();
		}*/

		// Componente ZoomImageView
		//mAttacher = new PhotoViewAttacher(imgView);
		//mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//mAttacher.update();

		/** Carrega a imagem no ImageView
		 *
		 * Existe um problema quanto ao ImageView com o componente de zoom atual
		 * Ele exibe apenas imagens com o tamanho maximo de 4000x4000
		 * Por isso existe o componente "WebViewZoom"
		 */
	}

	/**
	 *
	 * @param imgName - Path da imagem
	 */
	private void loadWebViewZoom (final String imgName) {
		//Log.v(TAG, "ReadMore.loadWebViewZoom: " + imgName);

		mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mWebView.setVisibility(View.VISIBLE);
		mWebView.getSettings().setAppCacheEnabled(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setBuiltInZoomControls(true); //Habilitar zoom
		mWebView.getSettings().setDisplayZoomControls(true); //Exibir controles de zoom
		mWebView.getSettings().setLoadWithOverviewMode(true);  //Nao sei
		mWebView.getSettings().setUseWideViewPort(true); //Nao sei
		mWebView.setInitialScale(1);
		mWebView.loadUrl("file:///android_asset/index.html");
		mWebView.setWebViewClient(new WebViewClient() {
			private int running = 0; // Could be public if you want a timer to check.

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
				running++;
				view.loadUrl(urlNewString);
				return true;
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				running = Math.max(running, 1); // First request move it to 1.
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				if (--running == 0) { // just "running--;" if you add a timer.
					view.loadUrl("javascript:loadImage(\"" + imgName + "\")");
				}
			}
		});
	}
}
