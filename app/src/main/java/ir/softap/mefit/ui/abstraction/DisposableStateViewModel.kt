package ir.softap.mefit.ui.abstraction

import com.etiennelenhart.eiffel.state.ViewState
import com.etiennelenhart.eiffel.viewmodel.StateViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class DisposableStateViewModel<T : ViewState>(val compositeDisposable: CompositeDisposable) :
    StateViewModel<T>() {

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}