package ir.softap.mefit.ui.abstraction.dialogfragment

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import ir.softap.mefit.R

abstract class BottomPopupDialogFragment : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.setGravity(Gravity.BOTTOM)
        return dialog
    }

}