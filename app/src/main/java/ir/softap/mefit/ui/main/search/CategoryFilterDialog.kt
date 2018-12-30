package ir.softap.mefit.ui.main.search

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.dialogfragment.DaggerXBottomPopupDialogFragment
import kotlinx.android.synthetic.main.dialog_category_filter.*

class CategoryFilterDialog : DaggerXBottomPopupDialogFragment() {

    override fun getTheme(): Int {
        return R.style.AppDialog_Fullscreen
    }

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProviders.of(parentFragment!!, viewModelFactory)[SearchViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.dialog_category_filter

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->
        val categoryFilterAdapter = CategoryFilterAdapter({ searchViewModel.fetchCategories() },
            { categoryFilter -> searchViewModel.changeFilter(categoryFilter) })

        with(lstCategoryFilter) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity!!, 3, RecyclerView.VERTICAL, false)
            adapter = categoryFilterAdapter
        }

        searchViewModel.observeState(this@CategoryFilterDialog) { searchViewState ->
            with(categoryFilterAdapter) {
                listState = searchViewState.fetchCategoriesListState
                submitList(searchViewState.categoryFilters)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {

        searchViewModel.filter()
        super.onDismiss(dialog)
    }


}