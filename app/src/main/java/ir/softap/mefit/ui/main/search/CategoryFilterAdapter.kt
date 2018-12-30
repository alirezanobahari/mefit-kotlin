package ir.softap.mefit.ui.main.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.softap.mefit.R
import ir.softap.mefit.data.model.Category
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.ListStateItemViewHolder
import ir.softap.mefit.ui.common.glide.GlideApp
import ir.softap.mefit.utilities.extensions.TAG
import ir.softap.mefit.utilities.extensions.drawables
import kotlinx.android.synthetic.main.item_category_filter.view.*
import org.apache.commons.lang3.tuple.MutablePair

class CategoryFilterAdapter(
    private val retry: () -> Unit,
    private val categoryFilterSelect: (Pair<Category, Boolean>) -> Unit
) : ListAdapter<MutablePair<Category, Boolean>, RecyclerView.ViewHolder>(CATEGORY_FILTER_DIFF_CALLBACK) {

    var listState: ListState = ListState.IDLE
        set(value) {
            val previousState = field
            val hadExtraRow = hasExtraRow()
            field = value
            val hasExtraRow = hasExtraRow()
            if (hadExtraRow != hasExtraRow) {
                if (hadExtraRow) {
                    notifyItemRemoved(super.getItemCount())
                } else {
                    notifyItemInserted(super.getItemCount())
                }
            } else if (hasExtraRow && previousState != value) {
                notifyItemChanged(itemCount - 1)
            }
        }

    lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::layoutInflater.isInitialized)
            layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_STATE -> ListStateItemViewHolder.create(
                layoutInflater, parent, true, true
            )
            VIEW_TYPE_CATEGORY_FILTER -> CategoryFilterViewHolder(
                layoutInflater.inflate(
                    R.layout.item_category_filter,
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException("${TAG()}, wrong viewType ($viewType)")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListStateItemViewHolder -> holder.onBind(listState, R.string.msg_not_category_found, retry)
            is CategoryFilterViewHolder -> holder.onBind(position)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is CategoryFilterViewHolder) holder.onRecycled()
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int = super.getItemCount().plus(if (hasExtraRow()) 1 else 0)

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            VIEW_TYPE_STATE
        } else {
            VIEW_TYPE_CATEGORY_FILTER
        }
    }

    private fun hasExtraRow() = listState == ListState.ERROR ||
            (listState == ListState.SUCCESS && listState.itemCount == 0)

    inner class CategoryFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val categoryFilter = getItem(position)
            with(itemView) {
                GlideApp.with(context)
                    .load(categoryFilter.left.thumbnail)
                    .into(imgCategoryFilterPhoto)
                tvCategoryFilterTitle.text = categoryFilter.left.title
                viewClickOverlay.background =
                        if (categoryFilter.right) context.drawables[R.drawable.bg_select_drawable] else null
                viewClickOverlay.setOnClickListener {
                    categoryFilterSelect(
                        Pair(
                            categoryFilter.left,
                            !categoryFilter.right
                        )
                    )
                }
            }
        }

        fun onRecycled() {
            with(itemView) {
                GlideApp.with(context).clear(imgCategoryFilterPhoto)
                tvCategoryFilterTitle.text = ""
                viewClickOverlay.background = null
            }
        }

    }

    companion object {

        const val VIEW_TYPE_STATE = 1
        const val VIEW_TYPE_CATEGORY_FILTER = 2

        private val CATEGORY_FILTER_DIFF_CALLBACK = object : DiffUtil.ItemCallback<MutablePair<Category, Boolean>>() {
            override fun areItemsTheSame(
                oldItem: MutablePair<Category, Boolean>,
                newItem: MutablePair<Category, Boolean>
            ): Boolean = oldItem.left.id == newItem.left.id

            override fun areContentsTheSame(
                oldItem: MutablePair<Category, Boolean>,
                newItem: MutablePair<Category, Boolean>
            ): Boolean =
                oldItem.left == newItem.left && oldItem.right == newItem.right

        }
    }
}