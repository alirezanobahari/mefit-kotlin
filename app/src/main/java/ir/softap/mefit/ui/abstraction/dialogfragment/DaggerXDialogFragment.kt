package ir.softap.mefit.ui.abstraction.dialogfragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import ir.softap.mefit.ui.abstraction.dialogfragment.BaseDialogFragment
import javax.inject.Inject

abstract class DaggerXDialogFragment : BaseDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    open fun onBackPressed(): Boolean = false

}