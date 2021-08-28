package br.com.i9algo.taxiadv.ui.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.event.ResizePlaylistEvent;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.helpers.defaults.DefaultSubscriber;
import br.com.i9algo.taxiadv.v2.models.inbound.Category;
import br.com.i9algo.taxiadv.v2.models.viewmodel.GridItemViewModel;
import br.com.i9algo.taxiadv.v2.storage.SidebarDAO;
import br.com.i9algo.taxiadv.v2.utils.GlideLoaderUtil;
import br.com.i9algo.taxiadv.v2.views.BaseFragment;
import br.com.i9algo.taxiadv.v2.views.slideshow.components.AdvertisingRecyclerAdapterV2;
import br.com.i9algo.taxiadv.v2.views.slideshow.sidebar.SidebarCategoriesPresenter;
import br.com.i9algo.taxiadv.v2.views.slideshow.sidebar.SidebarViewInterface;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GridFragment_V2 extends BaseFragment implements SidebarViewInterface, GrigFragmentInterface {

    private final String LOG_TAG = GridFragment_V2.class.getSimpleName();

    @BindView(R.id.content_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.imgCategory)
    ImageView imgCategory;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.imgCoverCategory)
    ImageView imgCoverCategory;

    private Category category;

    // Gerenciador de Layout Grid
    private GridLayoutManager lLayout;

    private GrigFragmentDelegate delegate;

    SidebarCategoriesPresenter presenter;

    public GridFragment_V2() {
        super();
    }

    @SuppressLint("ValidFragment")
    public GridFragment_V2(SidebarDAO dao) {
        super();
        presenter = new SidebarCategoriesPresenter(dao);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        ButterKnife.bind(this, view);

        presenter.bindView(this);

        // Inicia o gerenciador de Layout em Grid com o numero de colunas
        lLayout = new GridLayoutManager(getActivity(), this.category.getGridColumns());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);

        // Titulo da fragment
        txtTitle.setText(this.category.getName());

        // Carregar Imagem da categoria
        GlideLoaderUtil.loadImageView(this.getContext(), this.category.getIconURL(), imgCategory);

        configureNavigationAdapter();

        return view;
    }

    private void loadCoverCategory() {
        // Carregar imagem de capa
        //Logger.v(LOG_TAG, "@@@@ --> " + this.category.getCoverImage());

        if (!category.getCoverImage().isEmpty()) {
            //Foi criado esse timer porque a CoverImage não estava carregando corretamente no momento do onMinimize da Playlist

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    GlideLoaderUtil.loadImageView(GridFragment_V2.this.getContext(), category.getCoverImage(), imgCoverCategory);
                }
            }, 200);
        }
    }

    private void configureNavigationAdapter() {
        //Logger.i(LOG_TAG, "configureNavigationAdapter onNext");

        presenter.getGridItemListByCategoryId(category.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<List<GridItemViewModel>>() {
                    @Override
                    public void onNext(List<GridItemViewModel> itemList) {
                        super.onNext(itemList);

                        if (itemList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            imgCoverCategory.setVisibility(View.VISIBLE);
                            loadCoverCategory();
                        } else {
                            AdvertisingRecyclerAdapterV2 adapter = new AdvertisingRecyclerAdapterV2(GridFragment_V2.this.getActivity(), itemList, getModel().getName());
                            adapter.setDelegate(presenter);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        Logger.e("Adv", "getGridItemListByCategoryId onCompleted");
                    }
                });
    }

    public void setModel(Category category) {
        setName(category.getName());
        this.category = category;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /********************************
     * GrigFragmentInterface
     ************************************/
    @Override
    public BaseFragment getFragment() {
        return this;
    }

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public Category getModel() {
        return this.category;
    }

    @Override
    public GrigFragmentDelegate getDelegate() {
        return this.delegate;
    }

    @Override
    public SidebarCategoriesPresenter getPresenter() {
        return this.presenter;
    }

    /************************************************************************************/

    @Override
    public void showDetailedItem(GridItemViewModel gridItemViewModel) {
        delegate.delegateShowItem(category.getId(), gridItemViewModel);
    }

    public void setDelegate(GrigFragmentDelegate delegate) {
        this.delegate = delegate;
    }
}
