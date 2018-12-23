package ir.softap.mefit.utilities.extensions

import android.text.Html
import com.google.gson.Gson
import java.lang.reflect.Type


/**
 * Extension function for converting json string to given class type
 */
fun <T> String.fromJson(clazz: Class<T>) = Gson().fromJson(this, clazz)

/**
 * Extension function for converting json string to given type
 */
fun <T> String.fromJson(type: Type): T = Gson().fromJson(this, type)

/**
 * Extension function for converting html string to spannable
 */
fun String.fromHtml() = Html.fromHtml(this)
