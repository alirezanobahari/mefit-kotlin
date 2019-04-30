package ir.softap.mefit.ui.login

import android.app.Dialog
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import ir.softap.mefit.R
import ir.softap.mefit.R.id.btnSubmit
import ir.softap.mefit.R.id.edPid
import ir.softap.mefit.receiver.SmsBroadcastReceiver
import ir.softap.mefit.ui.abstraction.dialogfragment.DaggerXDialogFragment
import ir.softap.mefit.ui.common.UIState
import ir.softap.mefit.utilities.extensions.onChange
import ir.softap.mefit.utilities.extensions.strings
import kotlinx.android.synthetic.main.dialog_enter_pid.*

class EnterPidDialog : DaggerXDialogFragment() {

    companion object {
        private const val PID_LENGTH = 4
        private const val EXTRA_TID = "ir.softap.mefit.ui.login.EXTRA_TID"
        fun newInstance(tid: String) = EnterPidDialog().apply {
            arguments = Bundle().apply {
                putString(EXTRA_TID, tid)
            }
        }
    }

    private val smsBroadcastReceiver = SmsBroadcastReceiver { pid ->
        edPid.setText(pid)
    }

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[LoginViewModel::class.java]
    }

    private val tid: String by lazy {
        arguments?.getString(EXTRA_TID, "") ?: ""
    }

    init {
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.apply {
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return dialog
    }

    override val layoutRes: Int = R.layout.dialog_enter_pid

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->

        context?.registerReceiver(smsBroadcastReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))

        edPid.onChange { text ->
            if (text.length == PID_LENGTH) {
                loginViewModel.submitTransaction(tid, text)
            }
        }
        btnSubmit.setOnClickListener {
            if (validatePid(edPid.text.toString()))
                loginViewModel.submitTransaction(tid, edPid.text.toString())
        }

        loginViewModel.observeState(this) { loginViewState ->
            val loading = loginViewState.pidState == UIState.LOADING
            edPid.isEnabled = !loading
            if (loading) btnSubmit.startAnimation() else btnSubmit.revertAnimation()

            if (loginViewState.pidState == UIState.SUCCESS)
                dismiss()
        }
    }

    private fun validatePid(pid: String): Boolean {
        if (pid.isEmpty()) {
            edPid.error = context!!.strings[R.string.validation_empty_pid]
            return false
        }
        if (pid.length < PID_LENGTH) {
            edPid.error = context!!.strings[R.string.validation_invalid_pid]
            return false
        }
        return true
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun onDestroy() {
        context?.unregisterReceiver(smsBroadcastReceiver)
        super.onDestroy()
    }

}