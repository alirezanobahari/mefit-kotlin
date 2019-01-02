package ir.softap.mefit.ui.main.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.etiennelenhart.eiffel.state.peek
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.ToastBuilder
import ir.softap.mefit.ui.video.show.VideoShowActivity
import ir.softap.mefit.utilities.extensions.strings
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : DaggerXFragment(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SearchViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.fragment_search

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->

        srlSearch.setOnRefreshListener { searchViewModel.filter() }

        btnFilter.setOnClickListener { CategoryFilterDialog().show(childFragmentManager, "categoryFilterDialog") }

        edSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
                with(searchViewModel) {
                    setQuery(edSearch.text.toString())
                    filter()
                }
            true
        }

        btnSearch.setOnClickListener {
            with(searchViewModel) {
                setQuery(edSearch.text.toString())
                filter()
            }
        }

        val videoListAdapter = VideoListAdapter(
            { searchViewModel.retry() },
            { video -> startActivity(VideoShowActivity.newIntent(context!!, video)) }
        )
        lstSearch.layoutManager = GridLayoutManager(context!!, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    if (videoListAdapter.getItemViewType(position) == VideoListAdapter.VIEW_TYPE_STATE) spanCount
                    else 1
            }
        }
        lstSearch.adapter = videoListAdapter

        searchViewModel.observeState(this) { searchViewState ->
            srlSearch.isRefreshing = searchViewState.searchListState == ListState.LOADING
            videoListAdapter.listState = searchViewState.searchListState
            videoListAdapter.submitList(searchViewState.videos)

            searchViewState.searchViewEvent?.peek { searchViewEvent ->
                when (searchViewEvent) {
                    is SearchViewEvent.ErrorViewEvent -> {
                        ToastBuilder.showError(context!!, context!!.strings[searchViewEvent.errorMessage])
                        true
                    }
                }
            }

        }

    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
        fragmentDispatchingAndroidInjector

}