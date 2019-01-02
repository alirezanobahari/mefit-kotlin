package ir.softap.mefit.ui.video.show

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.etiennelenhart.eiffel.state.peek
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.ToastBuilder
import ir.softap.mefit.ui.video.list.VideoListActivity
import ir.softap.mefit.utilities.extensions.strings
import kotlinx.android.synthetic.main.fragment_video_detail.*
import javax.inject.Inject

class VideoDetailFragment : DaggerXFragment(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private val videoShowViewModel: VideoShowViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[VideoShowViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.fragment_video_detail

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->

        val videoDetailAdapter = VideoDetailAdapter(
            retry = { videoShowViewModel.retry() },
            videoSelect = { video -> startActivity(VideoShowActivity.newIntent(context!!, video)) },
            like = { video -> videoShowViewModel.like(video.id) },
            issuerSelect = { issuer ->
                startActivity(
                    VideoListActivity.newIntent(
                        context!!,
                        title = "${issuer.firstName ?: ""} ${issuer.lastName ?: ""}",
                        issuerId = issuer.id
                    )
                )
            },
            tagSelect = { tag ->
                startActivity(
                    VideoListActivity.newIntent(
                        context!!,
                        title = tag.title,
                        tagId = tag.id
                    )
                )
            },
            scrollToComment = { lstVideoDetail.smoothScrollToPosition(1) },
            addComment = {
                CommentDialog()
                    .show(childFragmentManager, "commentDialog")
            }
        )
        with(lstVideoDetail) {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            adapter = videoDetailAdapter
        }

        videoShowViewModel.observeState(this) { videoShowState ->

            videoDetailAdapter.listState = videoShowState.loadVideoDetailState
            if (videoShowState.loadVideoDetailState == ListState.SUCCESS) {
                with(videoDetailAdapter) {
                    videoDetail =
                            Triple(videoShowState.video!!, videoShowState.videoDetail!!, videoShowState.suggestedVideos)
                    submitData(videoShowState.comments.toMutableList())
                }
            }

            videoShowState.videoShowEvent?.peek { videoShowEvent ->
                when (videoShowEvent) {
                    is VideoShowEvent.ErrorViewEvent -> {
                        ToastBuilder.showError(context!!, context!!.strings[videoShowEvent.message])
                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
        fragmentDispatchingAndroidInjector

}