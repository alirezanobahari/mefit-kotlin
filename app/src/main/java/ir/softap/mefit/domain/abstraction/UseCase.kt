package ir.softap.mefit.domain.abstraction

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class UseCase<out Response, in Param>(private val coroutineContext: CoroutineContext) {

    abstract suspend fun buildUseCase(param: Param): Deferred<Response>

    suspend fun execute(param: Param): Deferred<Response> = withContext(coroutineContext) {
        return@withContext buildUseCase(param)
    }

}