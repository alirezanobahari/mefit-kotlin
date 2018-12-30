package ir.softap.mefit.domain.interactor

import io.reactivex.Completable
import ir.softap.mefit.data.executor.ThreadExecutor
import ir.softap.mefit.data.model.request.LikeRequest
import ir.softap.mefit.data.repository.UserRepository
import ir.softap.mefit.domain.abstraction.CompletableUseCase
import ir.softap.mefit.domain.executor.PostExecutionThread
import javax.inject.Inject

class LikeVideoUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val userRepository: UserRepository
) : CompletableUseCase<LikeVideoUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Completable =
        userRepository.like(params.token, params.likeRequest)

    data class Params(
        val token: String,
        val likeRequest: LikeRequest
    )

}