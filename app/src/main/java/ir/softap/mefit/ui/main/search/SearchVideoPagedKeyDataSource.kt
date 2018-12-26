package ir.softap.mefit.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.data.model.request.query.SearchVideoQuery
import ir.softap.mefit.domain.interactor.SearchVideoUseCase
import ir.softap.mefit.ui.abstraction.BasePageKeyedDataSource
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import ir.softap.mefit.utilities.onDebug
import javax.inject.Inject

class SearchVideoPageKeyedDataSource(
    compositeDisposable: CompositeDisposable,
    private val searchListState: (ListState) -> Unit,
    private val searchVideoUseCase: SearchVideoUseCase,
    private val searchVideoQuery: SearchVideoQuery
) : BasePageKeyedDataSource<Int, Video>(compositeDisposable) {

    private var retryCompletable: Completable? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Video>) {
        compositeDisposable.add(searchVideoUseCase.execute(searchVideoQuery.apply {
            page = 0
            count = params.requestedLoadSize
        }).doOnSubscribe { searchListState(ListState.LOADING) }
            .subscribe(
                { videos ->
                    searchListState(ListState.SUCCESS.withItemCount(videos.size))
                    callback.onResult(videos, null, 1)
                },
                { throwable ->
                    searchListState(ListState.ERROR)
                    onDebug { e { "${TAG()}, $throwable" } }
                    setRetry(Action { loadInitial(params, callback) })
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Video>) {
        compositeDisposable.add(searchVideoUseCase.execute(searchVideoQuery.apply {
            page = params.key
            count = params.requestedLoadSize
        }).doOnSubscribe { searchListState(ListState.LOADING) }
            .subscribe(
                { videos ->
                    searchListState(ListState.SUCCESS.withItemCount(videos.size))
                    callback.onResult(videos, params.key)
                },
                { throwable ->
                    searchListState(ListState.ERROR)
                    onDebug { e { "${TAG()}, $throwable" } }
                    setRetry(Action { loadAfter(params, callback) })
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Video>) {
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

class SearchVideoDataSourceFactory @Inject constructor(
    private val compositeDisposable: CompositeDisposable,
    private val searchVideoUseCase: SearchVideoUseCase
) : DataSource.Factory<Int, Video>() {

    private val searchVideoQuery: SearchVideoQuery = SearchVideoQuery()

    lateinit var searchListState: (ListState) -> Unit
    val mutableSearchVideoPageKeyedDataSource = MutableLiveData<SearchVideoPageKeyedDataSource>()

    override fun create(): DataSource<Int, Video> {
        val searchVideoPageKeyedDataSource =
            SearchVideoPageKeyedDataSource(compositeDisposable, searchListState, searchVideoUseCase, searchVideoQuery)
        mutableSearchVideoPageKeyedDataSource.postValue(searchVideoPageKeyedDataSource)
        return searchVideoPageKeyedDataSource
    }
}

