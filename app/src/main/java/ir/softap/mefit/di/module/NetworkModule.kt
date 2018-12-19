package ir.softap.mefit.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import dagger.Module
import dagger.Provides
import ir.softap.mefit.data.network.EnumRetrofitConverterFactory
import ir.softap.mefit.di.scope.ApplicationScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
class NetworkModule {

    @ApplicationScope
    @Provides
    fun coroutineContextProvider(): CoroutineContext = Dispatchers.IO

    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @ApplicationScope
    @Provides
    fun provideRetrofitInterface(
        okHttpClient: OkHttpClient,
        gson: Gson,
        enumRetrofitConverterFactory: EnumRetrofitConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(enumRetrofitConverterFactory)
            .client(okHttpClient)
            .build()
    }

    /**
     * Provides the OkHttp client object
     * @param authInterceptor the AuthInterceptor object used to add Auth header to the network calls
     * @param cache the Cache object for caching some request data
     * @return the OkHttpClient object
     */
    @ApplicationScope
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
        return okHttpClientBuilder.build()
    }

    /**
     * Provides the Gson object
     * @return the Gson object
     */
    @ApplicationScope
    @Provides
    fun provideGson(): Gson =
        GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
            .create()

}