package ir.softap.mefit.data.repository.source

import io.reactivex.Single
import ir.softap.mefit.data.model.Category
import ir.softap.mefit.data.network.CAT_SLUG
import retrofit2.http.GET

interface CategoryApi {

    @GET("$CAT_SLUG/categories")
    fun fetchCategories(): Single<List<Category>>

}