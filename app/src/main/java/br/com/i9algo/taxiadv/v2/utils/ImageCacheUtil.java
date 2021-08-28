package br.com.i9algo.taxiadv.v2.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.concurrent.ExecutionException;

import br.com.i9algo.taxiadv.v2.CustomApplication;

/**
 * Created by aStraube on 20/05/2016.
 */
public class ImageCacheUtil {

    private static RequestOptions glideOptions = null;

    public static RequestOptions getRequestOptions() {
        if (ImageCacheUtil.glideOptions != null)
            return ImageCacheUtil.glideOptions;

        ImageCacheUtil.glideOptions = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        return ImageCacheUtil.glideOptions;
    }

    public static void preloadImage(final String uriImage) {
        Glide.with(CustomApplication.getInstance().getApplicationContext())
                .load(uriImage)
                .apply(ImageCacheUtil.getRequestOptions())
                .preload();
    }
    public static void loadImage(final String uriImage, final ImageView imgView) {

        Glide.with(CustomApplication.getInstance().getApplicationContext())
                .load(uriImage)
                .apply(ImageCacheUtil.getRequestOptions())
                .into(imgView);
    }
    public static File getCachePathFile(final String urlImg) {
        FutureTarget<File> futureFile = Glide.with(CustomApplication.getInstance().getApplicationContext())
                .asFile()
                .load(urlImg)
                .apply(ImageCacheUtil.getRequestOptions())
                .submit();

        if (futureFile != null) {
            try {
                return futureFile.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
