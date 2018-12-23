package ir.softap.mefit.ui.common.glide

import android.annotation.SuppressLint
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.request.RequestOptions


// Required by Glide's annotation processor.
@GlideExtension
object MefitGlideExtension {

    // Size of mini thumb in pixels.
    private const val MINI_THUMB_SIZE = 100

    @SuppressLint("CheckResult")
    @GlideOption
    @JvmStatic
    fun miniThumb(options: RequestOptions) {
        options
            .fitCenter()
            .override(MINI_THUMB_SIZE)
    }

}
