package ir.softap.mefit.ui.common.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private GridLayoutManager gridLayoutManager;
    private int mSpanCount;
    private int mSpacing;
    private boolean mIncludeEdge;

    /**
     * Default {@link GridSpacingItemDecoration} constructor
     *
     * @param spanCount
     * @param spacing
     * @param includeEdge
     */
    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        mSpanCount = spanCount;
        mSpacing = spacing;
        mIncludeEdge = includeEdge;
    }

    /**
     * Auto grid spacing {@link GridSpacingItemDecoration} constructor
     *
     * @param context
     * @param spanCount
     * @param itemWidth
     * @param includeEdge
     */
    public GridSpacingItemDecoration(Context context, int spanCount, int itemWidth, boolean includeEdge) {
        mSpanCount = spanCount;
        mSpacing = (context.getResources().getDisplayMetrics().widthPixels - (mSpanCount * itemWidth)) / (mSpanCount + 1);
        mIncludeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        if (gridLayoutManager == null) {
            gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        }
        int position = parent.getChildAdapterPosition(view); // item position
        if (position == RecyclerView.NO_POSITION)
            return;
        int column = gridLayoutManager.getSpanSizeLookup().getSpanIndex(position, mSpanCount);
        int spanSize = gridLayoutManager.getSpanSizeLookup().getSpanSize(position);
        int totalChildCount = gridLayoutManager.getItemCount();
        boolean isRtl = gridLayoutManager.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        boolean isFullSpan = spanSize == mSpanCount;
        boolean isLastRow = spanSize == 1 ?
                position + mSpanCount - column > totalChildCount - 1 :
                position - column / spanSize > totalChildCount - 1;
        boolean isFirstRow = gridLayoutManager.getSpanSizeLookup().getSpanGroupIndex(position, mSpanCount) == 0;

        int left = 0, top = 0, right = 0, bottom = 0;

        if (isFullSpan)
            return;

        if (mIncludeEdge) {
            if (isRtl) {
                outRect.left = mSpacing - column * mSpacing / mSpanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = mSpacing - column * mSpacing / mSpanCount;
            } else {
                outRect.left = mSpacing - column * mSpacing / mSpanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * mSpacing / mSpanCount;
            }
            if (position < mSpanCount) { // top edge
                outRect.top = mSpacing;
            }
            outRect.bottom = mSpacing; // item bottom
        } else {
            if (isRtl) {
                outRect.left = mSpacing - column * mSpacing / mSpanCount;
            } else {
                outRect.right = (column + 1) * mSpacing / mSpanCount;
            }
            if (position >= mSpanCount) {
                outRect.top = mSpacing; // item top
            }
        }

        //  outRect.set(left, top, right, bottom);
    }
}