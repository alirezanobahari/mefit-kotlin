package ir.softap.mefit.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
    val id: Int,
    val title: String,
    val visits: Int,
    val thumbnail: String,
    val category: Category,
    val issuer: Issuer,
    val tags: List<Tag>,
    val like: Like,
    val duration: String,
    val premium: Boolean
) : Parcelable {

    @Parcelize
    data class VideoDetail(
        val url: Url,
        @SerializedName("comments_count") val commentsCount: Int,
        @SerializedName("desc") val descriptions: List<Description>?,
        val category: Category,
        val likes: Like,
        val rate: Rate,
        @SerializedName("premium_desc") val premiumDescription: PremiumDescription?
    ) : Parcelable {

        @Parcelize
        data class Url(
            @SerializedName("url_480") val url480: String?,
            @SerializedName("url_720") val url720: String?
        ) : Parcelable

        @Parcelize
        data class Description(
            val title: String,
            @SerializedName("sub") val subDescriptions: List<SubDescription>
        ) : Parcelable {

            @Parcelize
            data class SubDescription(
                val title: String,
                val text: String
            ) : Parcelable

        }

        @Parcelize
        data class PremiumDescription(
            @SerializedName("is_present") val isPresent: Boolean,
            @SerializedName("content") val descriptions: List<Description>?
        ) : Parcelable

    }

    @Parcelize
    data class Issuer(
        val id: String,
        @SerializedName("first_name") val firstName: String?,
        @SerializedName("last_name") val lastName: String?
    ) : Parcelable

    @Parcelize
    data class Tag(
        val id: Int,
        val title: String
    ) : Parcelable

    @Parcelize
    data class Like(
        val count: Int,
        @SerializedName("user_liked") val isUserLiked: Boolean
    ) : Parcelable

    @Parcelize
    data class Rate(
        @SerializedName("user_rate") val userRate: Int,
        val average: Int,
        val total: Int
    ) : Parcelable

}

val VIDEO_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
        oldItem == newItem
}

val TAG_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Video.Tag>() {
    override fun areItemsTheSame(oldItem: Video.Tag, newItem: Video.Tag): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Video.Tag, newItem: Video.Tag): Boolean =
        oldItem == newItem
}