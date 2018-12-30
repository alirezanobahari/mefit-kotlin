package ir.softap.mefit.di.module

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import dagger.Module
import dagger.Provides
import ir.softap.mefit.data.network.BASE_HOST
import ir.softap.mefit.data.network.BASE_URL
import ir.softap.mefit.data.network.EnumRetrofitConverterFactory
import ir.softap.mefit.data.network.HostSelectionInterceptor
import ir.softap.mefit.di.scope.ApplicationScope
import ir.softap.mefit.utilities.onDebug
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

const val RPG_RETROFIT_SERVICE = "RPG_RETROFIT_SERVICE"

@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
class NetworkModule {

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
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(enumRetrofitConverterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
    fun provideOkHttpClient(hostSelectionInterceptor: HostSelectionInterceptor): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .apply { onDebug { addNetworkInterceptor(StethoInterceptor()) } }
            .addInterceptor(hostSelectionInterceptor.apply { host = BASE_HOST })
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
            .setLenient()
            .create()

}