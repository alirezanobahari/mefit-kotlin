package ir.softap.mefit.ui.video.show

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.ui.PlaybackControlView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import kotlinx.android.synthetic.main.fragment_video_player.*


class VideoPlayerFragment : DaggerXFragment() {

    private val videoShowViewModel: VideoShowViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[VideoShowViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.fragment_video_player

    override val initViews: (View, Bundle?) -> Unit = { view, saveInstanceState ->

        val player = ExoPlayerFactory.newSimpleInstance(context!!)
        val dataSourceFactory = DefaultDataSourceFactory(context!!, context!!.packageName)
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse("https://sample-videos.com/video123/mp4/480/big_buck_bunny_480p_5mb.mp4"))
        player.prepare(mediaSource)
        exoPlayer.player = player

        //player.set

        val playbackControl = exoPlayer.findViewById<PlaybackControlView>(R.id.exo_controller)
        val btnExoFullscreen = playbackControl.findViewById<FrameLayout>(R.id.btnExoFullscreen)

        btnExoFullscreen.setOnClickListener { videoShowViewModel.fullScreen(true) }

        videoShowViewModel.observeState(this) { videoShowState ->

        }
    }

}