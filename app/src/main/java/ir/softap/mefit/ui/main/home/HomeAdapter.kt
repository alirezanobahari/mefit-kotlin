package ir.softap.mefit.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import ir.softap.mefit.R
import ir.softap.mefit.data.model.*
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.ListStateItemViewHolder
import ir.softap.mefit.ui.common.decoration.EqualSpacingItemDecoration
import ir.softap.mefit.ui.common.glide.GlideApp
import ir.softap.mefit.utilities.extensions.TAG
import ir.softap.mefit.utilities.extensions.toPx
import kotlinx.android.synthetic.main.item_home_section_slider.view.*
import kotlinx.android.synthetic.main.item_home_section_video.view.*
import kotlinx.android.synthetic.main.item_home_slider.view.*
import kotlinx.android.synthetic.main.item_list_video.view.*

class HomeAdapter(
    private val fragment: Fragment,
    private val retry: () -> Unit,
    private val homeSelect: (Home) -> Unit,
    private val videoSelect: (Video) -> Unit
) : ListAdapter<Home, RecyclerView.ViewHolder>(HOME_DIFF_CALLBACK) {

    companion object {
        private const val VIEW_TYPE_STATE = 1
        private const val VIEW_TYPE_SLIDER = 2
        private const val VIEW_TYPE_HOME = 3
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

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::layoutInflater.isInitialized)
            layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_STATE -> ListStateItemViewHolder.create(layoutInflater, parent)
            VIEW_TYPE_SLIDER -> HomeSlideShowViewHolder(
                layoutInflater.inflate(
                    R.layout.item_home_section_slider,
                    parent,
                    false
                )
            )
            VIEW_TYPE_HOME -> HomeItemViewHolder(
                layoutInflater.inflate(
                    R.layout.item_home_section_video,
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException("${TAG()} : wrong viewType ($viewType)!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListStateItemViewHolder -> holder.onBind(listState, R.string.msg_no_video_found, retry)
            is HomeSlideShowViewHolder -> holder.onBind(position)
            is HomeItemViewHolder -> holder.onBind(position)
        }
    }

    private fun hasExtraRow() = listState == ListState.ERROR ||
            (listState == ListState.SUCCESS && listState.itemCount + super.getItemCount() == 0)

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            VIEW_TYPE_STATE
        } else {
            if (getItem(position).title == SLIDE_SHOW_KEY) VIEW_TYPE_SLIDER
            else VIEW_TYPE_HOME
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is HomeSlideShowViewHolder -> holder.onRecycled()
            is HomeItemViewHolder -> holder.onRecycled()
        }
        super.onViewRecycled(holder)
    }

    inner class HomeSlideShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val home = getItem(position)
            with(itemView.lstHomeSlider) {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter = SliderAdapter().apply { submitList(home.videos) }
                PagerSnapHelper().attachToRecyclerView(this)
            }
        }

        fun onRecycled() {
            with(itemView.lstHomeSlider) {
                layoutManager = null
                adapter = null
            }
        }

        inner class SliderAdapter : ListAdapter<Video, SliderAdapter.SliderViewHolder>(VIDEO_DIFF_CALLBACK) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder =
                SliderViewHolder(layoutInflater.inflate(R.layout.item_home_slider, parent, false))

            override fun onBindViewHolder(holder: SliderViewHolder, position: Int) = holder.onBind(position)

            override fun onViewRecycled(holder: SliderViewHolder) {
                holder.onRecycled()
                super.onViewRecycled(holder)
            }

            inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

                fun onBind(position: Int) {
                    val video = getItem(position)
                    with(itemView) {
                        tvSliderTitle.text = video.title
                        cbSliderLike.isChecked = video.like.isUserLiked
                        tvSliderLikeCount.text = video.like.count.toString()
                        GlideApp.with(fragment)
                            .load(video.thumbnail)
                            .into(imgSliderThumbnail)
                        setOnClickListener { videoSelect(video) }
                    }
                }

                fun onRecycled() {
                    with(itemView) {
                        tvSliderTitle.text = ""
                        cbSliderLike.isChecked = false
                        tvSliderLikeCount.text = ""
                        GlideApp.with(fragment).clear(imgSliderThumbnail)
                    }
                }
            }
        }

    }

    inner class HomeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val home = getItem(position)
            with(itemView) {
                tvVideoSectionTitle.text = home.title
                btnMore.setOnClickListener { homeSelect(home) }
                with(lstHomeSectionVideo) {
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    addItemDecoration(EqualSpacingItemDecoration(16.toPx, EqualSpacingItemDecoration.HORIZONTAL))
                    adapter = VideoAdapter().apply { submitList(home.videos) }
                }
            }
        }

        fun onRecycled() {
            with(itemView) {
                tvVideoSectionTitle.text = ""
                btnMore.setOnClickListener(null)
                with(lstHomeSectionVideo) {
                    layoutManager = null
                    adapter = null
                    removeItemDecorationAt(0)
                }
            }
        }

        inner class VideoAdapter : ListAdapter<Video, VideoAdapter.VideoViewHolder>(VIDEO_DIFF_CALLBACK) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder =
                VideoViewHolder(
                    layoutInflater.inflate(
                        R.layout.item_list_video,
                        parent,
                        false
                    )
                )

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
                        GlideApp.with(fragment)
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
                        GlideApp.with(fragment).clear(imgVideoThumbnail)
                    }
                }
            }
        }
    }

}