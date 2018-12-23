package ir.softap.mefit.ui.main.category

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.ViewEvent
import com.etiennelenhart.eiffel.state.ViewState
import io.reactivex.disposables.CompositeDisposable
import ir.softap.mefit.R
import ir.softap.mefit.data.model.Category
import ir.softap.mefit.domain.interactor.FetchCategoriesUseCase
import ir.softap.mefit.ui.abstraction.DisposableStateViewModel
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import ir.softap.mefit.utilities.onDebug
import javax.inject.Inject

sealed class CategoryListViewEvent : ViewEvent() {
    data class ErrorViewEvent(@StringRes val message: Int) : CategoryListViewEvent()
}

data class CategoryListViewState(
    val categoryListState: ListState = ListState.IDLE,
    val categoryList: List<Category> = emptyList(),
    val categoryListViewEvent: CategoryListViewEvent? = null
) : ViewState

class CategoryListViewModel @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val fetchCategoriesUseCase: FetchCategoriesUseCase
) :
    DisposableStateViewModel<CategoryListViewState>(compositeDisposable) {

    override val state = MutableLiveData<CategoryListViewState>()

    init {
        if (!stateInitialized)
            state.value = CategoryListViewState()
        fetchCategoryList()
    }

    fun fetchCategoryList() {
        compositeDisposable.add(fetchCategoriesUseCase.execute(Unit)
            .doOnSubscribe { _ -> updateState { it.copy(categoryListState = ListState.LOADING) } }
            .subscribe(
                { categories ->
                    updateState { categoryListViewState ->
                        categoryListViewState.copy(
                            categoryListState = ListState.SUCCESS.withItemCount(categories.size),
                            categoryList = categories
                        )
                    }
                },
                { throwable ->
                    updateState { categoryListViewState ->
                        categoryListViewState.copy(
                            categoryListState = ListState.ERROR,
                            categoryListViewEvent = CategoryListViewEvent.ErrorViewEvent(R.string.msg_error_occurred)
                        )
                    }
                    onDebug { e { "${TAG()} $throwable" } }
                })
        )
    }

}