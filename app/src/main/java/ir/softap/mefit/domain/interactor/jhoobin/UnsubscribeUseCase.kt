package ir.softap.mefit.domain.interactor.jhoobin

import io.reactivex.Completable
import ir.softap.mefit.data.executor.ThreadExecutor
import ir.softap.mefit.data.repository.JhoobinRepository
import ir.softap.mefit.domain.abstraction.CompletableUseCase
import ir.softap.mefit.domain.executor.PostExecutionThread
import javax.inject.Inject

class UnsubscribeUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val jhoobinRepository: JhoobinRepository
) : CompletableUseCase<UnsubscribeUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Completable =
        jhoobinRepository.unsubscribe(params.packageName, params.subscriptionId, params.token, params.accessToken)

    data class Params(
        val packageName: String,
        val subscriptionId: String,
        val token: String,
        val accessToken: String
    )

}