package ir.softap.mefit.data.network

import ir.softap.mefit.di.scope.ApplicationScope
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@ApplicationScope
class HostSelectionInterceptor @Inject constructor() : Interceptor {

    @Volatile
    var host: String = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url()
            .newBuilder()
            .host(host)
            .build()
        return chain.proceed(
            request.newBuilder()
                .url(newUrl)
                .build()
        )
    }

}