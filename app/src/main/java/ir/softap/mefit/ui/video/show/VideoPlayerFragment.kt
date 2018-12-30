package ir.softap.mefit.ui.video.show

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.lifecycle.ViewModelProviders
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import ir.softap.mefit.ui.common.ListState
import kotlinx.android.synthetic.main.fragment_video_player.*


class VideoPlayerFragment : DaggerXFragment() {

    private val BUNDLE_INITIALIZED = "BUNDLE_INITIALIZED"

    /*companion object {

        private const val EXTRA_VIDEO_URLS = "ir.softap.mefit.ui.video.show.EXTRA_VIDEO_URLS"

        fun newInstance(videoUrls: Array<String>) = VideoPlayerFragment().apply {
            arguments = Bundle().apply {
                putStringArray(EXTRA_VIDEO_URLS, videoUrls)
            }
        }
    }*/

    private var initialized: Boolean = false

    private val videoShowViewModel: VideoShowViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[VideoShowViewModel::class.java]
    }

    /*private val videoUrls: Array<String> by lazy {
        arguments?.getStringArray(EXTRA_VIDEO_URLS) ?: emptyArray<String>()
    }*/

    override val layoutRes: Int = R.layout.fragment_video_player

    override val initViews: (View, Bundle?) -> Unit = { view, saveInstanceState ->

        if (saveInstanceState != null)
            initialized = saveInstanceState.getBoolean(BUNDLE_INITIALIZED)

        val mediaController = MediaController(context, false)
        mediaController.setAnchorView(view)
        videoView.setMediaController(mediaController)
        videoView.requestFocus()
        cbFullscreen.isChecked = videoShowViewModel.state.value?.fullscreen ?: false
        cbFullscreen.setOnCheckedChangeListener { _, isChecked ->
            if (videoShowViewModel.state.value?.fullscreen != isChecked)
                videoShowViewModel.fullScreen(isChecked)
        }

        videoShowViewModel.observeState(this) { videoShowState ->
            if (!initialized) {
                if (videoShowState.loadVideoDetailState == ListState.SUCCESS) {
                    videoView.setVideoURI(Uri.parse("https://sample-videos.com/video123/mp4/480/big_buck_bunny_480p_5mb.mp4"))//Uri.parse(videoShowState.videoDetail?.url?.url480))
                    videoView.setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.start()
                    }
                    videoView.setOnCompletionListener { mediaPlayer ->
                        videoView.setVideoURI(Uri.parse("https://sample-videos.com/video123/mp4/480/big_buck_bunny_480p_5mb.mp4"))//Uri.parse(videoShowState.videoDetail?.url?.url480))
                    }
                    initialized = true
                }
            }
            if (videoShowState.loadVideoDetailState == ListState.SUCCESS) {
               /* cbVideoQuality.setOnCheckedChangeListener { _, isChecked ->
                    videoView.setVideoURI(
                        Uri.parse(
                            if (isChecked) "https://sample-videos.com/video123/mp4/480/big_buck_bunny_480p_5mb.mp4" //videoShowState.videoDetail?.url?.url720
                            else "https://sample-videos.com/video123/mp4/480/big_buck_bunny_480p_5mb.mp4" //videoShowState.videoDetail?.url?.url480
                        )
                    )
                }*/
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(BUNDLE_INITIALIZED, initialized)
        super.onSaveInstanceState(outState)
    }

}