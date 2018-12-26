package ir.softap.mefit.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class Comment(
    @SerializedName("video") @ColumnInfo(name = "videoId") val videoId: Int,
    @ColumnInfo(name = "text") val text: String,
    @Embedded val user: User,
    @SerializedName("created") @ColumnInfo(name = "createdDate") val createdDate: Date
) : Parcelable {
    companion object {
        val COMMENT_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem.user.id == newItem.user.id && oldItem.createdDate == newItem.createdDate

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem == newItem
        }
    }

    @Entity
    @Parcelize
    data class User(
        @PrimaryKey(autoGenerate = false) val id: Int,
        @SerializedName("complete_name") @ColumnInfo(name = "completeName") val completeName: String
    ) : Parcelable
}