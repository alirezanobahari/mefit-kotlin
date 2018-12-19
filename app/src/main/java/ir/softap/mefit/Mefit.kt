package ir.softap.mefit

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector

class Mefit : Application(), HasActivityInjector {

    override fun onCreate() {
        super.onCreate()
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}