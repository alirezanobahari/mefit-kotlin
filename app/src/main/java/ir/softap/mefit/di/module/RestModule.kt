package ir.softap.mefit.di.module

import dagger.Module
import dagger.Provides
import ir.softap.mefit.data.repository.source.PostsApi
import ir.softap.mefit.di.scope.ApplicationScope
import retrofit2.Retrofit

@Module
class RestModule {


    /**
     * Provides the PostsApi service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the PostsApi service implementation.
     */
    @ApplicationScope
    @Provides
    fun provideAccountApi(retrofit: Retrofit): PostsApi {
        return retrofit.create(PostsApi::class.java)
    }


}