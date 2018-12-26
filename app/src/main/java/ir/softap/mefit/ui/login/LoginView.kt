package ir.softap.mefit.ui.login

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import io.reactivex.disposables.CompositeDisposable
import ir.softap.mefit.R
import ir.softap.mefit.SessionManager
import ir.softap.mefit.data.model.request.LoginRequest
import ir.softap.mefit.data.network.BASE_HOST
import ir.softap.mefit.data.network.CAT_SLUG
import ir.softap.mefit.data.network.HostSelectionInterceptor
import ir.softap.mefit.data.network.RPG_HOST
import ir.softap.mefit.domain.interactor.LoginUseCase
import ir.softap.mefit.domain.interactor.rpg.CheckMemberStatusUseCase
import ir.softap.mefit.domain.interactor.rpg.PerformSubscribeUseCase
import ir.softap.mefit.domain.interactor.rpg.SubmitTransactionUseCase
import ir.softap.mefit.ui.abstraction.DisposableStateViewModel
import ir.softap.mefit.ui.common.UIState
import ir.softap.mefit.ui.common.core.SID
import ir.softap.mefit.utilities.MobileNumberHelper
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import ir.softap.mefit.utilities.onDebug
import javax.inject.Inject

sealed class LoginViewEvent : ViewEvent() {
    data class ValidationStateEvent(val validationState: LoginViewModel.ValidationState) : LoginViewEvent()
    class LoginSuccessEvent : LoginViewEvent()
    class IrancellPurchaseEvent : LoginViewEvent()
    data class ShowEnterPinDialog(val tid: String) : LoginViewEvent()
    data class ErrorViewEvent(@StringRes val errorMessage: Int) : LoginViewEvent()
}

data class LoginViewState(
    val loginState: UIState = UIState.IDLE,
    val pidState: UIState = UIState.IDLE,
    val loginViewEvent: LoginViewEvent? = null
) : ViewState

class LoginViewModel @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val hostSelectionInterceptor: HostSelectionInterceptor,
    private val loginUseCase: LoginUseCase,
    private val checkMemberStatusUseCase: CheckMemberStatusUseCase,
    private val performSubscribeUseCase: PerformSubscribeUseCase,
    private val submitTransactionUseCase: SubmitTransactionUseCase
) : DisposableStateViewModel<LoginViewState>(compositeDisposable) {

    enum class ValidationState {
        OK, EMPTY_PHONE_NUMBER, INVALID_PHONE_NUMBER
    }

    override val state = MutableLiveData<LoginViewState>()

    init {
        if (!stateInitialized)
            state.value = LoginViewState()
    }

    //First
    fun login(username: String) {
        hostSelectionInterceptor.host = BASE_HOST
        if (validate(username)) {
            val numberOperator = MobileNumberHelper.getOperator(username)
            compositeDisposable.add(loginUseCase.execute(LoginRequest(username, CAT_SLUG, true))
                .doOnSubscribe { _ -> updateState(true) { loginViewState -> loginViewState.copy(loginState = UIState.LOADING) } }
                .subscribe(
                    { session ->
                        SessionManager.session = session
                        checkMemberStatus(username, numberOperator)
                    },
                    { throwable ->
                        updateState(true) { loginViewState ->
                            loginViewState.copy(
                                loginState = UIState.ERROR,
                                loginViewEvent = LoginViewEvent.ErrorViewEvent(R.string.error_on_login)
                            )
                        }
                        onDebug { e { "${TAG()}, $throwable" } }
                    })
            )
        }
    }

    //Second, Done
    private fun checkMemberStatus(username: String, numberOperator: MobileNumberHelper.Operator) {
        hostSelectionInterceptor.host = RPG_HOST
        compositeDisposable.add(checkMemberStatusUseCase.execute(CheckMemberStatusUseCase.Params(SID, username))
            .doOnSubscribe { _ -> updateState(true) { loginViewState -> loginViewState.copy(loginState = UIState.LOADING) } }
            .subscribe(
                { response ->
                    when {
                        response in arrayOf("NOTFOUND", "OFF") -> {
                            when (numberOperator) {
                                MobileNumberHelper.Operator.IRANCELL -> {
                                    hostSelectionInterceptor.host = BASE_HOST
                                    updateState {
                                        it.copy(loginViewEvent = LoginViewEvent.IrancellPurchaseEvent())
                                    }
                                }
                                MobileNumberHelper.Operator.HAMRAH_AVAL -> performSubscription(username)
                            }
                        }
                        response?.contains("ON") == true -> {
                            hostSelectionInterceptor.host = BASE_HOST
                            updateState(true) { loginViewState ->
                                loginViewState.copy(
                                    loginState = UIState.SUCCESS,
                                    loginViewEvent = LoginViewEvent.LoginSuccessEvent()
                                )
                            }
                        }
                    }
                },
                { throwable ->
                    updateState(true) { loginViewState ->
                        loginViewState.copy(
                            loginState = UIState.ERROR,
                            loginViewEvent = LoginViewEvent.ErrorViewEvent(R.string.error_on_check_member_status)
                        )
                    }
                    onDebug { e { "${TAG()}, $throwable" } }
                })
        )
    }

    //Third
    private fun performSubscription(username: String) {
        compositeDisposable.add(performSubscribeUseCase.execute(PerformSubscribeUseCase.Params(SID, username))
            .doOnSubscribe { _ -> updateState { it.copy(loginState = UIState.LOADING) } }
            .subscribe(
                { response ->
                    when {
                        response?.startsWith("SUCCESS") == true -> {
                            updateState {
                                it.copy(
                                    loginState = UIState.SUCCESS,
                                    loginViewEvent = LoginViewEvent.ShowEnterPinDialog(
                                        response.split(".")[1]
                                    )
                                )
                            }
                        }
                        response?.startsWith("ERROR") == true -> {
                            updateState(true) { loginViewState ->
                                loginViewState.copy(
                                    loginState = UIState.ERROR,
                                    loginViewEvent = LoginViewEvent.ErrorViewEvent(R.string.error_on_subscription)
                                )
                            }
                        }
                    }
                },
                { throwable ->
                    updateState(true) { loginViewState ->
                        loginViewState.copy(
                            loginState = UIState.ERROR,
                            loginViewEvent = LoginViewEvent.ErrorViewEvent(R.string.error_on_subscription)
                        )
                    }
                    onDebug { e { "${TAG()}, $throwable" } }
                })
        )
    }

    //Fourth, Done
    fun submitTransaction(tid: String, pid: String) {
        compositeDisposable.add(submitTransactionUseCase.execute(SubmitTransactionUseCase.Params(tid, pid))
            .doOnSubscribe { _ -> updateState(true) { loginViewState -> loginViewState.copy(pidState = UIState.LOADING) } }
            .subscribe(
                { response ->
                    when {
                        response?.startsWith("ERROR") == true ||
                                response?.startsWith("FAILED") == true -> {
                            updateState(true) { loginViewState ->
                                loginViewState.copy(
                                    pidState = UIState.ERROR,
                                    loginViewEvent = LoginViewEvent.ErrorViewEvent(R.string.error_on_subscription_transaction)
                                )
                            }
                        }
                        response?.startsWith("SUCCESS") == true -> {
                            hostSelectionInterceptor.host = BASE_HOST
                            updateState(true) { loginViewState ->
                                loginViewState.copy(
                                    pidState = UIState.SUCCESS,
                                    loginViewEvent = LoginViewEvent.LoginSuccessEvent()
                                )
                            }
                        }
                    }
                },
                { throwable ->
                    updateState(true) { loginViewState ->
                        loginViewState.copy(
                            pidState = UIState.ERROR,
                            loginViewEvent = LoginViewEvent.ErrorViewEvent(R.string.error_on_subscription_transaction)
                        )
                    }
                    onDebug { e { "${TAG()}, $throwable" } }
                })
        )
    }

    private fun validate(phoneNumber: String): Boolean {
        if (phoneNumber.isEmpty()) {
            updateState { it.copy(loginViewEvent = LoginViewEvent.ValidationStateEvent(LoginViewModel.ValidationState.EMPTY_PHONE_NUMBER)) }
            return false
        }
        if (!MobileNumberHelper.validate(phoneNumber)) {
            updateState { it.copy(loginViewEvent = LoginViewEvent.ValidationStateEvent(LoginViewModel.ValidationState.INVALID_PHONE_NUMBER)) }
            return false
        }
        updateState { it.copy(loginViewEvent = LoginViewEvent.ValidationStateEvent(LoginViewModel.ValidationState.OK)) }
        return true
    }

}