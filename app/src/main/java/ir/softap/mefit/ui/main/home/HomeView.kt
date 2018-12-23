package ir.softap.mefit.ui.main.home

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import io.reactivex.disposables.CompositeDisposable
import ir.softap.mefit.R
import ir.softap.mefit.data.model.Home
import ir.softap.mefit.data.model.SLIDE_SHOW_KEY
import ir.softap.mefit.domain.interactor.FetchHomeUseCase
import ir.softap.mefit.ui.abstraction.DisposableStateViewModel
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import ir.softap.mefit.utilities.onDebug
import javax.inject.Inject

sealed class HomeViewEvent : ViewEvent() {
    data class ErrorViewEvent(@StringRes val message: Int) : HomeViewEvent()
}

data class HomeViewState(
    val homeListState: ListState = ListState.IDLE,
    val homeList: List<Home> = emptyList(),
    val homeViewEvent: HomeViewEvent? = null
) : ViewState

class HomeViewModel @Inject constructor(
    private val fetchHomeUseCase: FetchHomeUseCase,
    compositeDisposable: CompositeDisposable
) : DisposableStateViewModel<HomeViewState>(compositeDisposable) {

    override val state = MutableLiveData<HomeViewState>()

    init {
        if (!stateInitialized)
            state.value = HomeViewState()
        fetchHome()
    }

    fun fetchHome() {
        compositeDisposable.add(fetchHomeUseCase.execute(Unit)
            .doOnSubscribe { _ -> updateState(true) { it.copy(homeListState = ListState.LOADING) } }
            .map { homeList -> homeList.sortedBy { it.title != SLIDE_SHOW_KEY } }
            .subscribe(
                { homeList ->
                    updateState(true) { homeViewState ->
                        homeViewState.copy(
                            homeListState = ListState.SUCCESS.withItemCount(homeList.size),
                            homeList = homeList
                        )
                    }
                },
                { throwable ->
                    updateState(true) { homeViewState ->
                        homeViewState.copy(
                            homeListState = ListState.ERROR,
                            homeViewEvent = HomeViewEvent.ErrorViewEvent(R.string.msg_error_occurred)
                        )
                    }
                    onDebug { e { "${TAG()}, $throwable" } }
                })
        )
    }

}