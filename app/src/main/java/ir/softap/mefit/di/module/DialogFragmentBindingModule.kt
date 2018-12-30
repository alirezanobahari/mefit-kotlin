package ir.softap.mefit.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ir.softap.mefit.di.scope.DialogFragmentScope
import ir.softap.mefit.ui.login.EnterPidDialog
import ir.softap.mefit.ui.main.search.CategoryFilterDialog
import ir.softap.mefit.ui.video.show.CommentDialog

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

    /**
     * [CommentDialog] binder
     */
    @DialogFragmentScope
    @ContributesAndroidInjector
    abstract fun bindCommentDialog(): CommentDialog

    /**
     * [CategoryFilterDialog] binder
     */
    @DialogFragmentScope
    @ContributesAndroidInjector
    abstract fun bindCategoryFilterDialog(): CategoryFilterDialog

}