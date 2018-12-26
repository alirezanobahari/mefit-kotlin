package ir.softap.mefit.ui.splash

import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import ir.softap.mefit.data.network.BASE_HOST
import ir.softap.mefit.data.network.HostSelectionInterceptor
import ir.softap.mefit.data.network.RPG_HOST
import ir.softap.mefit.domain.interactor.rpg.CheckMemberStatusUseCase
import ir.softap.mefit.ui.abstraction.DisposableStateViewModel
import ir.softap.mefit.ui.common.UIState
import ir.softap.mefit.ui.common.core.SID
import ir.softap.mefit.utilities.MobileNumberHelper
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import javax.inject.Inject

sealed class SplashViewEvent : ViewEvent() {
    data class MemberNotFoundEvent(val operator: MobileNumberHelper.Operator) : SplashViewEvent()
    data class MemberSuccessfulCheckEvent(val operator: MobileNumberHelper.Operator) : SplashViewEvent()
}

data class SplashViewState(
    val splashState: UIState = UIState.IDLE,
    val splashViewEvent: SplashViewEvent? = null
) : ViewState

class SplashViewModel @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val hostSelectionInterceptor: HostSelectionInterceptor,
    private val checkMemberStatusUseCase: CheckMemberStatusUseCase
) : DisposableStateViewModel<SplashViewState>(compositeDisposable) {

    override val state = MutableLiveData<SplashViewState>()

    private var retryCompletable: Completable? = null

    init {
        if (!stateInitialized)
            state.value = SplashViewState()
    }

    fun checkMemberStatus(username: String, operator: MobileNumberHelper.Operator) {
        hostSelectionInterceptor.host = RPG_HOST
        compositeDisposable.add(checkMemberStatusUseCase.execute(CheckMemberStatusUseCase.Params(SID, username))
            .doOnSubscribe { _ -> updateState { it.copy(splashState = UIState.LOADING) } }
            .subscribe(
                { response ->
                    hostSelectionInterceptor.host = BASE_HOST
                    when {
                        response in arrayOf("NOTFOUND", "OFF") -> {
                            updateState {
                                it.copy(
                                    splashState = UIState.SUCCESS,
                                    splashViewEvent = SplashViewEvent.MemberNotFoundEvent(operator)
                                )
                            }
                        }
                        response?.contains("ON") == true -> {
                            updateState {
                                it.copy(
                                    splashState = UIState.SUCCESS,
                                    splashViewEvent = SplashViewEvent.MemberSuccessfulCheckEvent(operator)
                                )
                            }
                        }
                    }
                },
                { throwable ->
                    updateState { it.copy(splashState = UIState.ERROR) }
                    e { "${TAG()}, $throwable" }
                    setRetry(Action { checkMemberStatus(username, operator) })
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