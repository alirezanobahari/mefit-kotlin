package ir.softap.mefit.utilities.extensions

import android.content.res.Resources

/**
 * extension function for checking [Double] number is negative
 */
fun Double.isNegative() = this < 0

/**
 * Extension function for converting toPx to toDp
 */
val Double.toDp: Double
    get() = (this / Resources.getSystem().displayMetrics.density)

/**
 * Extension function for converting toDp to toPx
 */
val Double.toPx: Double
    get() = (this * Resources.getSystem().displayMetrics.density)