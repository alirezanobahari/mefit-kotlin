package ir.softap.mefit.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ir.softap.mefit.di.scope.ApplicationScope

@ApplicationScope
@Component(
    modules =
    [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityBindingModule::class,
        ViewModelFactoryModule::class,
        NetworkModule::class,
        RestModule::class
    ]
)
interface AppComponent : AndroidInjector<AppController> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(appController: AppController): Builder

        fun build(): AppComponent
    }

}
