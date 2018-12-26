package ir.softap.mefit.ui.main.search

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.decoration.GridSpacingItemDecoration
import ir.softap.mefit.utilities.extensions.toPx
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : DaggerXFragment() {

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SearchViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.fragment_search

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->

        srlSearchResult.setOnRefreshListener { searchViewModel.refresh() }

        val videoPageListAdapter = VideoPageListAdapter(
            this@SearchFragment,
            { searchViewModel.retry() },
            { }
        )
        lstSearchResult.layoutManager = GridLayoutManager(context!!, 2)
        lstSearchResult.addItemDecoration(GridSpacingItemDecoration(2, 8.toPx, true))
        lstSearchResult.adapter = videoPageListAdapter

        searchViewModel.videoListLiveData.observe(this, Observer {
            videoPageListAdapter.submitList(it)
        })

        searchViewModel.observeState(this) { searchViewState ->

            srlSearchResult.isRefreshing = searchViewState.searchListState == ListState.LOADING

            videoPageListAdapter.listState = searchViewState.searchListState

        }

    }

}