package ir.softap.mefit.di.module

import dagger.Module
import dagger.Provides
import ir.softap.mefit.data.repository.source.CategoryApi
import ir.softap.mefit.data.repository.source.RpgApi
import ir.softap.mefit.data.repository.source.UserApi
import ir.softap.mefit.data.repository.source.VideoApi
import ir.softap.mefit.di.scope.ApplicationScope
import retrofit2.Retrofit

@Module
class RestModule {

    /**
     * Provides the RpgApi service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the RpgApi service implementation.
     */
    @ApplicationScope
    @Provides
    fun provideRpgApi(retrofit: Retrofit): RpgApi {
        return retrofit.create(RpgApi::class.java)
    }

    /**
     * Provides the UserApi service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the UserApi service implementation.
     */
    @ApplicationScope
    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    /**
     * Provides the VideoApi service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the VideoApi service implementation.
     */
    @ApplicationScope
    @Provides
    fun provideAccountApi(retrofit: Retrofit): VideoApi {
        return retrofit.create(VideoApi::class.java)
    }

    /**
     * Provides the CategoryApi service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the CategoryApi service implementation.
     */
    @ApplicationScope
    @Provides
    fun provideCategoryApi(retrofit: Retrofit): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }


}