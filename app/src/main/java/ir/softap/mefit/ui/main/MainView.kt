package ir.softap.mefit.ui.main

import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import com.etiennelenhart.eiffel.viewmodel.StateViewModel
import javax.inject.Inject

sealed class MainViewEvent : ViewEvent() {

}

data class MainViewState(
    val mainNavigation: MainNavigation = MainNavigation.HOME,
    val mainViewEvent: MainViewEvent? = null
) : ViewState

class MainViewModel @Inject constructor() : StateViewModel<MainViewState>() {

    override val state = MutableLiveData<MainViewState>()

    init {
        if (!stateInitialized)
            state.value = MainViewState()
    }

    fun navigateTo(mainNavigation: MainNavigation) {
        updateState { it.copy(mainNavigation = mainNavigation) }
    }


}