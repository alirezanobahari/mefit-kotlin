package ir.softap.mefit.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ir.softap.mefit.di.DaggerViewModelFactory
import ir.softap.mefit.di.ViewModelKey
import ir.softap.mefit.ui.main.MainViewModel

@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindHomeViewModel(viewModel: MainViewModel): ViewModel

}