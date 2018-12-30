package ir.softap.mefit.ui.main.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.decoration.GridSpacingItemDecoration
import ir.softap.mefit.ui.video.show.VideoShowActivity
import ir.softap.mefit.utilities.extensions.toPx
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
        lstSearch.layoutManager = GridLayoutManager(context!!, 2)
        lstSearch.addItemDecoration(GridSpacingItemDecoration(2, 8.toPx, true))
        lstSearch.adapter = videoListAdapter

        searchViewModel.observeState(this) { searchViewState ->
            srlSearch.isRefreshing = searchViewState.searchListState == ListState.LOADING
            videoListAdapter.listState = searchViewState.searchListState
            videoListAdapter.submitList(searchViewState.videos)

        }

    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
        fragmentDispatchingAndroidInjector

}