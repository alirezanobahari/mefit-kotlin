package ir.softap.mefit.ui.main.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.softap.mefit.R
import ir.softap.mefit.data.model.VIDEO_DIFF_CALLBACK
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.ListStateItemViewHolder
import ir.softap.mefit.ui.common.glide.GlideApp
import ir.softap.mefit.utilities.extensions.TAG
import kotlinx.android.synthetic.main.item_list_video.view.*

class VideoListAdapter(
    private val retry: () -> Unit,
    private val videoSelect: (Video) -> Unit
) : ListAdapter<Video, RecyclerView.ViewHolder>(VIDEO_DIFF_CALLBACK) {

    companion object {
        const val VIEW_TYPE_STATE = 1
        const val VIEW_TYPE_VIDEO = 2
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
            VIEW_TYPE_VIDEO -> VideoViewHolder(layoutInflater.inflate(R.layout.item_grid_video, parent, false))
            else -> throw IllegalStateException("${TAG()}, wrong viewType ($viewType)")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListStateItemViewHolder -> holder.onBind(listState, R.string.msg_no_video_found, retry)
            is VideoViewHolder -> holder.onBind(position)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is VideoViewHolder) holder.onRecycled()
        super.onViewRecycled(holder)
    }

    private fun hasExtraRow() = listState == ListState.ERROR ||
            (listState == ListState.SUCCESS && listState.itemCount + super.getItemCount() == 0)

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            VIEW_TYPE_STATE
        } else {
            VIEW_TYPE_VIDEO
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(position: Int) {
            val video = getItem(position)
            with(itemView) {
                tvVideoTitle.text = video!!.title
                tvVideoDuration.text = video.duration
                cbLike.isChecked = video.like.isUserLiked
                tvLikeCount.text = video.like.count.toString()
                GlideApp.with(context)
                    .load(video.thumbnail)
                    .into(imgVideoThumbnail)
                setOnClickListener { videoSelect(video) }
            }
        }

        fun onRecycled() {
            with(itemView) {
                tvVideoTitle.text = ""
                tvVideoDuration.text = ""
                cbLike.isChecked = false
                tvLikeCount.text = ""
                GlideApp.with(context).clear(imgVideoThumbnail)
            }
        }
    }


}