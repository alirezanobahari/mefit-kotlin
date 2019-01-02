package ir.softap.mefit.ui.main.profile

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import io.reactivex.disposables.CompositeDisposable
import ir.softap.mefit.R
import ir.softap.mefit.SessionManager
import ir.softap.mefit.data.model.request.EditProfileRequest
import ir.softap.mefit.domain.interactor.EditProfileUseCase
import ir.softap.mefit.ui.abstraction.DisposableStateViewModel
import ir.softap.mefit.ui.common.UIState
import ir.softap.mefit.utilities.ValidationHelper
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import javax.inject.Inject

sealed class ProfileViewEvent : ViewEvent() {
    data class ValidationStateEvent(val validationState: ValidationState) : ProfileViewEvent()
    class EditProfileSuccessfulEvent : ProfileViewEvent()
    data class ErrorViewEvent(@StringRes val message: Int) : ProfileViewEvent()
}

data class ProfileViewState(
    val editProfileState: UIState = UIState.IDLE,
    val profileViewEvent: ProfileViewEvent? = null
) : ViewState

enum class ValidationState {
    EMPTY_FIRST_NAME, EMPTY_LAST_NAME, EMPTY_EMAIL, INVALID_EMAIL, OK
}

class ProfileViewModel @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val editProfileUseCase: EditProfileUseCase
) : DisposableStateViewModel<ProfileViewState>(compositeDisposable) {

    override val state = MutableLiveData<ProfileViewState>()

    init {
        if (!stateInitialized)
            state.value = ProfileViewState()
    }

    fun editProfile(firstName: String, lastName: String, email: String) {
        if (validate(firstName, lastName, email)) {
            compositeDisposable.add(editProfileUseCase.execute(
                EditProfileUseCase.Params(
                    SessionManager.session?.token!!,
                    EditProfileRequest(firstName, lastName, email)
                )
            ).doOnSubscribe { _ -> updateState(true) { it.copy(editProfileState = UIState.ERROR) } }
                .subscribe(
                    { user ->
                        SessionManager.session = SessionManager.session?.copy(user = user)
                        updateState(true) { it.copy(profileViewEvent = ProfileViewEvent.EditProfileSuccessfulEvent()) }
                    },
                    { throwable ->
                        updateState(true) { profileViewState ->
                            profileViewState.copy(
                                editProfileState = UIState.ERROR,
                                profileViewEvent = ProfileViewEvent.ErrorViewEvent(R.string.error_edit_profile)
                            )
                        }
                        e { "${TAG()}, error on edit profile $throwable" }
                    })
            )
        }
    }

    private fun validate(firstName: String, lastName: String, email: String): Boolean {
        if (firstName.isEmpty()) {
            updateState { it.copy(profileViewEvent = ProfileViewEvent.ValidationStateEvent(ValidationState.EMPTY_FIRST_NAME)) }
            return false
        }
        if (lastName.isEmpty()) {
            updateState { it.copy(profileViewEvent = ProfileViewEvent.ValidationStateEvent(ValidationState.EMPTY_LAST_NAME)) }
            return false
        }
        if (email.isEmpty()) {
            updateState { it.copy(profileViewEvent = ProfileViewEvent.ValidationStateEvent(ValidationState.EMPTY_EMAIL)) }
            return false
        }
        if (!ValidationHelper.isValidEmail(email)) {
            updateState { it.copy(profileViewEvent = ProfileViewEvent.ValidationStateEvent(ValidationState.INVALID_EMAIL)) }
            return false
        }
        updateState { it.copy(profileViewEvent = ProfileViewEvent.ValidationStateEvent(ValidationState.OK)) }
        return true
    }
}