package ir.softap.mefit.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Comment(
    @SerializedName("video") val videoId: Int,
    val text: String,
    val user: User,
    @SerializedName("created") val createdDate: Date
)