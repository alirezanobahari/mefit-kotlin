package ir.softap.mefit.utilities.extensions

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson

/**
 * Extension method to get the TAG title for all object
 */
fun <T : Any> T.TAG() = this::class.simpleName

/**
 * Extension function for converting any object to json string
 */
fun <T : Any> T.toJson(): String = Gson().toJson(this)

/**
 * Extension function create [T] variable as [MutableLiveData]
 */
@SuppressLint("CheckResult")
fun <T : Any> T.asMutableLiveData(): MutableLiveData<T> {
    val mutableLiveData = MutableLiveData<T>()
    mutableLiveData.value = this
    return mutableLiveData
}
