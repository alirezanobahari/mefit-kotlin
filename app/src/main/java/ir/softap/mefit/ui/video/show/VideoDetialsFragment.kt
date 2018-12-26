package ir.softap.mefit.ui.video.show

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import ir.softap.mefit.ui.common.UIState
import kotlinx.android.synthetic.main.fragment_video_detail.*

class VideoDetialsFragment : DaggerXFragment() {

    private val videoShowViewModel: VideoShowViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[VideoShowViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.fragment_video_detail

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->

        val videoDetailAdapter = VideoDetailAdapter()

        videoShowViewModel.observeState(this) { videoShowState ->

            pbLoading.visibility = if (videoShowState.loadVideoState == UIState.LOADING) View.VISIBLE else View.GONE

            if(videoShowState.loadVideoState == UIState.SUCCESS) {

            }

        }

    }

}