package br.com.i9algo.taxiadv.v2.views.slideshow.sidebar;

import java.util.List;

import br.com.i9algo.taxiadv.v2.models.viewmodel.GridItemViewModel;
import br.com.i9algo.taxiadv.v2.presenters.BasePresenter;
import br.com.i9algo.taxiadv.v2.storage.SidebarDAO;
import br.com.i9algo.taxiadv.v2.views.slideshow.components.AdvertisingRecyclerAdapterV2;
import rx.Observable;

public class SidebarCategoriesPresenter extends BasePresenter<SidebarViewInterface> implements AdvertisingRecyclerAdapterV2.Delegate {

    SidebarDAO dao;

    public SidebarCategoriesPresenter() {
        super();
    }

    public SidebarCategoriesPresenter(SidebarDAO dao) {
        super();
        this.dao = dao;
    }

    @Override
    public void delegateClickAction(GridItemViewModel gridItemViewModel) {
        if (click("GridItemViewModel", gridItemViewModel.getSidebarId())) {
            view.showDetailedItem(gridItemViewModel);
        }
    }

    public Observable<List<GridItemViewModel>> getGridItemListByCategoryId(int id) {
        return dao.getGridItemListByCategory(id);
    }
}
