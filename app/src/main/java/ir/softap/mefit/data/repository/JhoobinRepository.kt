package ir.softap.mefit.data.repository

import ir.softap.mefit.data.repository.source.JhoobinApi
import javax.inject.Inject

class JhoobinRepository @Inject constructor(private val jhoobinApi: JhoobinApi) {

    fun unsubscribe(
        packageName: String,
        subscriptionId: String,
        token: String,
        accessToken: String
    ) = jhoobinApi.unsubscribe(packageName, subscriptionId, token, accessToken)

}