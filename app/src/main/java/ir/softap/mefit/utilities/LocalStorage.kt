package ir.softap.mefit.utilities

import android.content.Context
import androidx.core.content.edit
import ir.softap.mefit.utilities.extensions.fromJson
import ir.softap.mefit.utilities.extensions.toJson

class LocalStorage(private val context: Context) {

    companion object {
        fun with(context: Context) = LocalStorage(context)
    }

    @Synchronized
    fun <T : Any> save(key: String, t: T) {
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).edit {
            putString(key, if (t is String) t else t.toJson())
            apply()
        }
    }

    @Synchronized
    fun delete(key: String) {
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).edit {
            putString(key, null)
            apply()
        }
    }

    fun load(key: String): String? =
        context.getSharedPreferences(
            context.packageName,
            Context.MODE_PRIVATE
        ).getString(
            key,
            ""
        )

    fun <T> load(key: String, clazz: Class<T>): T? =
        load(key)?.fromJson(clazz)

}