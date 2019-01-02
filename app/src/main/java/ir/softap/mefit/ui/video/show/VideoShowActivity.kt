package ir.softap.mefit.ui.video.show

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import ir.softap.mefit.R
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.ui.abstraction.DaggerXFragmentActivity
import ir.softap.mefit.utilities.extensions.addFragment
import kotlinx.android.synthetic.main.activity_video_show.*

class VideoShowActivity : DaggerXFragmentActivity() {

    companion object {
        private const val EXTRA_VIDEO = "ir.softap.mefit.ui.video.show.EXTRA_VIDEO"

        fun newIntent(context: Context, video: Video) =
            Intent(context, VideoShowActivity::class.java).apply {
                putExtra(EXTRA_VIDEO, video)
            }
    }

    private val video: Video? by lazy {
        intent.extras?.getParcelable<Video>(EXTRA_VIDEO)
    }


    private val videoShowViewModel: VideoShowViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[VideoShowViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.activity_video_show

    override val initViews: (Bundle?) -> Unit = { saveInstanceState ->
        video?.also { videoShowViewModel.fetchVideoDetails(it) }

        if (saveInstanceState == null) {
            addFragment(VideoPlayerFragment(), R.id.flVideoPlayerFragmentContainer, false)
            addFragment(VideoDetailFragment(), R.id.flVideoDetailFragmentContainer, false)
        }

        videoShowViewModel.observeState(this) { videoShowState ->
            if (videoShowState.fullscreen) {
                flVideoPlayerFragmentContainer.rotation = 90.toFloat()
                with(flVideoPlayerFragmentContainer.layoutParams) {
                    height = LinearLayout.LayoutParams.MATCH_PARENT
                    width = LinearLayout.LayoutParams.MATCH_PARENT
                }
            }
            /*if (videoShowState.fullscreen) {
                onApiAndAbove(18) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
                }
                onApi(17) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            } else {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }*/
        }
    }

    override fun onBackPressed() {
        if (videoShowViewModel.state.value?.fullscreen == true) {
            videoShowViewModel.fullScreen(false)
            return
        }
        super.onBackPressed()
    }

}