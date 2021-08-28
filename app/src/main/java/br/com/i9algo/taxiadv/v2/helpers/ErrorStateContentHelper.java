package br.com.i9algo.taxiadv.v2.helpers;


import java.util.ArrayList;
import java.util.List;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.domain.enums.AdvType;
import br.com.i9algo.taxiadv.v2.models.inbound.CategoriesArraylistWrapper;
import br.com.i9algo.taxiadv.v2.models.inbound.Category;
import br.com.i9algo.taxiadv.v2.models.inbound.Programming;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowPlaylist;
import br.com.i9algo.taxiadv.v2.storage.firebase.RemoteConfigs;

public class ErrorStateContentHelper {

    public static final String URL_BASE_DEMO_CONTEND = "http://taxiadv.com.br/api/demo/";

    public static Programming getErrorStateProgramming() {
        Programming programming = new Programming();
        ArrayList<SlideshowPlaylist> playlist_array = new ArrayList<>();
        ArrayList<SlideshowItem> item_array = new ArrayList<>();

        SlideshowItem item;

        Logger.i("ErrorStateContentHelper", "getErrorStateProgramming");

        item = new SlideshowItem();
        item.setTitle("error_slide_item");
        //item.setMainImageURL("android.resource://br.com.i9algo.taxiadv/" + R.raw.placeholder_marca);
        //item.setMainImage(AssetsUtil.getPathFile("assets://placeholder_marca.jpg"));
        //item.setMainImage("assets://placeholder_marca.jpg");
        item.setMainImage("android.resource://br.com.i9algo.taxiadv/" + R.raw.slide_no_content);
        item.setType(AdvType.VIDEO.toString());

        item.setOrder(1);
        item.setPlaylistId(1);
        item.setId(1);
        item.setActionModelId(0);
        item.setExibitionTime(30);
        item_array.add(item);

        SlideshowPlaylist playlist = new SlideshowPlaylist();
        playlist.setItems(item_array);
        playlist.setStartAt(DateFormatHelper.getTodaysDateAsString());
        playlist.setExpiresAt(DateFormatHelper.getTodaysDateAsString());
        playlist.setStartTime("0");
        playlist.setEndTime("0");
        playlist.setId(1);
        playlist_array.add(playlist);
        programming.setData(playlist_array);

        return programming;
    }

    public static CategoriesArraylistWrapper getErrorStateCategories() {
        Logger.e("error", "fetching no content category");
        List<Category> categorias = new ArrayList<>();
        Category category = new Category();
        category.setId(0);
        category.setName("Turismo");
        category.setDescription("Pontos turisticos da região");
        category.setIconURL(URL_BASE_DEMO_CONTEND + "categories/ic_turismo.png");
        category.setBgColor(RemoteConfigs.getColorBgCategory());
        category.setCreatedAt("0000-00-00 00:00:00");
        category.setUpdatedAt("0000-00-00 00:00:00");
        categorias.add(category);

        CategoriesArraylistWrapper demoCategories = new CategoriesArraylistWrapper(categorias);
        return demoCategories;
    }
}
