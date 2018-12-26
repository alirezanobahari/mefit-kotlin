package ir.softap.mefit.data.model.request

import com.google.gson.annotations.SerializedName

data class EditProfileRequest(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val email: String
)