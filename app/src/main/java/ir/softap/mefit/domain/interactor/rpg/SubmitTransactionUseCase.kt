package ir.softap.mefit.domain.interactor.rpg

import io.reactivex.Single
import ir.softap.mefit.data.repository.RpgRepository
import ir.softap.mefit.domain.abstraction.SingleUseCase
import ir.softap.mefit.data.executor.ThreadExecutor
import ir.softap.mefit.domain.executor.PostExecutionThread
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class SubmitTransactionUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val rpgRepository: RpgRepository
) : SingleUseCase<String, SubmitTransactionUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Single<String> =
        rpgRepository.checkSubscription(params.tid, params.pin)

    data class Params(
        val tid: String,
        val pin: String
    )

}