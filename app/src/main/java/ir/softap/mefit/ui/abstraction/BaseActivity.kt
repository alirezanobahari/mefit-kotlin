package ir.softap.mefit.ui.abstraction

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ir.softap.mefit.ui.splash.SplashActivity
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.hideSoftKeyboard
import ir.softap.mefit.utilities.extensions.onMobile
import ir.softap.mefit.utilities.extensions.onTablet
import ir.softap.mefit.utilities.onRelease


abstract class BaseActivity : AppCompatActivity() {

    var enableDismissKeyboard = true

    abstract val layoutRes: Int

    abstract val initViews: (Bundle?) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        initViews(savedInstanceState)
        initOrientation()
        onRelease {
            globalExceptionHandler()
        }
    }

    /**
     * handle all exception that occurs in activities witch extends from [BaseActivity]
     * then restart application
     */
    private fun globalExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            startActivity(Intent(this, SplashActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            e { "$paramThrowable" }
        }
    }

    /**
     * set orientation of screen in tablet and mobile
     */
    private fun initOrientation() {
        onTablet {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
        }
        onMobile {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    /**
     * hide soft keyboard when touch outside of [EditText]
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val view = currentFocus
        val ret = super.dispatchTouchEvent(event)

        if (view is EditText && enableDismissKeyboard) {
            val w = currentFocus
            val scrcoords = IntArray(2)
            w!!.getLocationOnScreen(scrcoords)
            val x = event.rawX + w.left - scrcoords[0]
            val y = event.rawY + w.top - scrcoords[1]

            if (event.action == MotionEvent.ACTION_UP && (x < w.left || x >= w.right
                        || y < w.top || y > w.bottom)
            ) {
                hideSoftKeyboard()
            }
        }
        return ret
    }

}