package ir.softap.mefit.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ir.softap.mefit.di.scope.FragmentScope
import ir.softap.mefit.ui.main.category.CategoryListFragment
import ir.softap.mefit.ui.main.home.HomeFragment
import ir.softap.mefit.ui.main.playlist.PlayListFragment
import ir.softap.mefit.ui.main.profile.ProfileFragment
import ir.softap.mefit.ui.main.search.SearchFragment
import ir.softap.mefit.ui.video.show.VideoDetailFragment
import ir.softap.mefit.ui.video.show.VideoPlayerFragment

@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
abstract class FragmentBindingModule {

    /**
     * [HomeFragment] binder
     */
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): HomeFragment

    /**
     * [CategoryListFragment] binder
     */
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindCategoryListFragment(): CategoryListFragment

    /**
     * [SearchFragment] binder
     */
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindSearchFragment(): SearchFragment

    /**
     * [PlayListFragment] binder
     */
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindPlayListFragment(): PlayListFragment

    /**
     * [ProfileFragment] binder
     */
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindProfileFragment(): ProfileFragment

    /**
     * [VideoPlayerFragment] binder
     */
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindVideoPlayerFragment(): VideoPlayerFragment

    /**
     * [VideoDetailFragment] binder
     */
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindVideoDetailFragment(): VideoDetailFragment

}