package ir.softap.mefit

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import ir.softap.mefit.di.DaggerAppComponent
import ir.softap.mefit.ui.common.core.LocaleManager
import ir.softap.mefit.utilities.extensions.stringArrays
import net.jhoobin.jhub.CharkhoneSdkApp
import javax.inject.Inject

class Mefit : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        initInAppPurchaseSdk()
        initLocaleChanger()
        initDagger()
        initStetho()
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManager.get().onConfigurationChanged()
    }

    private fun initInAppPurchaseSdk() {
        CharkhoneSdkApp.initSdk(this, stringArrays[R.array.secrets])
    }

    private fun initLocaleChanger() {
        LocaleManager.with(this).apply {
            changeLocale(LocaleManager.FA)
        }
    }

    private fun initDagger() = DaggerAppComponent.builder()
        .application(this)
        .build()
        .inject(this)

    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }

}