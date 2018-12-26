package ir.softap.mefit.data.repository.source

import io.reactivex.Single
import ir.softap.mefit.data.network.AUTH
import ir.softap.mefit.ui.common.core.HAMRAH_AVAL_USERNAME
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RpgApi {

    @FormUrlEncoded
    @POST("api/member/status")
    fun memberStatus(
        @Header(AUTH) auth: String = HAMRAH_AVAL_USERNAME,
        @Field("sid") sid: String,
        @Field("mobile") mobile: String
    ): Single<String>

    @FormUrlEncoded
    @POST("api/member/sub")
    fun subscribe(
        @Header(AUTH) auth: String = HAMRAH_AVAL_USERNAME,
        @Field("sid") sid: String,
        @Field("mobile") mobile: String
    ): Single<String>

    @FormUrlEncoded
    @POST("api/member/check")
    fun checkSubscription(
        @Header(AUTH) auth: String = HAMRAH_AVAL_USERNAME,
        @Field("tid") tid: String,
        @Field("pin") pin: String
    ): Single<String>

}