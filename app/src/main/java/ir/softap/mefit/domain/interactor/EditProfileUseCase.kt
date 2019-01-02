package ir.softap.mefit.domain.interactor

import io.reactivex.Single
import ir.softap.mefit.data.executor.ThreadExecutor
import ir.softap.mefit.data.model.User
import ir.softap.mefit.data.model.request.EditProfileRequest
import ir.softap.mefit.data.repository.UserRepository
import ir.softap.mefit.domain.abstraction.SingleUseCase
import ir.softap.mefit.domain.executor.PostExecutionThread
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val userRepository: UserRepository
) : SingleUseCase<User, EditProfileUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Single<User> =
        userRepository.editProfile(params.token, params.editProfileRequest)

    data class Params(
        val token: String,
        val editProfileRequest: EditProfileRequest
    )

}