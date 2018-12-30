package ir.softap.mefit.domain.interactor

import io.reactivex.Single
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.data.model.request.query.FilterVideoQuery
import ir.softap.mefit.data.repository.VideoRepository
import ir.softap.mefit.domain.abstraction.SingleUseCase
import ir.softap.mefit.data.executor.ThreadExecutor
import ir.softap.mefit.domain.executor.PostExecutionThread
import javax.inject.Inject

class FilterVideoUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val videoRepository: VideoRepository
) : SingleUseCase<List<Video>, FilterVideoQuery>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: FilterVideoQuery): Single<List<Video>> =
        videoRepository.filterVideos(params)
}