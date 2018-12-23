package ir.softap.mefit.ui.main.category

import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.ToastBuilder
import ir.softap.mefit.ui.common.decoration.EqualSpacingItemDecoration
import ir.softap.mefit.utilities.extensions.colors
import ir.softap.mefit.utilities.extensions.strings
import ir.softap.mefit.utilities.extensions.toPx
import kotlinx.android.synthetic.main.fragment_category_list.view.*

class CategoryListFragment : DaggerXFragment() {

    private val categoryListViewModel: CategoryListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[CategoryListViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.fragment_category_list

    override val initViews: View.() -> Unit = {

        srlCategory.setColorSchemeColors(context.colors[R.color.colorPrimary])
        srlCategory.setOnRefreshListener { categoryListViewModel.fetchCategoryList() }

        val categoryAdapter = CategoryAdapter(this@CategoryListFragment,
            { categoryListViewModel.fetchCategoryList() },
            {

            })
        lstCategory.layoutManager = GridLayoutManager(context, 2)
        lstCategory.addItemDecoration(EqualSpacingItemDecoration(16.toPx, EqualSpacingItemDecoration.GRID, true))
        lstCategory.adapter = categoryAdapter

        categoryListViewModel.observeState(this@CategoryListFragment) { categoryListViewModel ->

            srlCategory.isRefreshing = categoryListViewModel.categoryList == ListState.LOADING

            with(categoryAdapter) {
                listState = categoryListViewModel.categoryListState
                submitList(categoryListViewModel.categoryList)
            }

            categoryListViewModel.categoryListViewEvent?.apply {
                if (!handled) {
                    when (this) {
                        is CategoryListViewEvent.ErrorViewEvent -> ToastBuilder.showError(
                            context,
                            context.strings[this.message]
                        )
                    }
                }
            }
        }
    }
}