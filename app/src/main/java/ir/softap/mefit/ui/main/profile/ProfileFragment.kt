package ir.softap.mefit.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.etiennelenhart.eiffel.state.peek
import ir.softap.mefit.R
import ir.softap.mefit.SessionManager
import ir.softap.mefit.data.model.Session
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import ir.softap.mefit.ui.common.AskDialog
import ir.softap.mefit.ui.common.ToastBuilder
import ir.softap.mefit.ui.common.core.KEY_USER_PHONE_NUMBER
import ir.softap.mefit.ui.login.LoginActivity
import ir.softap.mefit.ui.main.MainViewModel
import ir.softap.mefit.utilities.LocalStorage
import ir.softap.mefit.utilities.MobileNumberHelper
import ir.softap.mefit.utilities.d
import ir.softap.mefit.utilities.extensions.TAG
import ir.softap.mefit.utilities.extensions.strings
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : DaggerXFragment() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[MainViewModel::class.java]
    }

    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.fragment_profile

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->

        //Hide unsubscribe button if user phone number operator is Hamrah Aval
        if (MobileNumberHelper.getOperator(
                LocalStorage.with(context!!).load(KEY_USER_PHONE_NUMBER) ?: ""
            ) == MobileNumberHelper.Operator.HAMRAH_AVAL
        ) {
            btnUnsubscribe.visibility = View.GONE
        }

        btnSaveChanges.setOnClickListener {
            profileViewModel.editProfile(
                edFirstName.text.toString(),
                edLastName.text.toString(),
                edEmail.text.toString()
            )
        }
        btnUnsubscribe.setOnClickListener {
            AskDialog.newInstance(
                context!!.strings[R.string.msg_want_to_unsubscribe],
                confirm = { mainViewModel.unsubscribe() }
            ).show(childFragmentManager, "unsubscribeDialog")
        }
        btnLogout.setOnClickListener {
            AskDialog.newInstance(
                context!!.strings[R.string.msg_want_to_logout],
                confirm = {
                    SessionManager.session = null
                    LocalStorage.with(context!!).delete(Session::class.java.name)
                    startActivity(Intent(context!!, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
            ).show(childFragmentManager, "logoutDialog")
        }
       /* SessionManager.session?.user?.also { user ->
            edFirstName.setText(user.firstName)
            edLastName.setText(user.lastName)
            edEmail.setText(user.email)
        }*/

        profileViewModel.observeState(this) { profileViewState ->
            profileViewState.profileViewEvent?.peek { profileViewEvent ->
                when (profileViewEvent) {
                    is ProfileViewEvent.ValidationStateEvent -> {
                        when (profileViewEvent.validationState) {
                            ValidationState.EMPTY_FIRST_NAME -> edFirstName.error =
                                    context!!.strings[R.string.validation_empty_first_name]
                            ValidationState.EMPTY_LAST_NAME -> edFirstName.error =
                                    context!!.strings[R.string.validation_empty_last_name]
                            ValidationState.EMPTY_EMAIL -> edFirstName.error =
                                    context!!.strings[R.string.validation_empty_email]
                            ValidationState.INVALID_EMAIL -> edFirstName.error =
                                    context!!.strings[R.string.validation_invalid_email]
                            ValidationState.OK -> d { "${TAG()}, validation successful." }
                        }
                        true
                    }
                    is ProfileViewEvent.EditProfileSuccessfulEvent -> {
                        ToastBuilder.showSuccess(
                            context!!,
                            context!!.strings[R.string.msg_profile_edited_successfully]
                        )
                        true
                    }
                    is ProfileViewEvent.ErrorViewEvent -> {
                        ToastBuilder.showSuccess(
                            context!!,
                            context!!.strings[profileViewEvent.message]
                        )
                        true
                    }
                }
            }
        }
    }
}