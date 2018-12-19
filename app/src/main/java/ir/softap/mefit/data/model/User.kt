package ir.softap.mefit.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val email: String,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("is_premium") val isPremium: Boolean
)