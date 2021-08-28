package br.com.i9algo.taxiadv.v2.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;

import br.com.i9algo.taxiadv.v2.CustomApplication;

/**
 * Created by aStraube on 16/07/2016.
 */
@GlideModule
public final class CustomAppGlideModule extends AppGlideModule {

    // Disable manifest parsing to avoid adding similar modules twice.
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        //builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
        /*RequestOptions glideOptions = new RequestOptions()
                //.centerCrop()
                //.format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.drawable.placeholder)
                .fallback(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true);
        builder.setDefaultRequestOptions(glideOptions);*/

        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        builder.setMemoryCache ( new LruResourceCache(defaultMemoryCacheSize));
        builder.setBitmapPool ( new LruBitmapPool(defaultBitmapPoolSize));

        builder.setDiskCache(new DiskCache.Factory() {
            @Override public DiskCache build() {
                // set size & external vs. internal
                int cacheSize100MegaBytes = 90485760;

                File cacheLocation = CustomApplication.get(context).getCacheDir();
                cacheLocation.mkdirs();
                return DiskLruCacheWrapper.get(cacheLocation, cacheSize100MegaBytes);
            }
        });
    }
}