package br.com.i9algo.taxiadv.ui.fragments;

import br.com.i9algo.taxiadv.v2.models.viewmodel.GridItemViewModel;

public interface GrigFragmentDelegate {
    void delegateShowItem(int category, GridItemViewModel model);
}
