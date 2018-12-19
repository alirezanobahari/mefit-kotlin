package ir.softap.mefit.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ir.softap.mefit.AppController
import ir.softap.mefit.di.scope.ApplicationScope


@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
class AppModule {

    @ApplicationScope
    @Provides
    fun provideApplicationContext(appController: AppController): Context = appController.applicationContext

}