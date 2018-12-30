package ir.softap.mefit.ui.main.profile

import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import io.reactivex.disposables.CompositeDisposable
import ir.softap.mefit.ui.abstraction.DisposableStateViewModel
import ir.softap.mefit.ui.common.UIState
import javax.inject.Inject

sealed class ProfileViewEvent : ViewEvent() {
    data class ValidationStateViewEvent(val validationState: ValidationState = ValidationState.OK)
}

data class ProfileViewState(
    val editProfileState: UIState = UIState.IDLE,
    val profileViewEvent: ProfileViewEvent? = null
) : ViewState

enum class ValidationState {
    EMPTY_FIRST_NAME, EMPTY_LAST_NAME, EMPTY_EMAIL, INVALID_EMAIL, OK
}

class ProfileViewModel @Inject constructor(
    compositeDisposable: CompositeDisposable
    ) : DisposableStateViewModel<ProfileViewState>(compositeDisposable) {

    override val state = MutableLiveData<ProfileViewState>()

    init {
        if (!stateInitialized)
            state.value = ProfileViewState()
    }

    fun editProfile(firstName: String, lastName: String, email: String) {
        if (validate(firstName, lastName, email)) {

        }
    }

    private fun validate(firstName: String, lastName: String, email: String): Boolean {
        return true
    }
}