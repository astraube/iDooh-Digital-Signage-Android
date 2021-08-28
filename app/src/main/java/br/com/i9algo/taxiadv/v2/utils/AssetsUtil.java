package br.com.i9algo.taxiadv.v2.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by andre on 15/02/2016.
 */
public class AssetsUtil {

    private static final String[] ASSETS_CONFIG = {"file:///android_asset/", "assets://", "//assets/", "android.resource//"};

    public static boolean isAssetFile(String url) {
        boolean result = false;

        if(url.contains(ASSETS_CONFIG[0]) || url.contains(ASSETS_CONFIG[1]) || url.contains(ASSETS_CONFIG[2]) || url.contains(ASSETS_CONFIG[3]))
            result = true;

        return result;
    }

    public static String getPathFile(String url) {
        String result = url;
        for (String as : ASSETS_CONFIG) {
            if(url.contains(as)) {
                result = url.replace(as, "");
                return result;
            }
        }
        return result;
    }
}
