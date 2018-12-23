package ir.softap.mefit.ui.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class EqualSpacingItemDecoration @JvmOverloads constructor(
    private val spacing: Int = 0,
    private var displayMode: Int = -1,
    private var includeEdges: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildViewHolder(view).adapterPosition
        val itemCount = state.itemCount
        val layoutManager = parent.layoutManager
        setSpacingForDirection(outRect, layoutManager, position, itemCount)
    }

    private fun setSpacingForDirection(
        outRect: Rect,
        layoutManager: RecyclerView.LayoutManager?,
        position: Int,
        itemCount: Int
    ) {

        val isRtl = layoutManager?.layoutDirection == View.LAYOUT_DIRECTION_RTL
        // Resolve display mode automatically
        if (displayMode == -1) {
            displayMode = resolveDisplayMode(layoutManager)
        }

        when (displayMode) {
            HORIZONTAL -> {
                if (isRtl) {
                    if (includeEdges) {
                        outRect.right = if (position == 0) spacing else 0
                        outRect.left = spacing
                    } else {
                        outRect.left = if (position == itemCount - 1) 0 else spacing
                    }
                } else {
                    if (includeEdges) {
                        outRect.left = if (position == 0) spacing else 0
                        outRect.right = spacing
                    } else {
                        outRect.right = if (position == itemCount - 1) 0 else spacing
                    }
                }
            }
            VERTICAL -> {
                if (includeEdges) {
                    outRect.top = if (position == 0) spacing else 0
                    outRect.bottom = spacing
                } else {
                    outRect.bottom = if (position == itemCount - 1) spacing else 0
                }
            }
            GRID -> if (layoutManager is GridLayoutManager) {
                val gridLayoutManager = layoutManager as GridLayoutManager?
                val cols = gridLayoutManager!!.spanCount
                val col = position % cols
                val rows = itemCount / cols

                if (includeEdges) {
                    outRect.left = spacing - col * spacing / cols // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = spacing - col * spacing / cols
                    /*if (position < cols) { // top edge
                        outRect.top = spacing
                    }
                    outRect.bottom = spacing // item bottom*/
                }

               /* outRect.left = spacing
                outRect.right = if (position % cols == cols - 1) spacing else 0
                outRect.top = spacing
                outRect.bottom = if (position / cols == rows - 1) spacing else 0*/
            }
        }
    }

    private fun resolveDisplayMode(layoutManager: RecyclerView.LayoutManager?): Int {
        if (layoutManager is GridLayoutManager) return GRID
        return if (layoutManager!!.canScrollHorizontally()) HORIZONTAL else VERTICAL
    }

    companion object {

        val HORIZONTAL = 0
        val VERTICAL = 1
        val GRID = 2
    }
}