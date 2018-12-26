package ir.softap.mefit.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragmentActivity
import ir.softap.mefit.ui.main.category.CategoryListFragment
import ir.softap.mefit.ui.main.home.HomeFragment
import ir.softap.mefit.ui.main.playlist.PlayListFragment
import ir.softap.mefit.ui.main.profile.ProfileFragment
import ir.softap.mefit.ui.main.search.SearchFragment
import ir.softap.mefit.utilities.extensions.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerXFragmentActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
    }

    private val homeFragment = HomeFragment()
    private val categoryListFragment = CategoryListFragment()
    private val searchFragment = SearchFragment()
    private val playListFragment = PlayListFragment()
    private val profileFragment = ProfileFragment()

    override val layoutRes: Int = R.layout.activity_main

    override val initViews: (Bundle?) -> Unit = {

        if (it == null) {
            replaceFragment(homeFragment, R.id.flMainContainer)
        }

        tabMainNavigation.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> mainViewModel.navigateTo(MainNavigation.HOME)
                    1 -> mainViewModel.navigateTo(MainNavigation.CATEGORY)
                    2 -> mainViewModel.navigateTo(MainNavigation.SEARCH)
                    3 -> mainViewModel.navigateTo(MainNavigation.PLAYLIST)
                    4 -> mainViewModel.navigateTo(MainNavigation.PROFILE)
                }
            }

        })

        mainViewModel.observeState(this) { mainViewState ->
            when (mainViewState.mainNavigation) {
                MainNavigation.HOME -> replaceFragment(homeFragment, R.id.flMainContainer)
                MainNavigation.CATEGORY -> replaceFragment(categoryListFragment, R.id.flMainContainer)
                MainNavigation.SEARCH -> replaceFragment(searchFragment, R.id.flMainContainer)
                MainNavigation.PLAYLIST -> replaceFragment(playListFragment, R.id.flMainContainer)
                MainNavigation.PROFILE -> replaceFragment(profileFragment, R.id.flMainContainer)
            }
        }

    }
}
