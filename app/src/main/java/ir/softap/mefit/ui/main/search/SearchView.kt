package ir.softap.mefit.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import io.reactivex.disposables.CompositeDisposable
import ir.softap.mefit.R
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.data.model.request.query.SearchVideoQuery
import ir.softap.mefit.ui.abstraction.BasePageKeyedDataSource
import ir.softap.mefit.ui.abstraction.DisposableStateViewModel
import ir.softap.mefit.ui.common.ListState
import javax.inject.Inject

sealed class SearchViewEvent : ViewEvent() {
    data class ErrorViewEvent(val errorMessage: Int) : SearchViewEvent()
}

data class SearchViewState(
    val searchVideoQuery: SearchVideoQuery = SearchVideoQuery(),
    val searchListState: ListState = ListState.IDLE,
    val videoList: List<Video> = emptyList(),
    val searchViewEvent: SearchViewEvent? = null
) : ViewState

class SearchViewModel @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val searchVideoDataSourceFactory: SearchVideoDataSourceFactory
) :
    DisposableStateViewModel<SearchViewState>(compositeDisposable) {

    override val state = MutableLiveData<SearchViewState>()

    val videoListLiveData: LiveData<PagedList<Video>> by lazy {
        val config = PagedList.Config.Builder()
            .setPageSize(BasePageKeyedDataSource.DEFAULT_PAGE_SIZE)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(false)
            .build()
        LivePagedListBuilder<Int, Video>(searchVideoDataSourceFactory, config).build()
    }

    init {
        if (!stateInitialized)
            state.value = SearchViewState()

        searchVideoDataSourceFactory.searchListState = { listState ->
            updateState(true) {
                it.copy(
                    searchListState = listState,
                    searchViewEvent = if (listState == ListState.ERROR) SearchViewEvent.ErrorViewEvent(
                        R.string.msg_error_occurred
                    ) else it.searchViewEvent
                )
            }
        }

    }

    fun refresh() = videoListLiveData.value?.dataSource?.invalidate()

    fun retry() = searchVideoDataSourceFactory.mutableSearchVideoPageKeyedDataSource.value?.retry()

}