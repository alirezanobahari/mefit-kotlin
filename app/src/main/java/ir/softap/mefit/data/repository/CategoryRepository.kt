package ir.softap.mefit.data.repository

import io.reactivex.Single
import ir.softap.mefit.data.model.Category
import ir.softap.mefit.data.repository.source.CategoryApi
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryApi: CategoryApi) {

    fun fetchCategories(): Single<List<Category>> = categoryApi.fetchCategories()

}