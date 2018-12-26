package ir.softap.mefit.data.network

import ir.softap.mefit.di.scope.ApplicationScope
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

@ApplicationScope
class AuthInterceptor @Inject constructor() : Interceptor {

    var token: String = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
            .newBuilder()
            .addHeader(AUTH, token)
            .build()
        return chain.proceed(request)
    }

}