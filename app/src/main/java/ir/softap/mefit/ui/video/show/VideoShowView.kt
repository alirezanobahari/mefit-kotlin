package ir.softap.mefit.ui.video.show

import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import io.reactivex.disposables.CompositeDisposable
import ir.softap.mefit.R
import ir.softap.mefit.SessionManager
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.domain.interactor.FetchVideoDetailsUseCase
import ir.softap.mefit.ui.abstraction.DisposableStateViewModel
import ir.softap.mefit.ui.common.UIState
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import javax.inject.Inject

sealed class VideoShowEvent : ViewEvent() {

    data class ErrorViewEvent(val message: Int) : VideoShowEvent()

}

data class VideoShowState(
    val loadVideoState: UIState = UIState.IDLE,
    val video: Video? = null,
    val videoDetail: Video.VideoDetail? = null,
    val videoShowEvent: VideoShowEvent? = null
) : ViewState

class VideoShowViewModel @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val fetchVideoDetailsUseCase: FetchVideoDetailsUseCase
) : DisposableStateViewModel<VideoShowState>(compositeDisposable) {

    override val state = MutableLiveData<VideoShowState>()

    init {
        if (!stateInitialized)
            state.value = VideoShowState()
    }

    fun fetchVideoDetails(video: Video) {
        updateState { it.copy(video = video) }
        compositeDisposable.add(
            fetchVideoDetailsUseCase.execute(
                FetchVideoDetailsUseCase.Params(
                    SessionManager.session?.token!!,
                    video.id
                )
            ).doOnSubscribe { _ ->
                updateState { it.copy(loadVideoState = UIState.LOADING) }
            }.subscribe(
                { videoDetail ->
                    updateState { videoShowState ->
                        videoShowState.copy(
                            loadVideoState = UIState.SUCCESS,
                            videoDetail = videoDetail
                        )
                    }
                },
                { throwable ->
                    updateState { videoShowState ->
                        videoShowState.copy(
                            loadVideoState = UIState.ERROR,
                            videoShowEvent = VideoShowEvent.ErrorViewEvent(R.string.msg_error_occurred)
                        )
                    }
                    e { "${TAG()}, $throwable" }
                })
        )
    }

}