package ir.softap.mefit.domain.executor

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import ir.softap.mefit.domain.executor.PostExecutionThread
import javax.inject.Inject

class UiThread @Inject internal constructor() : PostExecutionThread {

    override val scheduler: Scheduler
        get() = AndroidSchedulers.mainThread()

}