package ir.softap.mefit.ui.video.show

import android.content.Context
import android.content.Intent
import android.os.Bundle
import ir.softap.mefit.R
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.ui.abstraction.DaggerXActivity

class VideoShowActivity : DaggerXActivity() {

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

    override val layoutRes: Int = R.layout.activity_video_show

    override val initViews: (Bundle?) -> Unit = {



    }


}