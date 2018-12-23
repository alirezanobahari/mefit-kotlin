package ir.softap.mefit.ui.main.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.softap.mefit.R
import ir.softap.mefit.data.model.Category
import ir.softap.mefit.data.model.Category.Companion.CATEGORY_DIFF_CALLBACK
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.ListStateItemViewHolder
import ir.softap.mefit.ui.common.glide.GlideApp
import ir.softap.mefit.utilities.extensions.TAG
import kotlinx.android.synthetic.main.abc_activity_chooser_view.view.*
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter(
    private val fragment: Fragment,
    private val retry: () -> Unit,
    private val selectCategory: (Category) -> Unit
) : ListAdapter<Category, RecyclerView.ViewHolder>(CATEGORY_DIFF_CALLBACK) {

    companion object {
        const val VIEW_TYPE_STATE = 1
        const val VIEW_TYPE_CATEGORY = 2
    }

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
            VIEW_TYPE_STATE -> ListStateItemViewHolder.create(layoutInflater, parent)
            VIEW_TYPE_CATEGORY -> CategoryViewHolder(layoutInflater.inflate(R.layout.item_category, parent, false))
            else -> throw IllegalStateException("${TAG()}, wrong ViewType ($viewType)")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListStateItemViewHolder -> holder.onBind(listState, R.string.msg_not_category_found, retry)
            is CategoryViewHolder -> holder.onBind(position)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is CategoryViewHolder) holder.onRecycled()
        super.onViewRecycled(holder)
    }

    private fun hasExtraRow() = listState == ListState.ERROR ||
            (listState == ListState.SUCCESS && listState.itemCount + super.getItemCount() == 0)

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            VIEW_TYPE_STATE
        } else {
            VIEW_TYPE_CATEGORY
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val category = getItem(position)
            with(itemView) {
                tvCategoryTitle.text = category.title
                GlideApp.with(fragment)
                    .load(category.thumbnail)
                    .into(imgCategoryPhoto)
                setOnClickListener { selectCategory(category) }
            }
        }

        fun onRecycled() {
            with(itemView) {
                tvCategoryTitle.text = ""
                GlideApp.with(fragment).clear(image)
                setOnClickListener(null)
            }
        }

    }

}