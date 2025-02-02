package ir.softap.mefit.data.repository.source

import io.reactivex.Single
import ir.softap.mefit.data.model.Home
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.data.model.request.query.FilterVideoQuery
import ir.softap.mefit.data.network.AUTH
import ir.softap.mefit.data.network.CAT_SLUG
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface VideoApi {

    @GET(CAT_SLUG)
    fun fetchHome(): Single<List<Home>>

    @GET("$CAT_SLUG/filter")
    fun filterVideos(@QueryMap filterVideoQuery: FilterVideoQuery): Single<List<Video>>

    @GET("$CAT_SLUG/get-video")
    fun fetchVideoDetail(
        @Header(AUTH) token: String,
        @Query("id") videoId: Int
    ): Single<Video.VideoDetail>

    @GET("$CAT_SLUG/related")
    fun fetchSuggestions(@Query("id") videoId: Int): Single<List<Video>>

}