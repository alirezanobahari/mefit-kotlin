package ir.softap.mefit.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.android.billingclient.util.IabHelper
import com.android.billingclient.util.MarketIntentFactorySDK
import com.etiennelenhart.eiffel.state.peek
import ir.softap.mefit.R
import ir.softap.mefit.SessionManager
import ir.softap.mefit.data.model.Session
import ir.softap.mefit.ui.abstraction.DaggerXFragmentActivity
import ir.softap.mefit.ui.common.ToastBuilder
import ir.softap.mefit.ui.common.UIState
import ir.softap.mefit.ui.common.core.CHARKHOONE_RSA_KEY
import ir.softap.mefit.ui.common.core.KEY_USER_PHONE_NUMBER
import ir.softap.mefit.ui.common.core.MEFIT_SUBSCRIPTION_SKU
import ir.softap.mefit.ui.main.MainActivity
import ir.softap.mefit.utilities.LocalStorage
import ir.softap.mefit.utilities.d
import ir.softap.mefit.utilities.e
import ir.softap.mefit.utilities.extensions.TAG
import ir.softap.mefit.utilities.extensions.strings
import ir.softap.mefit.utilities.onApiAndAbove
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : DaggerXFragmentActivity() {

    private val REQUEST_PURCHASE = 100

    private var enterPidDialog: EnterPidDialog? = null

    private val iabHelper: IabHelper by lazy {
        IabHelper(this, CHARKHOONE_RSA_KEY, MarketIntentFactorySDK(true))
    }

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.activity_login

    override val initViews: (Bundle?) -> Unit = {

        onApiAndAbove(23) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_SMS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_SMS), 100
                )
            }
        }

        iabHelper.startSetup { iabResult -> d { "${TAG()}, setup result: ${iabResult.isSuccess}" } }

        btnEnter.setOnClickListener {
            LocalStorage.with(this).save(KEY_USER_PHONE_NUMBER, edPhoneNumber.text.toString())
            loginViewModel.login(edPhoneNumber.text.toString())
            iabHelper.setFillInIntent(Intent().apply {
                putExtra("msisdn", edPhoneNumber.text.toString())
                putExtra("editAble", true)
            })
        }

        loginViewModel.observeState(this) { loginViewState ->

            val loading = loginViewState.loginState == UIState.LOADING
            edPhoneNumber.isEnabled = !loading
            if (loading) btnEnter.startAnimation() else btnEnter.revertAnimation()

            loginViewState.loginViewEvent?.peek { loginViewEvent ->
                when (loginViewEvent) {
                    is LoginViewEvent.ValidationStateEvent -> {
                        when (loginViewEvent.validationState) {
                            LoginViewModel.ValidationState.EMPTY_PHONE_NUMBER -> edPhoneNumber.error =
                                    strings[R.string.validation_empty_phone_number]
                            LoginViewModel.ValidationState.INVALID_PHONE_NUMBER -> edPhoneNumber.error =
                                    strings[R.string.validation_wrong_phone_number]
                            LoginViewModel.ValidationState.OK -> d { "${TAG()}, validation ok!" }
                        }
                        true
                    }
                    is LoginViewEvent.ShowEnterPinDialog -> {
                        enterPidDialog = EnterPidDialog.newInstance(loginViewEvent.tid).apply {
                            show(supportFragmentManager, "enterPidDialog")
                        }
                        true
                    }
                    is LoginViewEvent.IrancellPurchaseEvent -> {
                        checkIrancellPremium()
                        true
                    }
                    is LoginViewEvent.LoginSuccessEvent -> {
                        LocalStorage.with(this@LoginActivity)
                            .save(Session::class.java.name, SessionManager.session!!)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java)).also {
                            finish()
                        }
                        true
                    }
                    is LoginViewEvent.ErrorViewEvent -> {
                        ToastBuilder.showError(this@LoginActivity, strings[loginViewEvent.errorMessage])
                        true
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        iabHelper.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (enterPidDialog?.onBackPressed() == true)
            return
        super.onBackPressed()
    }

    private fun checkIrancellPremium() {
        iabHelper.queryInventoryAsync { iabResult, purchase ->
            if (iabResult.isSuccess) {
                if (purchase.hasPurchase(MEFIT_SUBSCRIPTION_SKU)) {
                    LocalStorage.with(this@LoginActivity)
                        .save(Session::class.java.name, SessionManager.session!!)
                    purchaseSucceed()
                } else
                    irancellPurchase()
            } else
                irancellPurchase()
        }
    }

    private fun irancellPurchase() {
        try {
            iabHelper.launchPurchaseFlow(
                this@LoginActivity,
                MEFIT_SUBSCRIPTION_SKU,
                REQUEST_PURCHASE
            ) { iabResult, _ ->
                if (iabResult.isSuccess) {
                    LocalStorage.with(this@LoginActivity)
                        .save(Session::class.java.name, SessionManager.session!!)
                    purchaseSucceed()
                }
            }
        } catch (iabException: IabHelper.IabAsyncInProgressException) {
            ToastBuilder.showError(this, strings[R.string.msg_error_occurred])
            e { "${TAG()}, $iabException" }
        }
    }

    private fun purchaseSucceed() {

        startActivity(Intent(this, MainActivity::class.java)).also { finish() }
    }
}