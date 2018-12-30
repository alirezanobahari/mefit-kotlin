package ir.softap.mefit.domain.interactor

import io.reactivex.Single
import ir.softap.mefit.data.executor.ThreadExecutor
import ir.softap.mefit.data.model.Comment
import ir.softap.mefit.data.repository.UserRepository
import ir.softap.mefit.domain.abstraction.SingleUseCase
import ir.softap.mefit.domain.executor.PostExecutionThread
import javax.inject.Inject

class FetchCommentsUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val userRepository: UserRepository
) : SingleUseCase<List<Comment>, Int>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: Int): Single<List<Comment>> = userRepository.fetchComments(params)
}