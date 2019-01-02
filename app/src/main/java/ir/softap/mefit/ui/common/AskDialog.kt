package ir.softap.mefit.ui.common

import android.os.Bundle
import android.view.View
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.dialogfragment.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_ask.*

class AskDialog : BaseDialogFragment() {

    override fun getTheme(): Int  = R.style.AppDialog_Fullscreen

    companion object {

        private const val EXTRA_MESSAGE = "ir.softap.mefit.ui.common.EXTRA_MESSAGE"

        fun newInstance(message: String, confirm: () -> Unit, cancel: (() -> Unit)? = null) = AskDialog().apply {
            this.confirm = confirm
            this.cancel = cancel
            arguments = Bundle().apply {
                putString(EXTRA_MESSAGE, message)
            }
        }
    }

    private lateinit var confirm: () -> Unit
    private var cancel: (() -> Unit)? = null

    private val message: String by lazy {
        arguments?.getString(EXTRA_MESSAGE) ?: ""
    }

    override val layoutRes: Int = R.layout.dialog_ask

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->

        tvMessage.text = message

        btnConfirm.setOnClickListener {
            confirm()
            dismiss()
        }
        btnCancel.setOnClickListener {
            cancel?.invoke()
            dismiss()
        }

    }


}