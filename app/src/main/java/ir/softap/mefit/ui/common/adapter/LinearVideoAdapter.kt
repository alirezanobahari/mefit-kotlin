package ir.softap.mefit.ui.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.softap.mefit.R
import ir.softap.mefit.data.model.VIDEO_DIFF_CALLBACK
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.ui.common.glide.GlideApp
import kotlinx.android.synthetic.main.item_list_video.view.*

class LinearVideoAdapter(private val videoSelect: (Video) -> Unit) :
    ListAdapter<Video, LinearVideoAdapter.VideoViewHolder>(VIDEO_DIFF_CALLBACK) {

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        if (!::layoutInflater.isInitialized)
            layoutInflater = LayoutInflater.from(parent.context)
        return VideoViewHolder(
            layoutInflater.inflate(
                R.layout.item_list_video,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) =
        holder.onBind(position)

    override fun onViewRecycled(holder: VideoViewHolder) {
        holder.onRecycled()
        super.onViewRecycled(holder)
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(position: Int) {
            val video = getItem(position)
            with(itemView) {
                tvVideoTitle.text = video.title
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
