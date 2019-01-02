package ir.softap.mefit.ui.main.category

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.etiennelenhart.eiffel.state.peek
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.ToastBuilder
import ir.softap.mefit.ui.main.category.CategoryAdapter.Companion.VIEW_TYPE_STATE
import ir.softap.mefit.ui.video.list.VideoListActivity
import ir.softap.mefit.utilities.extensions.colors
import ir.softap.mefit.utilities.extensions.strings
import kotlinx.android.synthetic.main.fragment_category_list.*

class CategoryListFragment : DaggerXFragment() {

    private val categoryListViewModel: CategoryListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[CategoryListViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.fragment_category_list

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->

        srlCategory.setColorSchemeColors(context!!.colors[R.color.colorPrimary])
        srlCategory.setOnRefreshListener { categoryListViewModel.fetchCategoryList() }

        val categoryAdapter = CategoryAdapter(this@CategoryListFragment,
            { categoryListViewModel.fetchCategoryList() },
            { category -> startActivity(VideoListActivity.newIntent(context!!, category.title, category.id)) })

        lstCategory.layoutManager = GridLayoutManager(context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    if (categoryAdapter.getItemViewType(position) == VIEW_TYPE_STATE) spanCount
                    else 1
            }
        }
        lstCategory.adapter = categoryAdapter

        categoryListViewModel.observeState(this@CategoryListFragment) { categoryListViewModel ->

            srlCategory.isRefreshing = categoryListViewModel.categoryList == ListState.LOADING

            with(categoryAdapter) {
                listState = categoryListViewModel.categoryListState
                submitList(categoryListViewModel.categoryList)
            }

            categoryListViewModel.categoryListViewEvent?.peek { categoryListViewEvent ->
                when (categoryListViewEvent) {
                    is CategoryListViewEvent.ErrorViewEvent -> {
                        ToastBuilder.showError(
                            context!!,
                            context!!.strings[categoryListViewEvent.message]
                        )
                        true
                    }
                }
            }
        }
    }
}