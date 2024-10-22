package ir.softap.mefit.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ir.softap.mefit.di.DaggerViewModelFactory
import ir.softap.mefit.di.ViewModelKey
import ir.softap.mefit.ui.intro.IntroViewModel
import ir.softap.mefit.ui.login.LoginViewModel
import ir.softap.mefit.ui.main.MainViewModel
import ir.softap.mefit.ui.main.category.CategoryListViewModel
import ir.softap.mefit.ui.main.home.HomeViewModel
import ir.softap.mefit.ui.main.profile.ProfileViewModel
import ir.softap.mefit.ui.main.search.SearchViewModel
import ir.softap.mefit.ui.splash.SplashViewModel
import ir.softap.mefit.ui.video.list.VideoListViewModel
import ir.softap.mefit.ui.video.show.VideoShowViewModel

@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun bindSplashViewModel(view: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IntroViewModel::class)
    internal abstract fun bindIntroViewModel(view: IntroViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(view: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(view: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModel(view: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryListViewModel::class)
    internal abstract fun bindCategoryListViewModel(view: CategoryListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindSearchViewModel(view: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun bindProfileViewModel(view: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VideoListViewModel::class)
    internal abstract fun bindVideoListViewModel(view: VideoListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VideoShowViewModel::class)
    internal abstract fun bindVideoShowViewModel(view: VideoShowViewModel): ViewModel

}