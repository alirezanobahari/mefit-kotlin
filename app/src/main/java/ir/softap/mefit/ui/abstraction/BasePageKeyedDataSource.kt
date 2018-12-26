package ir.softap.mefit.ui.abstraction

import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable

abstract class BasePageKeyedDataSource<Key, Value>(val compositeDisposable: CompositeDisposable) :
    PageKeyedDataSource<Key, Value>() {

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
    }

    /**
     * dispose [compositeDisposable]
     */
    fun dispose() {
        compositeDisposable.dispose()
    }

}