package ir.softap.mefit.ui.video.show

import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.functions.Function3
import ir.softap.mefit.R
import ir.softap.mefit.SessionManager
import ir.softap.mefit.data.model.Comment
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.data.model.request.LikeRequest
import ir.softap.mefit.data.model.request.PostCommentRequest
import ir.softap.mefit.domain.interactor.*
import ir.softap.mefit.ui.abstraction.DisposableStateViewModel
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.UIState
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import javax.inject.Inject

sealed class VideoShowEvent : ViewEvent() {
    data class ValidationStateEvent(val validationState: VideoShowViewModel.ValidationState) : VideoShowEvent()
    data class ErrorViewEvent(val message: Int) : VideoShowEvent()
}

data class VideoShowState(
    val postCommentState: UIState = UIState.IDLE,
    val loadVideoDetailState: ListState = ListState.IDLE,
    val video: Video? = null,
    val videoDetail: Video.VideoDetail? = null,
    val suggestedVideos: List<Video> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val fullscreen: Boolean = false,
    val videoShowEvent: VideoShowEvent? = null
) : ViewState

class VideoShowViewModel @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val fetchVideoDetailsUseCase: FetchVideoDetailsUseCase,
    private val fetchSuggestedVideosUseCase: FetchSuggestedVideosUseCase,
    private val fetchCommentsUseCase: FetchCommentsUseCase,
    private val likeVideoUseCase: LikeVideoUseCase,
    private val commentUseCase: CommentUseCase
) : DisposableStateViewModel<VideoShowState>(compositeDisposable) {

    enum class ValidationState {
        EMPTY_COMMENT, OK
    }

    public override val state = MutableLiveData<VideoShowState>()
    private var retryCompletable: Completable? = null

    init {
        if (!stateInitialized)
            state.value = VideoShowState()
    }

    fun fetchVideoDetails(video: Video) {
        updateState { it.copy(video = video) }
        compositeDisposable.add(
            Single.zip<Video.VideoDetail, List<Video>, List<Comment>, Triple<Video.VideoDetail, List<Video>, List<Comment>>>(
                fetchVideoDetailsUseCase.execute(
                    FetchVideoDetailsUseCase.Params(
                        SessionManager.session?.token!!,
                        video.id
                    )
                ),
                fetchSuggestedVideosUseCase.execute(video.id),
                fetchCommentsUseCase.execute(video.id),
                Function3 { videoDetails, suggestedVideos, comments ->
                    Triple(videoDetails, suggestedVideos, comments)
                }
            )
                .doOnSubscribe { _ -> updateState { it.copy(loadVideoDetailState = ListState.LOADING) } }
                .subscribe(
                    { result ->
                        updateState { videoShowState ->
                            videoShowState.copy(
                                loadVideoDetailState = ListState.SUCCESS.withItemCount(result.third.size),
                                videoDetail = result.first,
                                suggestedVideos = result.second,
                                comments = result.third
                            )
                        }
                    },
                    { throwable ->
                        setRetry(Action { fetchVideoDetails(video) })
                        updateState { videoShowState ->
                            videoShowState.copy(
                                loadVideoDetailState = ListState.ERROR,
                                videoShowEvent = VideoShowEvent.ErrorViewEvent(R.string.msg_error_occurred)
                            )
                        }
                        e { "${TAG()}, $throwable" }
                    })
        )
    }

    fun like(videoId: Int) {
        compositeDisposable.add(
            likeVideoUseCase.execute(LikeVideoUseCase.Params(SessionManager.session!!.token, LikeRequest(videoId)))
                .subscribe({},
                    { throwable ->
                        updateState { it.copy(videoShowEvent = VideoShowEvent.ErrorViewEvent(R.string.error_on_like)) }
                        e { "${TAG()}, $throwable" }
                    })
        )
    }

    fun comment(videoId: Int, text: String) {
        if (validateComment(text))
            compositeDisposable.add(
                commentUseCase.execute(
                    CommentUseCase.Params(
                        SessionManager.session!!.token,
                        PostCommentRequest(videoId, text)
                    )
                ).doOnSubscribe { _ -> updateState { it.copy(postCommentState = UIState.LOADING) } }
                    .subscribe(
                        {
                            updateState { it.copy(postCommentState = UIState.SUCCESS) }
                        },
                        { throwable ->
                            updateState { videoShowState ->
                                videoShowState.copy(
                                    postCommentState = UIState.ERROR,
                                    videoShowEvent = VideoShowEvent.ErrorViewEvent(R.string.error_on_like)
                                )
                            }
                            e { "${TAG()}, $throwable" }
                        })
            )
    }

    fun fullScreen(fullscreen: Boolean) {
        updateState { it.copy(fullscreen = fullscreen) }
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    private fun validateComment(text: String): Boolean {
        if (text.isEmpty()) {
            updateState { it.copy(videoShowEvent = VideoShowEvent.ValidationStateEvent(ValidationState.EMPTY_COMMENT)) }
            return false
        }
        updateState { it.copy(videoShowEvent = VideoShowEvent.ValidationStateEvent(ValidationState.OK)) }
        return true
    }

}