package ir.softap.mefit.utilities

import android.os.Build
import android.os.Bundle
import ir.softap.mefit.BuildConfig

/**
 * run [body] if value of [BuildConfig.DEBUG] is true
 */
inline fun onDebug(body: () -> Unit) {
    if (BuildConfig.DEBUG) body()
}

/**
 * run [body] if value of [BuildConfig.DEBUG] is false
 */
inline fun onRelease(body: () -> Unit) {
    if (BuildConfig.DEBUG.not()) body()
}

/**
 * run [body] if [Build.VERSION.SDK_INT] is equal or grater than [Build.VERSION_CODES.LOLLIPOP]
 */

inline fun lollipopAndAbove(body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        body()
}

/**
 * run [body] if [Build.VERSION.SDK_INT] is lower than [Build.VERSION_CODES.LOLLIPOP]
 */
inline fun belowLollipop(body: () -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        body()
}

/**
 * run [body] if [Build.VERSION.SDK_INT] is equal or grater than [apiLevel]
 */
inline fun onApi(apiLevel: Int, body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= apiLevel)
        body()
}

/**
 * run [body] if [Build.VERSION.SDK_INT] is between [startApiLevel] and [endApiLevel] with [inclusive] option
 */
inline fun betweenApis(
    startApiLevel: Int,
    endApiLevel: Int,
    inclusive: Boolean = true,
    body: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= startApiLevel &&
        if (inclusive) (Build.VERSION.SDK_INT <= endApiLevel) else (Build.VERSION.SDK_INT < endApiLevel)
    )
        body()
}

/**
 * run [body] if [savedInstanceState] is null
 */
inline fun onNoInstanceState(savedInstanceState: Bundle?, body: () -> Unit) {
    if (savedInstanceState == null)
        body()
}