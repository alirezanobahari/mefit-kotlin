package ir.softap.mefit.ui.video.show

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import kotlinx.android.synthetic.main.fragment_video_player.view.*


class VideoPlayerFragment : DaggerXFragment() {

    companion object {

        private const val EXTRA_VIDEO_URLS = "ir.softap.mefit.ui.video.show.EXTRA_VIDEO_URLS"

        fun newInstance(videoUrls: Array<String>) = VideoPlayerFragment().apply {
            arguments = Bundle().apply {
                putStringArray(EXTRA_VIDEO_URLS, videoUrls)
            }
        }
    }

    private val videoUrls: Array<String> by lazy {
        arguments?.getStringArray(EXTRA_VIDEO_URLS) ?: emptyArray<String>()
    }

    override val layoutRes: Int = R.layout.fragment_video_player

    override val initViews: View.() -> Unit = {
        val mediaController = MediaController(context, false)
        mediaController.setAnchorView(this)
        videoView.setMediaController(mediaController)
        videoView.requestFocus()
        videoView.setOnCompletionListener {
            videoView.setVideoURI(Uri.parse(videoUrls[0]))
        }
        cbVideoQuality.setOnCheckedChangeListener { _, isChecked ->
            videoView.setVideoURI(Uri.parse(if (isChecked) videoUrls[0] else videoUrls[1]))
        }
        cbFullscreen.setOnCheckedChangeListener { _, isChecked ->

        }
        cbFullscreen.isChecked = context.resources.configuration.orientation ==
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

}