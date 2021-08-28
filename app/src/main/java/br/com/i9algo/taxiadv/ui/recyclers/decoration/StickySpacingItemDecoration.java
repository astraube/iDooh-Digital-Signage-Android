package br.com.i9algo.taxiadv.ui.recyclers.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class StickySpacingItemDecoration extends RecyclerView.ItemDecoration {

    /**
     *
     * {@link #startPadding} and {@link #endPadding} are final and required on initialization
     * because  are drawn
     * before the adapter's child views so you cannot rely on the child view measurements
     * to determine padding as the two are connascent
     *
     * see {@see <a href="https://en.wikipedia.org/wiki/Connascence_(computer_programming)"}
     */

    /**
     * @param startPadding
     * @param endPadding
     */
    private final int startPadding;
    private final int endPadding;

    public StickySpacingItemDecoration() {
        this(1, -1);
    }

    public StickySpacingItemDecoration(int startPadding, int endPadding) {
        this.startPadding = startPadding;
        this.endPadding = endPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int totalWidth = parent.getWidth();
        int itemCount = state.getItemCount();

        //first element
        if (position == 0) {
            int firstPadding = (totalWidth - ((startPadding != -1) ? startPadding : totalWidth) ) / 2;
            //int firstPadding = totalWidth / 2 - ((startPadding != -1) ? startPadding : totalWidth) ;
            firstPadding = Math.max(0, firstPadding);
            outRect.set(firstPadding, 0, 0, 0);
        }

        //last element
        if (position == itemCount - 1 && itemCount > 1) {
            int lastPadding = (totalWidth - ((endPadding != -1) ? endPadding : totalWidth) ) / 2;
            lastPadding = Math.max(0, lastPadding);
            outRect.set(0, 0, lastPadding, 0);
        }
    }

}