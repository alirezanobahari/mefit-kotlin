package ir.softap.mefit.data.repository

import io.reactivex.Single
import ir.softap.mefit.data.model.Home
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.data.model.request.query.FilterVideoQuery
import ir.softap.mefit.data.repository.source.VideoApi
import javax.inject.Inject

class VideoRepository @Inject constructor(private val videoApi: VideoApi) {

    fun fetchHome(): Single<List<Home>> = videoApi.fetchHome()

    fun filterVideos(filterVideoQuery: FilterVideoQuery): Single<List<Video>> =
        videoApi.filterVideos(filterVideoQuery)

    fun fetchVideoDetail(token: String, videoId: Int): Single<Video.VideoDetail> =
        videoApi.fetchVideoDetail(token, videoId)

    fun fetchSuggestions(videoId: Int): Single<List<Video>> =
        videoApi.fetchSuggestions(videoId)

}