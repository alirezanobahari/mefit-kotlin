package ir.softap.mefit.ui.video.list

import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import ir.softap.mefit.R
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.data.model.request.query.FilterVideoQuery
import ir.softap.mefit.domain.interactor.FilterVideoUseCase
import ir.softap.mefit.ui.abstraction.DisposableStateViewModel
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.core.DEFAULT_PAGE_SIZE
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import retrofit2.HttpException
import javax.inject.Inject

sealed class VideoListViewEvent : ViewEvent() {

    data class ErrorViewEvent(val errorMessage: Int) : VideoListViewEvent()

}

data class VideoListViewState(
    val videoListState: ListState = ListState.IDLE,
    val videos: List<Video> = emptyList(),
    val videoListViewEvent: VideoListViewEvent? = null
) : ViewState

class VideoListViewModel @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val filterVideoUseCase: FilterVideoUseCase
) : DisposableStateViewModel<VideoListViewState>(compositeDisposable) {

    override val state = MutableLiveData<VideoListViewState>()

    private var retryCompletable: Completable? = null

    init {
        if (!stateInitialized)
            state.value = VideoListViewState()
    }

    fun filter(
        categoryId: Int? = null,
        issuerId: Int? = null,
        tagId: Int? = null,
        typeId: Int? = null
    ) {
        compositeDisposable.add(filterVideoUseCase.execute(
            FilterVideoQuery(
                issuerId = issuerId,
                catsId = categoryId?.let { listOf(it.toString()) },
                tagsId = tagId?.let { listOf(it.toString()) },
                typesId = typeId?.let { listOf(it.toString()) },
                page = 0,
                count = DEFAULT_PAGE_SIZE
            )
        ).doOnSubscribe {_-> updateState { it.copy(videoListState = ListState.LOADING) } }
            .subscribe(
                { videos ->
                    updateState { videoListViewState ->
                        videoListViewState.copy(
                            videoListState = ListState.SUCCESS.withItemCount(videos.size),
                            videos = videos
                        )
                    }
                },
                { throwable ->
                    if (throwable is HttpException && throwable.code() == 404) {
                        updateState { videoListViewState ->
                            videoListViewState.copy(videoListState = ListState.SUCCESS.withItemCount(0))
                        }
                        return@subscribe
                    }
                    updateState { videoListViewState ->
                        videoListViewState.copy(
                            videoListState = ListState.ERROR,
                            videoListViewEvent = VideoListViewEvent.ErrorViewEvent(R.string.msg_error_occurred)
                        )
                    }
                    e { "${TAG()}, $throwable" }
                    setRetry(Action { filter(categoryId, issuerId, tagId, typeId) })
                })
        )
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

}