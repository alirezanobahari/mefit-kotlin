package ir.softap.mefit.utilities.extensions

/**
 * Various extension functions for AppCompatActivity.
 */

import android.content.Context
import android.content.Intent
import android.text.TextUtils.replace
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction


/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragment(fragment: Fragment, @IdRes frameId: Int, allowStateLoss: Boolean = true) {
    supportFragmentManager.transaction(allowStateLoss = allowStateLoss) {
        replace(frameId, fragment)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragment(fragment: Fragment, tag: String, allowStateLoss: Boolean = true) {
    supportFragmentManager.transaction(allowStateLoss = allowStateLoss) {
        add(fragment, tag)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragment(fragment: Fragment, @IdRes frameId: Int, allowStateLoss: Boolean = true) {
    supportFragmentManager.transaction(allowStateLoss = allowStateLoss) {
        add(frameId, fragment)
    }
}


/**
 * Extension method to provide hide keyboard for [AppCompatActivity].
 */
fun AppCompatActivity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(Context
                .INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

/**
 * Run [body] if [AppCompatActivity] was deep linked
 */
fun AppCompatActivity.doOnDeepLink(body: () -> Unit) {
    if (intent.action == Intent.ACTION_VIEW)
        body()
}

/**
 * Run [body] if [AppCompatActivity] was deep linked
 */
fun AppCompatActivity.doOnDeepLink(intent: Intent?, body: () -> Unit) {
    if (intent?.action == Intent.ACTION_VIEW)
        body()
}

/**
 * Run [body] if [AppCompatActivity] was regular call
 */
fun AppCompatActivity.doOnRegularCall(body: () -> Unit) {
    if (intent.action != Intent.ACTION_VIEW)
        body()
}
