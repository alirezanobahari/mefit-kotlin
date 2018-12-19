package ir.softap.mefit.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val id: Int,
    val title: String,
    val thumbnail: String
) : Parcelable