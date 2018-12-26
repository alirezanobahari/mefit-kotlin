package ir.softap.mefit.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ir.softap.mefit.Mefit
import ir.softap.mefit.di.scope.ApplicationScope
import ir.softap.mefit.data.executor.ThreadExecutor
import ir.softap.mefit.data.executor.ThreadExecutorImpl
import ir.softap.mefit.domain.executor.PostExecutionThread
import ir.softap.mefit.domain.executor.UiThread
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
class AppModule {

    @ApplicationScope
    @Provides
    fun provideApplicationContext(mefit: Mefit): Context = mefit.applicationContext

    @ApplicationScope
    @Provides
    internal fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @ApplicationScope
    @Provides
    internal fun provideThreadExecutor(): ThreadExecutor {
        return ThreadExecutorImpl()
    }

    @ApplicationScope
    @Provides
    internal fun providePostExecutionThread(uiThread: UiThread): PostExecutionThread {
        return uiThread
    }

}