package br.com.i9algo.taxiadv.v2.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import br.com.i9algo.taxiadv.R;

/**
 * Created by aStraube on 11/07/2016.
 */
public class GlideLoaderUtil {

    public static void loadImageView(Context context, String url, ImageView imgView) {
        // TODO - colocar uma imagem de falha e placeholder
        loadImageView(context, url, imgView, R.drawable.placeholder_marca, 0, R.drawable.placeholdercover);
    }
    public static void loadImageView(Context context, String url, ImageView imgView,
                                     int imageFail, int imageOnLoading, int placeholder) {

        RequestOptions glideOptions = new RequestOptions()
                //.centerCrop()
                .placeholder(placeholder)
                .fallback(imageFail)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true);

        Glide.with(context)
                .load(url)
                .apply(glideOptions)
                .into(imgView);
    }

    public static void loadImagePlaylist(Context context, String url, ImageView imgView) {
        RequestOptions glideOptions = new RequestOptions()
                //.centerCrop()
                .placeholder(R.drawable.placeholder_marca)
                .fallback(R.drawable.placeholder_marca)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false);

        Glide.with(context)
                .load(url)
                .apply(glideOptions)
                .into(imgView);
    }

    public static void loadImageMemoryCache(Context context, String url, ImageView imgView) {
        GlideLoaderUtil.loadImageMemoryCache(context, url, imgView, R.drawable.placeholder_marca, 0, R.drawable.placeholdercover);
    }

    public static void loadImageMemoryCache(Context context, String url, ImageView imgView,
                                            int imageFail, int imageOnLoading, int placeholder) {
        RequestOptions glideOptions = new RequestOptions()
                //.centerCrop()
                .placeholder(placeholder)
                .fallback(imageFail)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false);

        Glide.with(context)
                .load(url)
                .apply(glideOptions)
                .into(imgView);
    }

    public static void cleanCache(Context context) {
        Glide.get(context).clearDiskCache();
    }
}
