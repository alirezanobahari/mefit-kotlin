package ir.softap.mefit.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import ir.softap.mefit.data.model.Comment
import ir.softap.mefit.data.model.Session
import ir.softap.mefit.data.model.User
import ir.softap.mefit.data.model.request.EditProfileRequest
import ir.softap.mefit.data.model.request.LikeRequest
import ir.softap.mefit.data.model.request.LoginRequest
import ir.softap.mefit.data.model.request.PostCommentRequest
import ir.softap.mefit.data.repository.source.UserApi
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    fun login(loginRequest: LoginRequest): Single<Session> = userApi.login(loginRequest)

    fun editProfile(
        token: String,
        editProfileRequest: EditProfileRequest
    ): Single<User> = userApi.editProfile(token, editProfileRequest)

    fun like(
        token: String,
        like: LikeRequest
    ): Completable = userApi.like(token, like)

    fun comment(
        token: String,
        postCommentRequest: PostCommentRequest
    ): Completable = userApi.comment(token, postCommentRequest)

    fun fetchComments(videoId: Int): Single<List<Comment>> = userApi.fetchComments(videoId)

    fun fetchUserInfo(
        token: String,
        catSlug: String
    ): Single<User> = userApi.fetchUserInfo(token, catSlug)

}