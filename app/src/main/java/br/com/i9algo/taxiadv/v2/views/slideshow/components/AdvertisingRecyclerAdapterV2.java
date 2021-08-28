package br.com.i9algo.taxiadv.v2.views.slideshow.components;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.models.viewmodel.GridItemViewModel;
import br.com.i9algo.taxiadv.v2.utils.DateUtils;

public class AdvertisingRecyclerAdapterV2 extends RecyclerView.Adapter<AdvertisingRecyclerViewHoldersV2> implements ItemCallbackInterfaceV2 {

    // Lista de objetos para montar o Grid
    private List<GridItemViewModel> itemList;
    private Context context;
    private String mTitle;

    private Delegate delegate;

    public AdvertisingRecyclerAdapterV2(Context context, List<GridItemViewModel> itemList, String titleAdapter) {
        this.itemList = itemList;
        this.context = context;
        this.mTitle = titleAdapter;
    }

    @Override
    public AdvertisingRecyclerViewHoldersV2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_card_view, null);
        AdvertisingRecyclerViewHoldersV2 rcv = new AdvertisingRecyclerViewHoldersV2(layoutView, this);
        return rcv;
    }

    @Override
    public void onBindViewHolder(AdvertisingRecyclerViewHoldersV2 holder, int position) {
        try {
            GridItemViewModel itemViewModel = itemList.get(position);

            holder.setModel(itemViewModel);

            if (! TextUtils.isEmpty( itemViewModel.getItemName() ))
                holder.itemName.setText(itemViewModel.getItemName());

            Date startAt = itemViewModel.getDateStartEventAsDate();
            if (startAt != null) {
                String dt = DateUtils.format(DateUtils.BR_DATE_TIME_PATTERN, startAt);
                holder.itemEventAt.setText(dt);
                holder.itemEventAt.setVisibility(View.VISIBLE);
            }

            Glide.with(context).load(itemViewModel.getCoverImage()).into(holder.itemPhoto);

        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    @Override
    public void onItemClick(AdvertisingRecyclerViewHoldersV2 v) {
            delegate.delegateClickAction(v.getModel());
    }

    public interface Delegate {
        void delegateClickAction(GridItemViewModel gridItemViewModel);
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

}
