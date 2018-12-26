package ir.softap.mefit.domain.interactor

import io.reactivex.Single
import ir.softap.mefit.data.model.Session
import ir.softap.mefit.data.model.request.LoginRequest
import ir.softap.mefit.data.repository.UserRepository
import ir.softap.mefit.domain.abstraction.SingleUseCase
import ir.softap.mefit.data.executor.ThreadExecutor
import ir.softap.mefit.domain.executor.PostExecutionThread
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val userRepository: UserRepository
) : SingleUseCase<Session, LoginRequest>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: LoginRequest): Single<Session> =
        userRepository.login(params)
}