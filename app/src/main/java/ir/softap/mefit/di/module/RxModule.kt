package ir.softap.mefit.di.module

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
class RxModule {

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()
    
}