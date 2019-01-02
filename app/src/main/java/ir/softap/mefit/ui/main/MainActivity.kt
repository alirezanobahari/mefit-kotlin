package ir.softap.mefit.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.android.billingclient.util.IabHelper
import com.etiennelenhart.eiffel.state.peek
import com.google.android.material.tabs.TabLayout
import ir.softap.mefit.R
import ir.softap.mefit.SessionManager
import ir.softap.mefit.data.model.Session
import ir.softap.mefit.ui.abstraction.DaggerXFragmentActivity
import ir.softap.mefit.ui.common.UIState
import ir.softap.mefit.ui.common.core.CHARKHOONE_RSA_KEY
import ir.softap.mefit.ui.common.core.JHOOBIN_ACCESS_TOKEN
import ir.softap.mefit.ui.common.core.MEFIT_SUBSCRIPTION_SKU
import ir.softap.mefit.ui.login.LoginActivity
import ir.softap.mefit.ui.main.category.CategoryListFragment
import ir.softap.mefit.ui.main.home.HomeFragment
import ir.softap.mefit.ui.main.playlist.PlayListFragment
import ir.softap.mefit.ui.main.profile.ProfileFragment
import ir.softap.mefit.ui.main.search.SearchFragment
import ir.softap.mefit.utilities.LocalStorage
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

    private lateinit var iabHelper: IabHelper

    override val layoutRes: Int = R.layout.activity_main

    override val initViews: (Bundle?) -> Unit = { saveInstanceState ->

        iabHelper = IabHelper(this, CHARKHOONE_RSA_KEY).also { it.startSetup { } }

        if (saveInstanceState == null) {
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
                    // 3 -> mainViewModel.navigateTo(MainNavigation.PLAYLIST)
                    3 -> mainViewModel.navigateTo(MainNavigation.PROFILE)
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

            viewActionLoading.visibility =
                    if (mainViewState.unsubscribeState == UIState.LOADING) View.VISIBLE else View.GONE

            if (mainViewState.unsubscribeState == UIState.SUCCESS) {
                SessionManager.session = null
                LocalStorage.with(this).delete(Session::class.java.name)
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }

            mainViewState.mainViewEvent?.peek { mainViewEvent ->
                when (mainViewEvent) {
                    is MainViewEvent.UnsubscribeEvent -> {
                        unsubscribeIrancell()
                        true
                    }
                }
            }
        }
    }

    private fun unsubscribeIrancell() {
        iabHelper.queryInventoryAsync { _, inventory ->
            val subscription = inventory.getPurchase(MEFIT_SUBSCRIPTION_SKU)
            if (subscription != null && subscription.isAutoRenewing) {
                mainViewModel.unsubscribeRequest(
                    packageName,
                    MEFIT_SUBSCRIPTION_SKU,
                    subscription.token,
                    JHOOBIN_ACCESS_TOKEN
                )
            }
        }
    }

}
