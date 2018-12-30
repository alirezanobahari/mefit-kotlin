package ir.softap.mefit.ui.video.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXActivity
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.ToastBuilder
import ir.softap.mefit.ui.main.search.VideoListAdapter
import ir.softap.mefit.ui.video.show.VideoShowActivity
import ir.softap.mefit.utilities.extensions.colors
import ir.softap.mefit.utilities.extensions.strings
import kotlinx.android.synthetic.main.activity_video_list.*

class VideoListActivity : DaggerXActivity() {

    companion object {

        private const val EXTRA_TITLE = "ir.softap.mefit.ui.video.list.EXTRA_TITLE"
        private const val EXTRA_CATEGORY_ID = "ir.softap.mefit.ui.video.list.EXTRA_CATEGORY_ID"
        private const val EXTRA_ISSUER_ID = "ir.softap.mefit.ui.video.list.EXTRA_ISSUER_ID"
        private const val EXTRA_TAG_ID = "ir.softap.mefit.ui.video.list.EXTRA_TAG_ID"
        private const val EXTRA_TYPE_ID = "ir.softap.mefit.ui.video.list.EXTRA_TYPE_ID"

        fun newIntent(
            context: Context,
            title: String,
            categoryId: Int? = null,
            issuerId: Int? = null,
            tagId: Int? = null,
            typeId: Int? = null
        ) = Intent(context, VideoListActivity::class.java).apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_CATEGORY_ID, categoryId)
            putExtra(EXTRA_ISSUER_ID, issuerId)
            putExtra(EXTRA_TAG_ID, tagId)
            putExtra(EXTRA_TYPE_ID, typeId)
        }
    }

    private val title: String by lazy {
        intent.extras?.getString(EXTRA_TITLE) ?: ""
    }
    private val categoryId: Int? by lazy {
        intent.extras?.getInt(EXTRA_CATEGORY_ID, Int.MIN_VALUE)
    }
    private val issuerId: Int? by lazy {
        intent.extras?.getInt(EXTRA_ISSUER_ID, Int.MIN_VALUE)
    }
    private val tagId: Int? by lazy {
        intent.extras?.getInt(EXTRA_TAG_ID, Int.MIN_VALUE)
    }
    private val typeId: Int? by lazy {
        intent.extras?.getInt(EXTRA_TYPE_ID, Int.MIN_VALUE)
    }

    private val videoListViewModel: VideoListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[VideoListViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.activity_video_list

    override val initViews: (Bundle?) -> Unit = { _ ->

        btnBack.setOnClickListener { onBackPressed() }

        tvTitle.text = title

        srlVideo.setColorSchemeColors(colors[R.color.colorAccent])
        srlVideo.setOnRefreshListener {
            videoListViewModel.filter(
                if (categoryId != Int.MIN_VALUE) categoryId else null,
                if (issuerId != Int.MIN_VALUE) issuerId else null,
                if (tagId != Int.MIN_VALUE) tagId else null,
                if (typeId != Int.MIN_VALUE) typeId else null
            )
        }

        val videoPageListAdapter = VideoListAdapter(
            { videoListViewModel.retry() },
            { video -> startActivity(VideoShowActivity.newIntent(this, video)) }
        )
        with(lstVideo) {
            layoutManager = GridLayoutManager(this@VideoListActivity, 2)
            adapter = videoPageListAdapter
        }

        videoListViewModel.observeState(this) { videoListViewState ->
            srlVideo.isRefreshing = videoListViewState.videoListState == ListState.LOADING
            videoPageListAdapter.listState = videoListViewState.videoListState

            videoPageListAdapter.submitList(videoListViewState.videos)

            if (videoListViewState.videoListViewEvent?.handled == false) {
                when (videoListViewState.videoListViewEvent) {
                    is VideoListViewEvent.ErrorViewEvent -> {
                        ToastBuilder.showError(this, strings[videoListViewState.videoListViewEvent.errorMessage])
                    }
                }
            }
        }

        videoListViewModel.filter(
            if (categoryId != Int.MIN_VALUE) categoryId else null,
            if (issuerId != Int.MIN_VALUE) issuerId else null,
            if (tagId != Int.MIN_VALUE) tagId else null,
            if (typeId != Int.MIN_VALUE) typeId else null
        )
    }
}
