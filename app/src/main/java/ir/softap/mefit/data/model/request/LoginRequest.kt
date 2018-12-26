package ir.softap.mefit.data.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    @SerializedName("cat_slug") val catSlug: String,
    @SerializedName("is_premium") val isPremium: Boolean
)