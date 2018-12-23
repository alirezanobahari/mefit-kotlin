package ir.softap.mefit.domain.interactor

import io.reactivex.Single
import ir.softap.mefit.data.model.Home
import ir.softap.mefit.data.repository.VideoRepository
import ir.softap.mefit.domain.abstraction.SingleUseCase
import ir.toolgram.toolgramapp.data.executor.ThreadExecutor
import ir.toolgram.toolgramapp.domain.executor.PostExecutionThread
import javax.inject.Inject

class FetchHomeUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val videoRepository: VideoRepository
) : SingleUseCase<List<Home>, Unit>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: Unit): Single<List<Home>> = videoRepository.fetchHome()
}