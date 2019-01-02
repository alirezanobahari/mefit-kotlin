package ir.softap.mefit.data.repository.source

import io.reactivex.Completable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JhoobinApi {

    @GET("/ws/androidpublisher/v2/applications/{packageName}/purchases/subscriptions/{subscriptionId}/tokens/{token}:cancel")
    fun unsubscribe(
        @Path("packageName") packageName: String,
        @Path("subscriptionId") subscriptionId: String,
        @Path("token") token: String,
        @Query("access_token") accessToken: String
    ): Completable

}