package ir.softap.mefit.domain.interactor

import io.reactivex.Single
import ir.softap.mefit.data.executor.ThreadExecutor
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.data.repository.VideoRepository
import ir.softap.mefit.domain.abstraction.SingleUseCase
import ir.softap.mefit.domain.executor.PostExecutionThread
import javax.inject.Inject

class FetchSuggestedVideosUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val videoRepository: VideoRepository
) : SingleUseCase<List<Video>, Int>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: Int): Single<List<Video>> = videoRepository.fetchSuggestions(params)
}