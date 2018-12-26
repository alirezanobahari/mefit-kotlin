package ir.softap.mefit.data.repository

import io.reactivex.Single
import ir.softap.mefit.data.repository.source.RpgApi
import retrofit2.Response
import retrofit2.http.POST
import javax.inject.Inject

class RpgRepository @Inject constructor(private val rpgApi: RpgApi) {

    fun memberStatus(
        sid: String,
        mobile: String
    ): Single<String> = rpgApi.memberStatus(sid = sid, mobile = mobile)

    fun subscribe(
        sid: String,
        mobile: String
    ): Single<String> = rpgApi.subscribe(sid = sid, mobile = mobile)

    fun checkSubscription(
        tid: String,
        pin: String
    ): Single<String> = rpgApi.checkSubscription(tid = tid, pin = pin)


}