package ir.softap.mefit.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ir.softap.mefit.di.DaggerViewModelFactory
import ir.softap.mefit.di.ViewModelKey
import ir.softap.mefit.ui.main.MainViewModel
import ir.softap.mefit.ui.main.category.CategoryListViewModel
import ir.softap.mefit.ui.main.home.HomeViewModel

@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

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

}