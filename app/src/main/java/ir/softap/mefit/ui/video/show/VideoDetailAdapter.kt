package ir.softap.mefit.ui.video.show

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.softap.mefit.R
import ir.softap.mefit.data.model.Comment
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.ui.abstraction.BaseRecyclerAdapter
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.ListStateItemViewHolder
import ir.softap.mefit.ui.common.adapter.LinearVideoAdapter
import ir.softap.mefit.ui.common.decoration.EqualSpacingItemDecoration
import ir.softap.mefit.utilities.extensions.TAG
import ir.softap.mefit.utilities.extensions.calculateDifference
import ir.softap.mefit.utilities.extensions.toPx
import kotlinx.android.synthetic.main.item_add_comment.view.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_video_detail.view.*

class VideoDetailAdapter(
    private val retry: () -> Unit,
    private val videoSelect: (Video) -> Unit,
    private val like: (Video) -> Unit,
    private val issuerSelect: (Video.Issuer) -> Unit,
    private val tagSelect: (Video.Tag) -> Unit,
    private val scrollToComment: () -> Unit,
    private val addComment: () -> Unit
) : BaseRecyclerAdapter<Comment, RecyclerView.ViewHolder>() {

    var listState: ListState = ListState.IDLE
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var videoDetail: Triple<Video, Video.VideoDetail, List<Video>>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::layoutInflater.isInitialized)
            layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_STATE -> ListStateItemViewHolder.create(
                layoutInflater, parent, true, false
            )
            VIEW_TYPE_VIDEO_DETAIL -> VideoDetailViewHolder(
                layoutInflater.inflate(
                    R.layout.item_video_detail,
                    parent,
                    false
                )
            )
            VIEW_TYPE_ADD_COMMENT -> AddCommentViewHolder(
                layoutInflater.inflate(
                    R.layout.item_add_comment,
                    parent,
                    false
                )
            )
            VIEW_TYPE_NO_COMMENT -> NoCommentViewHolder(
                layoutInflater.inflate(
                    R.layout.item_no_comment, parent,
                    false
                )
            )
            VIEW_TYPE_COMMENT -> CommentViewHolder(
                layoutInflater.inflate(
                    R.layout.item_comment, parent,
                    false
                )
            )
            else -> throw IllegalStateException("${TAG()}, wrong viewType ($viewType)")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListStateItemViewHolder -> holder.onBind(listState, retry = retry)
            is AddCommentViewHolder -> holder.onBind()
            is NoCommentViewHolder -> holder.onBind()
            is VideoDetailViewHolder -> holder.onBind()
            is CommentViewHolder -> holder.onBind(position.minus(2))
        }
    }

    override fun getItemCount(): Int = super.getItemCount().plus(1).plus(if (listState == ListState.SUCCESS) 1 else 0)

    override fun getItemViewType(position: Int): Int =
        if (position == 0) if (hasListState()) VIEW_TYPE_STATE else VIEW_TYPE_VIDEO_DETAIL
        else if (position == 1)
            if (listState.itemCount == 0) VIEW_TYPE_NO_COMMENT else VIEW_TYPE_ADD_COMMENT
        else VIEW_TYPE_COMMENT

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is CommentViewHolder) holder.onRecycled()
        super.onViewRecycled(holder)
    }

    private fun hasListState() = listState in arrayOf(ListState.LOADING, ListState.ERROR)

    inner class VideoDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind() {
            if (videoDetail != null) {
                with(itemView) {
                    tvCommentCount.text = videoDetail!!.second.commentsCount.toString()
                    tvLikeCount.text = videoDetail!!.first.like.count.toString()
                    cbLike.isChecked = videoDetail!!.first.like.isUserLiked
                    cbLike.isEnabled = !videoDetail!!.first.like.isUserLiked
                    tvVideoTitle.text = videoDetail!!.first.title
                    tvIssuerName.text = videoDetail!!.first.issuer.let { "${it.firstName ?: ""} ${it.lastName ?: ""}" }
                    tvIssuerName.setOnClickListener { issuerSelect(videoDetail!!.first.issuer) }
                    cbLike.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            like(videoDetail!!.first)
                            cbLike.isEnabled = false
                        }
                    }
                    btnComments.setOnClickListener { scrollToComment() }

                    with(lstTag) {
                        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                        addItemDecoration(
                            EqualSpacingItemDecoration(
                                2.toPx,
                                EqualSpacingItemDecoration.HORIZONTAL,
                                true
                            )
                        )
                        adapter = TagAdapter(tagSelect).also { it.submitList(videoDetail?.first?.tags) }
                    }
                    with(descriptionView) {
                        descriptions = videoDetail!!.second.descriptions
                        premiumDescription = videoDetail!!.second.premiumDescription
                    }
                    with(lstSuggestedVideo) {
                        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                        addItemDecoration(
                            EqualSpacingItemDecoration(
                                8.toPx,
                                EqualSpacingItemDecoration.HORIZONTAL,
                                true
                            )
                        )
                        adapter = LinearVideoAdapter(videoSelect).also { it.submitList(videoDetail!!.third) }
                    }
                }
            }
        }

    }

    inner class AddCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind() {
            with(itemView) {
                btnAddComment.setOnClickListener { addComment() }
            }
        }

    }

    inner class NoCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind() {
            with(itemView) {
                btnAddComment.setOnClickListener { addComment() }
            }
        }

    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val comment = getItem(position)
            with(itemView) {
                tvName.text = comment.user.completeName
                tvComment.text = comment.text
                tvCommentDate.text = comment.createdDate.calculateDifference(context)
            }
        }

        fun onRecycled() {
            with(itemView) {
                tvName.text = ""
                tvComment.text = ""
                tvCommentDate.text = ""
            }
        }

    }

    companion object {
        const val VIEW_TYPE_STATE = 1
        const val VIEW_TYPE_VIDEO_DETAIL = 2
        const val VIEW_TYPE_ADD_COMMENT = 3
        const val VIEW_TYPE_NO_COMMENT = 4
        const val VIEW_TYPE_COMMENT = 5
    }

}
