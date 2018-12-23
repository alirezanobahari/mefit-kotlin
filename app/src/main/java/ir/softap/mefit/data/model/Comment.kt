package ir.softap.mefit.data.model

import android.os.Parcelable
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
    @Entity
    @Parcelize
    data class User(
        @PrimaryKey(autoGenerate = false) val id: Int,
        @SerializedName("complete_name") @ColumnInfo(name = "completeName") val completeName: String
    ) : Parcelable
}