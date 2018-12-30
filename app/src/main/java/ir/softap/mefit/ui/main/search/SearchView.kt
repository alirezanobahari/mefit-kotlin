package ir.softap.mefit.ui.main.search

import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import ir.softap.mefit.R
import ir.softap.mefit.data.model.Category
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.data.model.request.query.FilterVideoQuery
import ir.softap.mefit.domain.interactor.FetchCategoriesUseCase
import ir.softap.mefit.domain.interactor.FilterVideoUseCase
import ir.softap.mefit.ui.abstraction.DisposableStateViewModel
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.core.DEFAULT_PAGE_SIZE
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import org.apache.commons.lang3.tuple.MutablePair
import retrofit2.HttpException
import javax.inject.Inject

sealed class SearchViewEvent : ViewEvent() {
    data class ErrorViewEvent(val errorMessage: Int) : SearchViewEvent()
}

data class SearchViewState(
    val fetchCategoriesListState: ListState = ListState.IDLE,
    val categoryFilters: List<MutablePair<Category, Boolean>> = emptyList(),
    val filterVideoQuery: FilterVideoQuery = FilterVideoQuery(),
    val searchListState: ListState = ListState.IDLE,
    val videos: List<Video> = emptyList(),
    val query: String = "",
    val searchViewEvent: SearchViewEvent? = null
) : ViewState

class SearchViewModel @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val fetchCategoriesUseCase: FetchCategoriesUseCase,
    private val filterVideoUseCase: FilterVideoUseCase
) :
    DisposableStateViewModel<SearchViewState>(compositeDisposable) {

    override val state = MutableLiveData<SearchViewState>()

    private var retryCompletable: Completable? = null

    init {
        if (!stateInitialized)
            state.value = SearchViewState()
        fetchCategories()
    }

    fun fetchCategories() {
        compositeDisposable.add(fetchCategoriesUseCase.execute(Unit)
            .doOnSubscribe { _ -> updateState { it.copy(fetchCategoriesListState = ListState.LOADING) } }
            .subscribe(
                { categories ->
                    updateState { searchViewState ->
                        searchViewState.copy(
                            fetchCategoriesListState = ListState.SUCCESS.withItemCount(categories.size),
                            categoryFilters = categories.map { MutablePair(it, false) }
                        )
                    }
                },
                { throwable ->
                    updateState { searchViewState ->
                        searchViewState.copy(
                            fetchCategoriesListState = ListState.ERROR,
                            searchViewEvent = SearchViewEvent.ErrorViewEvent(R.string.msg_error_occurred)
                        )
                    }
                    e { "${TAG()}, $throwable" }
                })
        )
    }

    fun filter() {
        compositeDisposable.add(filterVideoUseCase.execute(
            FilterVideoQuery(
                query = state.value?.query,
                catsId = state.value?.categoryFilters?.filter { it.right }?.map { it.left.id.toString() },
                page = 0,
                count = DEFAULT_PAGE_SIZE
            )
        ).doOnSubscribe { _ -> updateState { it.copy(searchListState = ListState.LOADING) } }
            .subscribe(
                { videos ->
                    updateState { videoListViewState ->
                        videoListViewState.copy(
                            searchListState = ListState.SUCCESS.withItemCount(videos.size),
                            videos = videos
                        )
                    }
                },
                { throwable ->
                    if (throwable is HttpException && throwable.code() == 404) {
                        updateState { videoListViewState ->
                            videoListViewState.copy(searchListState = ListState.SUCCESS.withItemCount(0))
                        }
                        return@subscribe
                    }
                    updateState { videoListViewState ->
                        videoListViewState.copy(
                            searchListState = ListState.ERROR,
                            searchViewEvent = SearchViewEvent.ErrorViewEvent(R.string.msg_error_occurred)
                        )
                    }
                    e { "${TAG()}, $throwable" }
                    setRetry(Action { filter() })
                })
        )
    }

    fun setQuery(query: String) {
        updateState { it.copy(query = query) }
    }

    fun changeFilter(categoryFilter: Pair<Category, Boolean>) {
        updateState { searchViewState ->
            searchViewState.copy(
                categoryFilters = state.value!!.categoryFilters.apply {
                    first { it.left.id == categoryFilter.first.id }.apply {
                        left = categoryFilter.first
                        right = categoryFilter.second
                    }
                }
            )
        }
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