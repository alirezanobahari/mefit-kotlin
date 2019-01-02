package ir.softap.mefit.ui.main

import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import io.reactivex.disposables.CompositeDisposable
import ir.softap.mefit.data.network.BASE_HOST
import ir.softap.mefit.data.network.HostSelectionInterceptor
import ir.softap.mefit.data.network.JHOOBIN_HOST
import ir.softap.mefit.domain.interactor.jhoobin.UnsubscribeUseCase
import ir.softap.mefit.ui.abstraction.DisposableStateViewModel
import ir.softap.mefit.ui.common.UIState
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import javax.inject.Inject

sealed class MainViewEvent : ViewEvent() {
    class UnsubscribeEvent : MainViewEvent()
}

data class MainViewState(
    val mainNavigation: MainNavigation = MainNavigation.HOME,
    val mainViewEvent: MainViewEvent? = null,
    val unsubscribeState: UIState = UIState.IDLE
) : ViewState

class MainViewModel @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val hostSelectionInterceptor: HostSelectionInterceptor,
    private val unsubscribeUseCase: UnsubscribeUseCase
) : DisposableStateViewModel<MainViewState>(compositeDisposable) {

    override val state = MutableLiveData<MainViewState>()

    init {
        if (!stateInitialized)
            state.value = MainViewState()
    }

    fun navigateTo(mainNavigation: MainNavigation) {
        updateState { it.copy(mainNavigation = mainNavigation) }
    }

    fun unsubscribe() {
        updateState { it.copy(mainViewEvent = MainViewEvent.UnsubscribeEvent()) }
    }

    fun unsubscribeRequest(
        packageName: String,
        sku: String,
        purchaseToken: String,
        accessToken: String
    ) {
        hostSelectionInterceptor.host = JHOOBIN_HOST
        compositeDisposable.add(
            unsubscribeUseCase.execute(
                UnsubscribeUseCase.Params(
                    packageName,
                    sku,
                    purchaseToken,
                    accessToken
                )
            )
                .doOnSubscribe { _ -> updateState { it.copy(unsubscribeState = UIState.LOADING) } }
                .subscribe(
                    {
                        updateState { it.copy(unsubscribeState = UIState.SUCCESS) }
                        hostSelectionInterceptor.host = BASE_HOST
                    },
                    { throwable ->
                        updateState { it.copy(unsubscribeState = UIState.ERROR) }
                        e { "${TAG()}, error on unsubscribe, $throwable" }
                        hostSelectionInterceptor.host = BASE_HOST
                    })
        )
    }

}