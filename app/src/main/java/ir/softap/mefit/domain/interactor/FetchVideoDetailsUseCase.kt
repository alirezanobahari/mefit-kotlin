package ir.softap.mefit.domain.interactor

import io.reactivex.Single
import ir.softap.mefit.data.executor.ThreadExecutor
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.data.repository.VideoRepository
import ir.softap.mefit.domain.abstraction.SingleUseCase
import ir.softap.mefit.domain.executor.PostExecutionThread
import javax.inject.Inject

class FetchVideoDetailsUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val videoRepository: VideoRepository
) : SingleUseCase<Video.VideoDetail, FetchVideoDetailsUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Single<Video.VideoDetail> =
        videoRepository.fetchVideoDetail(params.token, params.video)

    data class Params(
        val token: String,
        val video: Int
    )

}