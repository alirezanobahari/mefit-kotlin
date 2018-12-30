package ir.softap.mefit.domain.interactor

import io.reactivex.Completable
import ir.softap.mefit.data.executor.ThreadExecutor
import ir.softap.mefit.data.model.request.PostCommentRequest
import ir.softap.mefit.data.repository.UserRepository
import ir.softap.mefit.domain.abstraction.CompletableUseCase
import ir.softap.mefit.domain.executor.PostExecutionThread
import javax.inject.Inject

class CommentUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val userRepository: UserRepository
) : CompletableUseCase<CommentUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Completable =
        userRepository.comment(params.token, params.postCommentRequest)

    data class Params(val token: String, val postCommentRequest: PostCommentRequest)

}