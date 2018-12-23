package ir.softap.mefit.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ir.softap.mefit.di.scope.ActivityScope
import ir.softap.mefit.ui.main.MainActivity

@Module(includes = [FragmentBindingModule::class])
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
abstract class ActivityBindingModule {

    /**
     * [MainActivity] binder
     */
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

}