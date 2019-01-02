package ir.softap.mefit.ui.video.show

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.dialogfragment.DaggerXBottomPopupDialogFragment
import ir.softap.mefit.ui.common.UIState
import ir.softap.mefit.utilities.d
import ir.softap.mefit.utilities.extensions.TAG
import ir.softap.mefit.utilities.extensions.strings
import kotlinx.android.synthetic.main.dialog_comment.*

class CommentDialog : DaggerXBottomPopupDialogFragment() {

    override fun getTheme(): Int = R.style.AppDialog_Fullscreen

    private val videoShowViewModel: VideoShowViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[VideoShowViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.dialog_comment

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->
        btnSendComment.setOnClickListener {
            videoShowViewModel.comment(
                videoShowViewModel.state.value?.video?.id!!,
                edComment.text.toString()
            )
        }

        videoShowViewModel.observeState(this) { videoShowState ->
            val loading = videoShowState.postCommentState == UIState.LOADING
            btnSendComment.visibility = if (loading) View.INVISIBLE else View.VISIBLE
            btnSendComment.isEnabled = !loading
            pbLoading.visibility = if (loading) View.VISIBLE else View.GONE

            if(videoShowState.postCommentState == UIState.SUCCESS)
                dismiss()

            if (videoShowState.videoShowEvent?.handled == false) {
                when (videoShowState.videoShowEvent) {
                    is VideoShowEvent.ValidationStateEvent -> {
                        when (videoShowState.videoShowEvent.validationState) {
                            VideoShowViewModel.ValidationState.EMPTY_COMMENT -> edComment.error =
                                    context!!.strings[R.string.validation_empty_comment]
                            else -> d { "${TAG()}, validation ok." }
                        }
                    }
                }
            }
        }
    }
}