package ir.softap.mefit.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ir.softap.mefit.di.scope.DialogFragmentScope
import ir.softap.mefit.ui.login.EnterPidDialog

@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
abstract class DialogFragmentBindingModule {

    /**
     * [EnterPidDialog] binder
     */
    @DialogFragmentScope
    @ContributesAndroidInjector
    abstract fun bindEnterPidDialog(): EnterPidDialog

}