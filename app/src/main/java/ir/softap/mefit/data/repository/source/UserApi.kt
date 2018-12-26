package ir.softap.mefit.data.repository.source

import io.reactivex.Completable
import io.reactivex.Single
import ir.softap.mefit.data.model.Comment
import ir.softap.mefit.data.model.Session
import ir.softap.mefit.data.model.User
import ir.softap.mefit.data.model.request.EditProfileRequest
import ir.softap.mefit.data.model.request.LikeRequest
import ir.softap.mefit.data.model.request.LoginRequest
import ir.softap.mefit.data.model.request.PostCommentRequest
import ir.softap.mefit.data.network.AUTH
import retrofit2.http.*

interface UserApi {

    @POST("user/v2/login")
    fun login(@Body loginRequest: LoginRequest): Single<Session>

    @PUT("user/update")
    fun editProfile(
        @Header(AUTH) token: String,
        @Body editProfileRequest: EditProfileRequest
    ): Single<User>

    @POST("user/like")
    fun like(
        @Header(AUTH) token: String,
        @Body like: LikeRequest
    ): Completable

    @PUT("user/comment/")
    fun comment(
        @Header(AUTH) token: String,
        @Body postCommentRequest: PostCommentRequest
    ): Completable

    @GET("user/comment")
    fun fetchComments(@Query("video") videoId: Int): Single<List<Comment>>

    @GET("user/info")
    fun fetchUserInfo(
        @Header(AUTH) token: String,
        @Query("cat_slug") catSlug: String
    ): Single<User>

}