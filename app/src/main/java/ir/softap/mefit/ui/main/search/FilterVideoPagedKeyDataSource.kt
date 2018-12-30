package ir.softap.mefit.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.data.model.request.query.FilterVideoQuery
import ir.softap.mefit.domain.interactor.FilterVideoUseCase
import ir.softap.mefit.ui.abstraction.BasePageKeyedDataSource
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import ir.softap.mefit.utilities.onDebug
import retrofit2.HttpException
import javax.inject.Inject

@Deprecated("Not used dou to ignoring pagination")
class FilterVideoPageKeyedDataSource(
    compositeDisposable: CompositeDisposable,
    private val searchListState: (ListState) -> Unit,
    private val filterVideoUseCase: FilterVideoUseCase,
    private val filterVideoQuery: FilterVideoQuery
) : BasePageKeyedDataSource<Int, Video>(compositeDisposable) {

    private var retryCompletable: Completable? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Video>) {
        compositeDisposable.add(filterVideoUseCase.execute(filterVideoQuery.apply {
            page = 0
            count = params.requestedLoadSize
        }).doOnSubscribe { searchListState(ListState.LOADING) }
            .subscribe(
                { videos ->
                    searchListState(ListState.SUCCESS.withItemCount(videos.size))
                    callback.onResult(videos, null, 1)
                },
                { throwable ->
                    if (throwable is HttpException && throwable.code() == 404) {
                        searchListState(
                            ListState.SUCCESS.withItemCount(0)
                        )
                        return@subscribe
                    }
                    searchListState(ListState.ERROR)
                    onDebug { e { "${TAG()}, $throwable" } }
                    setRetry(Action { loadInitial(params, callback) })
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Video>) {
        compositeDisposable.add(filterVideoUseCase.execute(filterVideoQuery.apply {
            page = params.key
            count = params.requestedLoadSize
        }).doOnSubscribe { searchListState(ListState.LOADING) }
            .subscribe(
                { videos ->
                    searchListState(ListState.SUCCESS.withItemCount(videos.size))
                    callback.onResult(videos, params.key + 1)
                },
                { throwable ->
                    if (throwable is HttpException && throwable.code() == 404) {
                        searchListState(
                            ListState.SUCCESS.withItemCount(0)
                        )
                        return@subscribe
                    }
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

@Deprecated("Not used dou to ignoring pagination")
class FilterVideoDataSourceFactory constructor(
    private val compositeDisposable: CompositeDisposable,
    private val filterVideoUseCase: FilterVideoUseCase
) : DataSource.Factory<Int, Video>() {

    var filterVideoQuery: FilterVideoQuery = FilterVideoQuery()

    lateinit var searchListState: (ListState) -> Unit
    val mutableSearchVideoPageKeyedDataSource = MutableLiveData<FilterVideoPageKeyedDataSource>()

    override fun create(): DataSource<Int, Video> {
        val searchVideoPageKeyedDataSource =
            FilterVideoPageKeyedDataSource(compositeDisposable, searchListState, filterVideoUseCase, filterVideoQuery)
        mutableSearchVideoPageKeyedDataSource.postValue(searchVideoPageKeyedDataSource)
        return searchVideoPageKeyedDataSource
    }
}

