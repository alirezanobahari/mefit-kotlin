package ir.softap.mefit.ui.video.show

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import kotlinx.android.synthetic.main.fragment_video_player.*


class VideoPlayerFragment : DaggerXFragment() {

    private val videoShowViewModel: VideoShowViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[VideoShowViewModel::class.java]
    }

    private var player: SimpleExoPlayer? = null

    override val layoutRes: Int = R.layout.fragment_video_player

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->

        val pbExoBuffering = exoPlayer.findViewById<ProgressBar>(R.id.exo_buffering)

        //--------------------------------------
        //Creating default track selector
        //and init the player
        val adaptiveTrackSelection = AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
        player = ExoPlayerFactory.newSimpleInstance(
            context!!,
            DefaultRenderersFactory(context!!),
            DefaultTrackSelector(adaptiveTrackSelection),
            DefaultLoadControl()
        )

        //init the player
        exoPlayer.player = player

        //add listener to player
        player?.addListener(
            object : Player.EventListener {

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        ExoPlayer.STATE_READY -> pbExoBuffering.visibility = View.GONE
                        ExoPlayer.STATE_BUFFERING -> pbExoBuffering.visibility = View.VISIBLE
                    }
                }

            }
        )

        //-------------------------------------------------
        val defaultBandwidthMeter = DefaultBandwidthMeter()
        val defaultHlsExtractor = DefaultHlsExtractorFactory(DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES)
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(
            context!!,
            context!!.packageName,
            defaultBandwidthMeter
        )

        videoShowViewModel.observeState(this) { videoShowState ->

            if (player?.playWhenReady == false) {
                videoShowState.videoDetail?.url?.also { url ->
                    val mediaSource = HlsMediaSource.Factory(dataSourceFactory)
                        .setExtractorFactory(defaultHlsExtractor)
                        .createMediaSource(Uri.parse(url))
                    player?.prepare(mediaSource)
                    exoPlayer.player = player
                    player?.playWhenReady = true
                }
            }

        }
    }

    override fun onPause() {
        pausePlayer()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        startPlayer()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    private fun pausePlayer() {
        player?.apply {
            playWhenReady = false
            player?.playbackState
        }
    }

    private fun startPlayer() {
        player?.apply {
            playWhenReady = true
            playbackState
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            player?.release()
            player = null
        }
    }

}