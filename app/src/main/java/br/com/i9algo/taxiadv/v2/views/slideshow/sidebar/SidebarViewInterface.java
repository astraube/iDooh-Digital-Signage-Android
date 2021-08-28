package br.com.i9algo.taxiadv.v2.views.slideshow.sidebar;

import br.com.i9algo.taxiadv.v2.models.viewmodel.GridItemViewModel;
import br.com.i9algo.taxiadv.v2.views.BaseViewInterface;

public interface SidebarViewInterface extends BaseViewInterface {
    void showDetailedItem(GridItemViewModel gridItemViewModel);
}
