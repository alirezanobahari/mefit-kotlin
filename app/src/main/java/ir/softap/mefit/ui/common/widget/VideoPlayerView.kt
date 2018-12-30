package ir.softap.mefit.ui.common.widget;

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import ir.softap.mefit.R
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.utilities.extensions.colors
import kotlinx.android.synthetic.main.view_video_player.view.*


class VideoPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener, SeekBar.OnSeekBarChangeListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnCompletionListener {

    private var url: Video.VideoDetail.Url? = null

    private var startOnPrepared = true

    private var playerInterface: PlayerInterface? = null

    private val DELAY_HIDE_CONTROLS = 3000

    private var tempSeek = 0
    private var isHD = false
    private var isControlShown = true
    private var isFullScreen = false
    private var isPause = false
    private var enableTouch = true

    //update seekBar position
    private val updateSeekBar = object : Runnable {

        override fun run() {
            sbVideoPlayer.progress = videoPlayer.currentPosition
            if (videoPlayer.isPlaying && !isPause) {
                sbVideoPlayer.postDelayed(this, 100)
            }
        }
    }


    private val updateTimer = object : Runnable {
        override fun run() {
            tvVideoTimer.text = timeBuilderFromMilliSec(videoPlayer.currentPosition.toLong())
            if (videoPlayer.isPlaying && !isPause) {
                tvVideoTimer.postDelayed(this, 1000)
            }
        }
    }


    init {
        addView(View.inflate(getContext(), R.layout.view_video_player, null))

        videoControl.setOnClickListener(this)
        btnFullscreen.setOnClickListener(this)
        btnClose.setOnClickListener(this)
        btnPlayPause.setOnClickListener(this)
        btnHd.setOnClickListener(this)
        sbVideoPlayer.setOnSeekBarChangeListener(this)

        videoPlayer.setOnPreparedListener(this)
        videoPlayer.setOnInfoListener(this)
        videoPlayer.setOnCompletionListener(this)

        delayHideControls()
    }

    fun playerSetup(
        url: Video.VideoDetail.Url,
        playerInterface: PlayerInterface,
        onErrorListener: MediaPlayer.OnErrorListener
    ) {
        this.playerInterface = playerInterface
        setUrl(url, true)
        videoPlayer.setOnErrorListener(onErrorListener)
    }

    fun playerSetup(url: Video.VideoDetail.Url) {
        setUrl(url, true)
        enableTouch = true
    }


    private fun setUrl(url: Video.VideoDetail.Url, startOnPrepared: Boolean) {
        this.url = url
        this.startOnPrepared = startOnPrepared
        if (url.url480 != null) {
            videoPlayer.setVideoPath(url.url480)
        } else {
            videoPlayer.setVideoPath(url.url720)
            isHD = true
        }

        postDelayed({ showHideLoading(true) }, 200)
    }

    private fun delayHideControls() {
        if (isControlShown)
            postDelayed({ showHideControls(false) }, DELAY_HIDE_CONTROLS.toLong())
    }

    fun showHideBtnClose(show: Boolean) {
        btnClose.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (enableTouch)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    showHideControls(true)
                }

                MotionEvent.ACTION_MOVE -> {
                }

                MotionEvent.ACTION_UP -> delayHideControls()
            }
        return true
    }

    fun disableOnTouch() {
        enableTouch = false
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnClose -> {
                playerInterface!!.onClose()
            }
            R.id.btnFullscreen -> {
                if (!isFullScreen) {
                    isFullScreen = true
                    playerInterface!!.onFullScreen(isFullScreen)
                    btnFullscreen.setImageResource(R.drawable.ic_minimize)
                } else {
                    isFullScreen = false
                    playerInterface!!.onFullScreen(isFullScreen)
                    btnFullscreen.setImageResource(R.drawable.ic_fullscreen)
                }
            }
            R.id.btnPlayPause -> {
                if (videoPlayer.isPlaying) {
                    pause()
                } else {
                    play()
                }
            }
            R.id.btnHd -> {
                isHD = if (isHD) {
                    btnHd.setColorFilter(Color.WHITE)
                    setHD(false)
                    false
                } else {
                    btnHd.setColorFilter(context.colors[R.color.colorAccent])
                    setHD(true)
                    true
                }
            }
            R.id.videoControl -> {
                if (isControlShown)
                    showHideControls(false)
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
        if (b)
            videoPlayer.seekTo(i)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {

    }

    fun showHideControls(show: Boolean) {
        if (show) {
            isControlShown = show
            videoControl!!.animate().alpha(1f).setDuration(500).setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {
                    videoControl!!.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animator: Animator) {

                }

                override fun onAnimationCancel(animator: Animator) {

                }

                override fun onAnimationRepeat(animator: Animator) {

                }
            }).start()
        } else {
            isControlShown = show
            videoControl!!.animate().alpha(0f).setDuration(500).setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {

                }

                override fun onAnimationEnd(animator: Animator) {
                    videoControl!!.visibility = View.GONE
                }

                override fun onAnimationCancel(animator: Animator) {

                }

                override fun onAnimationRepeat(animator: Animator) {

                }
            }).start()
        }
    }

    private fun setHD(hd: Boolean) {
        tempSeek = videoPlayer.currentPosition
        videoPlayer.setVideoPath(if (hd) url!!.url720 else url!!.url480)
        videoPlayer.postDelayed({ videoPlayer.seekTo(tempSeek) }, 200)
        postDelayed({ showHideLoading(true) }, 200)
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        sbVideoPlayer.max = videoPlayer.duration
        sbVideoPlayer.postDelayed(updateSeekBar, 100)
        tvVideoTimer.postDelayed(updateTimer, 100)
        showHideLoading(false)

        if (startOnPrepared)
            videoPlayer.start()

        tvVideoTime.text = timeBuilderFromMilliSec(videoPlayer.duration.toLong())

        mediaPlayer.setOnBufferingUpdateListener { _, percent ->
            if (percent < sbVideoPlayer.max) {
                sbVideoPlayer.secondaryProgress = percent * sbVideoPlayer.max / 100
            }
        }
    }


    private fun showHideLoading(show: Boolean) {
        if (show) {
            pbLoading.visibility = View.VISIBLE
            btnPlayPause.visibility = View.INVISIBLE
        } else {
            pbLoading.visibility = View.INVISIBLE
            btnPlayPause.visibility = View.VISIBLE
        }
    }

    fun pause() {
        if (videoPlayer.isPlaying) {
            videoPlayer.pause()
            isPause = true
            btnPlayPause!!.setImageResource(R.drawable.ic_play)
        }
    }

    fun play() {
        if (!videoPlayer.isPlaying) {
            videoPlayer.start()
            isPause = false
            btnPlayPause!!.setImageResource(R.drawable.ic_pause)
        }
    }

    override fun onInfo(mediaPlayer: MediaPlayer, i: Int, i1: Int): Boolean {
        when (i) {
            MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                showHideLoading(false)
                return true
            }
            MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                showHideLoading(true)
                return true
            }
            MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                showHideLoading(false)
                return true
            }
        }
        return false
    }


    private fun timeBuilderFromMilliSec(millis: Long): String {
        return "${(millis / 1000).toInt() / 60}:${(millis / 1000).toInt() % 60}"
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        tvVideoTimer.text = timeBuilderFromMilliSec(0)
        sbVideoPlayer.progress = 0
        isPause = true
        btnPlayPause!!.setImageResource(R.drawable.ic_play)
        setUrl(url!!, false)
    }

    fun setFullScreen(fullScreen: Boolean) {
        this.isFullScreen = fullScreen
        if (fullScreen) {
            btnFullscreen.setImageResource(R.drawable.ic_minimize)
        } else {
            btnFullscreen.setImageResource(R.drawable.ic_fullscreen)
        }
    }


    interface PlayerInterface {
        fun onClose()

        fun onFullScreen(fullScreen: Boolean)
    }
}
