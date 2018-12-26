package ir.softap.mefit.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ir.softap.mefit.di.scope.ActivityScope
import ir.softap.mefit.ui.login.LoginActivity
import ir.softap.mefit.ui.main.MainActivity
import ir.softap.mefit.ui.splash.SplashActivity

@Module(
    includes = [
        FragmentBindingModule::class,
        DialogFragmentBindingModule::class
    ]
)
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
abstract class ActivityBindingModule {

    /**
     * [SplashActivity] binder
     */
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindSplashActivity(): SplashActivity

    /**
     * [LoginActivity] binder
     */
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindLoginActivity(): LoginActivity

    /**
     * [MainActivity] binder
     */
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

}