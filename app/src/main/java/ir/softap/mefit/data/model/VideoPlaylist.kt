package ir.softap.mefit.data.model

import android.nfc.Tag

data class VideoPlaylist(
    val id: Int,
    val video: Int,
    val title: String,
    val visits: Int,
    val thumbnail: String,
    val category: Category,
    val issuer: Video.Issuer,
    val tags: List<Tag>,
    val like: Video.Like,
    val duration: String,
    val videoDetail: Video.VideoDetail,
    val order: Int,
    val premium: Boolean?
)