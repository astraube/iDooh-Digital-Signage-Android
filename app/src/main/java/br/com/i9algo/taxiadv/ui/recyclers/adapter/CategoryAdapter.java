package br.com.i9algo.taxiadv.ui.recyclers.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.ui.recyclers.CategoryListView;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.models.inbound.Category;
import br.com.i9algo.taxiadv.v2.utils.GlideLoaderUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final String LOG_TAG = getClass().getSimpleName();

    private List<Category> mCategories;
    private Map<Object, Category> mCategoriesObj;
    private OnClickDelegate mNavigationDelegator;

    private int rowLayout;
    private Context mContext;

    /*public CategoryAdapter(List<Category> list, int rowLayout, Context context) {
        this(list, rowLayout, context, null);
    }*/

    /*public CategoryAdapter(List<Category> list, int rowLayout, Context context, OnClickDelegate navigationDelegator) {
        this.mCategories = list;
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.mNavigationDelegator = navigationDelegator;
    }*/
    public CategoryAdapter(Map<Object, Category> categories, int rowLayout, Context context, OnClickDelegate navigationDelegator, CategoryListView listView) {
        this.mCategoriesObj = categories;

        setListObjects(this.mCategoriesObj, listView);

        this.rowLayout = rowLayout;
        this.mContext = context;
        this.mNavigationDelegator = navigationDelegator;
    }

    public Category getCategoryById(int position) {
        if (mCategories == null)
            return null;

        return mCategories.get(position);
    }
    public Category getCategoryByKey(Object key) {
        if (mCategoriesObj == null)
            return null;

        return mCategoriesObj.get(key);
    }

    private void updateUI(final CategoryListView listView){
        notifyDataSetChanged();
        listView.setVisibility(View.VISIBLE);
        listView.refreshDrawableState();
        listView.invalidate();
    }

    public void setListObjects(Map<Object, Category> categories, final CategoryListView listView) {
        mCategoriesObj = categories;
        if (categories != null && categories.size() > 0) {
            Collection<Category> values = categories.values();
            mCategories = new ArrayList<Category>(values);

            updateUI(listView);
        } else {
            listView.setVisibility(View.GONE);
        }
    }
    public void removeObject(Object key, final CategoryListView listView) {
        Category catR = mCategoriesObj.get(key);
        mCategories.remove(catR);
        mCategoriesObj.remove(key);
        /*for (Category c : mCategories){
            mCategories.remove(2);
            Logger.d(LOG_TAG, "@@@@@ onChildChanged " + c.getKey());
            if (c.getKey() == key) {

            }
        }*/
        updateUI(listView);
    }
    public void addObject(Object key, Category category, final CategoryListView listView) {
        mCategoriesObj.put(key, category);
        setListObjects(mCategoriesObj, listView);
    }
    public void changeObject(Object key, Category category, final CategoryListView listView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mCategoriesObj.replace(key, mCategoriesObj.get(key), category);
        } else {
            mCategoriesObj.remove(key);
            mCategoriesObj.put(key, category);

            for (Category c : mCategories){
                //Logger.d(LOG_TAG, "@@@@@ old changeObject " + c.toString());
                if (c.getId() == category.getId()) {
                    int index = mCategories.indexOf(c);
                    //Logger.d(LOG_TAG, "@@@@@ indexOf " + index);
                    mCategories.set(index, category);
                    setListObjects(mCategoriesObj, listView);
                }
            }
        }
    }

    public void clearData() {
        if (mCategories != null)
            mCategories.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        try {
            final Category currentArticle = mCategories.get(position);

            //Logger.d(LOG_TAG, "@@@@@ onBindViewHolder " + currentArticle.toString());

            viewHolder.setModel(currentArticle);

            if (mNavigationDelegator != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Logger.i(LOG_TAG, "---# onClick");
                        mNavigationDelegator.delegateNavigationClickAction(currentArticle);
                    }
                });
            }

        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        if (mCategories == null)
            return 0;

        if (position >= mCategories.size()) {
            position = position % mCategories.size();
        }

        return position;
    }

    @Override
    public int getItemCount() {
        return mCategories == null ? 0 : mCategories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Category catModel;
        GradientDrawable cardGradientDrawable;

        @BindView(R.id.card) RelativeLayout card;
        @BindView(R.id.imgCover) ImageView imgCover;
        @BindView(R.id.txtLabel) TextView txtLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cardGradientDrawable = new GradientDrawable();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                cardGradientDrawable.setCornerRadius(8f);
                card.setBackground(cardGradientDrawable);
            }
        }

        public void setModel(Category catModel) {
            this.catModel = catModel;

            this.txtLabel.setText(this.catModel.getName());
            this.txtLabel.setTextSize(18);
            this.txtLabel.setTextColor(mContext.getResources().getColor(R.color.white));

            this.imgCover.setPadding(10, 20, 0, 0);
            this.imgCover.setAlpha(0.4f);
            this.imgCover.setBackgroundResource(android.R.color.transparent);
            //this.imgCover.setColorFilter(mContext.getResources().getColor(R.color.white));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                /*this.cardGradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                this.cardGradientDrawable.setColors(new int[] {
                        mContext.getResources().getColor(currentArticle.color),
                        mContext.getResources().getColor(currentArticle.color)
                });*/
                this.cardGradientDrawable.setColor( Color.parseColor(this.catModel.getBgColor()) );
            }

            GlideLoaderUtil.loadImageMemoryCache(mContext, this.catModel.getIconURL(), this.imgCover,
                    R.drawable.ic_logo_white_40x, 0, R.drawable.ic_logo_white_40x);
        }
        public Category getModel() { return this.catModel; }
    }

    public interface Delegate {
        Context getContext();
    }
    public interface OnClickDelegate {
        void delegateNavigationClickAction(Category item);
    }
}