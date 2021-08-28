package br.com.i9algo.taxiadv.v2.views.slideshow.components;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.models.viewmodel.GridItemViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvertisingRecyclerViewHoldersV2 extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.item_photo)
    public AppCompatImageView itemPhoto;

    @BindView(R.id.item_name)
    public TextView itemName;

    @BindView(R.id.item_event_at)
    public TextView itemEventAt;

    private GridItemViewModel gridItemViewModel;
    private ItemCallbackInterfaceV2 mCallBack = null;

    public AdvertisingRecyclerViewHoldersV2(View itemView, ItemCallbackInterfaceV2 callBack) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mCallBack = callBack;
        itemView.setOnClickListener(this);

    }

    public void setModel(GridItemViewModel advModel) { this.gridItemViewModel = advModel; }
    public GridItemViewModel getModel() { return this.gridItemViewModel; }

    @Override
    public void onClick(View view) {
        mCallBack.onItemClick(this);
    }

}
