package ir.softap.mefit.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import ir.softap.mefit.ui.common.ListState
import ir.softap.mefit.ui.common.ToastBuilder
import ir.softap.mefit.ui.common.decoration.EqualSpacingItemDecoration
import ir.softap.mefit.utilities.extensions.colors
import ir.softap.mefit.utilities.extensions.strings
import ir.softap.mefit.utilities.extensions.toPx
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : DaggerXFragment() {

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.fragment_home

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->

        srlHome.setColorSchemeColors(context!!.colors[R.color.colorPrimary])
        srlHome.setOnRefreshListener { homeViewModel.fetchHome() }

        val homeAdapter = HomeAdapter(this@HomeFragment,
            retry = { homeViewModel.fetchHome() },
            homeSelect = {},
            videoSelect = {})
        lstHome.layoutManager = LinearLayoutManager(context)
        lstHome.addItemDecoration(EqualSpacingItemDecoration(16.toPx, EqualSpacingItemDecoration.VERTICAL, true))
        lstHome.adapter = homeAdapter

        homeViewModel.observeState(this@HomeFragment) { homeViewState ->

            srlHome.isRefreshing = homeViewState.homeListState == ListState.LOADING

            with(homeAdapter) {
                listState = homeViewState.homeListState
                submitList(homeViewState.homeList)
            }

            homeViewState.homeViewEvent?.apply {
                if (!handled) {
                    when (this) {
                        is HomeViewEvent.ErrorViewEvent -> ToastBuilder.showError(
                            context!!,
                            context!!.strings[this.message]
                        )
                    }
                }
            }
        }
    }
}