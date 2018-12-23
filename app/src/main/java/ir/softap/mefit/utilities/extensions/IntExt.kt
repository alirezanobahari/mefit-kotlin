package ir.softap.mefit.utilities.extensions

import android.content.res.Resources

/**
 * Extension function for converting toPx to toDp
 */
val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

/**
 * Extension function for converting toDp to toPx
 */
val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()