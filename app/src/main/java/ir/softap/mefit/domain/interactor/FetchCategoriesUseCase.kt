package ir.softap.mefit.domain.interactor

import io.reactivex.Single
import ir.softap.mefit.data.model.Category
import ir.softap.mefit.data.repository.CategoryRepository
import ir.softap.mefit.domain.abstraction.SingleUseCase
import ir.toolgram.toolgramapp.data.executor.ThreadExecutor
import ir.toolgram.toolgramapp.domain.executor.PostExecutionThread
import javax.inject.Inject

class FetchCategoriesUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val categoryRepository: CategoryRepository
) : SingleUseCase<List<Category>, Unit>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: Unit): Single<List<Category>> = categoryRepository.fetchCategories()
}