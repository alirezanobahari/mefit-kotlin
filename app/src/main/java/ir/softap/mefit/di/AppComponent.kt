package ir.softap.mefit.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ir.softap.mefit.Mefit
import ir.softap.mefit.di.module.*
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
        RestModule::class,
        RxModule::class
    ]
)
interface AppComponent : AndroidInjector<Mefit> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(mefit: Mefit): Builder

        fun build(): AppComponent
    }

}
