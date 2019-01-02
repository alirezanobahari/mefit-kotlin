package ir.softap.mefit.ui.intro

import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import com.etiennelenhart.eiffel.viewmodel.StateViewModel
import javax.inject.Inject

sealed class IntroViewEvent : ViewEvent() {
    data class NextEvent(val page: Int) : IntroViewEvent()
    data class PreviousEvent(val page: Int) : IntroViewEvent()
}

data class IntroViewState(
    val currentPage: Int = 0,
    val introViewEvent: IntroViewEvent? = null
) : ViewState

class IntroViewModel @Inject constructor() : StateViewModel<IntroViewState>() {

    override val state = MutableLiveData<IntroViewState>()

    val lastIntroPosition = 2

    init {
        if (!stateInitialized)
            state.value = IntroViewState()
    }

    fun gotoNext() {
        if (lastIntroPosition > state.value?.currentPage!!) {
            val nextPage = state.value?.currentPage!! + 1
            updateState { introViewState ->
                introViewState.copy(
                    currentPage = nextPage,
                    introViewEvent = IntroViewEvent.NextEvent(nextPage)
                )
            }
        }
    }

    fun gotoPrevious() {
        if (0 < state.value?.currentPage!!) {
            val prevPage = state.value?.currentPage!! - 1
            updateState { introViewState ->
                introViewState.copy(
                    currentPage = prevPage,
                    introViewEvent = IntroViewEvent.PreviousEvent(prevPage)
                )
            }
        }
    }

}