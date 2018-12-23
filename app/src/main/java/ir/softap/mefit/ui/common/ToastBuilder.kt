package ir.softap.mefit.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.widget.Toast
import ir.softap.mefit.R
import ir.softap.mefit.utilities.extensions.toDp
import kotlinx.android.synthetic.main.layout_toast.view.*


class ToastBuilder private constructor(val context: Context) {

    enum class Type {
        PRIMARY, SECONDARY, SUCCESS, DANGER, WARNING, INFO, LIGHT, DARK
    }

    private object ColorRes {

        //primary
        var primary_background_color = Color.parseColor("#cce5ff")
        var primary_text_color = Color.parseColor("#004085")
        var primary_border_color = Color.parseColor("#b8daff")

        //secondary
        var secondary_background_color = Color.parseColor("#e2e3e5")
        var secondary_text_color = Color.parseColor("#383d41")
        var secondary_border_color = Color.parseColor("#d6d8db")

        //success
        var success_background_color = Color.parseColor("#d4edda")
        var success_text_color = Color.parseColor("#155724")
        var success_border_color = Color.parseColor("#c3e6cb")

        //danger
        var danger_background_color = Color.parseColor("#f8d7da")
        var danger_text_color = Color.parseColor("#721c24")
        var danger_border_color = Color.parseColor("#f5c6cb")

        //warning
        var warning_background_color = Color.parseColor("#fff3cd")
        var warning_text_color = Color.parseColor("#856404")
        var warning_border_color = Color.parseColor("#ffeeba")

        //info
        var info_background_color = Color.parseColor("#d1ecf1")
        var info_text_color = Color.parseColor("#0c5460")
        var info_border_color = Color.parseColor("#bee5eb")

        //light
        var light_background_color = Color.parseColor("#fefefe")
        var light_text_color = Color.parseColor("#818182")
        var light_border_color = Color.parseColor("#ebebec")

        //dark
        var dark_background_color = Color.parseColor("#d6d8d9")
        var dark_text_color = Color.parseColor("#1b1e21")
        var dark_border_color = Color.parseColor("#c6c8ca")

    }

    companion object {

        fun with(context: Context) = ToastBuilder(context)

        fun showSuccess(context: Context, message: String) =
            ToastBuilder(context)
                .type(Type.SUCCESS)
                .message(message)
                .show()

        fun showError(context: Context, message: String) =
            ToastBuilder(context)
                .type(Type.DANGER)
                .message(message)
                .show()

        fun showWarning(context: Context, message: String) =
            ToastBuilder(context)
                .type(Type.WARNING)
                .message(message)
                .show()

        fun showInfo(context: Context, message: String) =
            ToastBuilder(context)
                .type(Type.INFO)
                .message(message)
                .show()

    }

    private val toast: Toast = Toast(context)
    private var type = Type.INFO
    private var message = ""
    private var duration = Toast.LENGTH_SHORT

    fun type(type: Type): ToastBuilder {
        this.type = type
        return this
    }

    fun message(message: String): ToastBuilder {
        this.message = message
        return this
    }

    @SuppressLint("InflateParams")
    fun show() {
        toast.duration = duration
        with(LayoutInflater.from(context).inflate(R.layout.layout_toast, null, false)) {
            toast.view = this
            tvToastMessage.text = message
            val toastBackground = tvToastMessage.background as GradientDrawable
            when (type) {
                Type.PRIMARY -> {
                    toastBackground.setColor(ColorRes.primary_background_color)
                    toastBackground.setStroke(1.toDp, ColorRes.primary_border_color)
                    tvToastMessage.setTextColor(ColorRes.primary_text_color)
                }
                Type.SECONDARY -> {
                    toastBackground.setColor(ColorRes.secondary_background_color)
                    toastBackground.setStroke(1.toDp, ColorRes.secondary_border_color)
                    tvToastMessage.setTextColor(ColorRes.secondary_text_color)
                }
                Type.SUCCESS -> {
                    toastBackground.setColor(ColorRes.success_background_color)
                    toastBackground.setStroke(1.toDp, ColorRes.success_border_color)
                    tvToastMessage.setTextColor(ColorRes.success_text_color)
                }
                Type.DANGER -> {
                    toastBackground.setColor(ColorRes.danger_background_color)
                    toastBackground.setStroke(1.toDp, ColorRes.danger_border_color)
                    tvToastMessage.setTextColor(ColorRes.danger_text_color)
                }
                Type.WARNING -> {
                    toastBackground.setColor(ColorRes.warning_background_color)
                    toastBackground.setStroke(1.toDp, ColorRes.warning_border_color)
                    tvToastMessage.setTextColor(ColorRes.warning_text_color)
                }
                Type.INFO -> {
                    toastBackground.setColor(ColorRes.info_background_color)
                    toastBackground.setStroke(1.toDp, ColorRes.info_border_color)
                    tvToastMessage.setTextColor(ColorRes.info_text_color)
                }
                Type.LIGHT -> {
                    toastBackground.setColor(ColorRes.light_background_color)
                    toastBackground.setStroke(1.toDp, ColorRes.light_border_color)
                    tvToastMessage.setTextColor(ColorRes.light_text_color)
                }
                Type.DARK -> {
                    toastBackground.setColor(ColorRes.dark_background_color)
                    toastBackground.setStroke(1.toDp, ColorRes.dark_border_color)
                    tvToastMessage.setTextColor(ColorRes.dark_text_color)
                }
            }
        }
        toast.show()
    }

}