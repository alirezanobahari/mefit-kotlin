package ir.softap.mefit.domain.interactor.rpg

import io.reactivex.Single
import ir.softap.mefit.data.repository.RpgRepository
import ir.softap.mefit.domain.abstraction.SingleUseCase
import ir.softap.mefit.data.executor.ThreadExecutor
import ir.softap.mefit.domain.executor.PostExecutionThread
import retrofit2.Response
import javax.inject.Inject

class CheckMemberStatusUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val rpgRepository: RpgRepository
) : SingleUseCase<String, CheckMemberStatusUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Single<String> =
        rpgRepository.memberStatus(params.sid, params.mobile)

    data class Params(
        val sid: String,
        val mobile: String
    )
}