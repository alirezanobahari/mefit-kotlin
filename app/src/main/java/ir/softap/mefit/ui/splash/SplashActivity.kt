package ir.softap.mefit.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.android.billingclient.util.IabHelper
import com.etiennelenhart.eiffel.state.peek
import ir.softap.mefit.R
import ir.softap.mefit.SessionManager
import ir.softap.mefit.data.model.Session
import ir.softap.mefit.ui.abstraction.DaggerXActivity
import ir.softap.mefit.ui.common.core.CHARKHOONE_RSA_KEY
import ir.softap.mefit.ui.common.core.KEY_USER_PHONE_NUMBER
import ir.softap.mefit.ui.common.core.MEFIT_SUBSCRIPTION_SKU
import ir.softap.mefit.ui.intro.IntroActivity
import ir.softap.mefit.ui.main.MainActivity
import ir.softap.mefit.utilities.LocalStorage
import ir.softap.mefit.utilities.MobileNumberHelper
import ir.softap.mefit.utilities.d
import ir.softap.mefit.utilities.extensions.TAG
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : DaggerXActivity() {

    private val iabHelper: IabHelper by lazy {
        IabHelper(applicationContext, CHARKHOONE_RSA_KEY)
    }

    private val splashViewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SplashViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.activity_splash

    override val initViews: (Bundle?) -> Unit = {

        iabHelper.startSetup { }

        btnRetry.setOnClickListener { splashViewModel.retry() }

        val session = LocalStorage.with(this)
            .load(Session::class.java.name, Session::class.java)

        if (session == null) {
            startIntro()
        } else {
            SessionManager.session = session
            val userPhoneNumber = LocalStorage.with(this).load(KEY_USER_PHONE_NUMBER) ?: ""
            val operator = MobileNumberHelper.getOperator(userPhoneNumber)
            splashViewModel.checkMemberStatus(userPhoneNumber, operator)
        }

        splashViewModel.observeState(this) { splashViewState ->

            splashViewState.splashViewEvent?.peek { splashViewEvent ->
                when (splashViewEvent) {
                    is SplashViewEvent.MemberNotFoundEvent -> {
                        when (splashViewEvent.operator) {
                            MobileNumberHelper.Operator.IRANCELL -> {
                                iabHelper.queryInventoryAsync { iabResult, inventory ->
                                    if (iabResult.isFailure) {
                                        startIntro()
                                        return@queryInventoryAsync
                                    }
                                    if (!inventory.hasPurchase(MEFIT_SUBSCRIPTION_SKU)) {
                                        startIntro()
                                        return@queryInventoryAsync
                                    }
                                    startMain()
                                }

                            }
                            MobileNumberHelper.Operator.HAMRAH_AVAL -> {
                                startIntro()
                            }
                            MobileNumberHelper.Operator.UNDEFINED -> d { "${TAG()} operator : ${splashViewEvent.operator}" }
                        }
                        true
                    }
                    is SplashViewEvent.MemberSuccessfulCheckEvent -> {
                        startMain()
                        true
                    }
                }
            }

        }

    }

    private fun startIntro() {
        SessionManager.session = null
        LocalStorage.with(this).apply {
            delete(Session::class.java.name)
            delete(KEY_USER_PHONE_NUMBER)
        }
        startActivity(
            Intent(
                this@SplashActivity,
                IntroActivity::class.java
            )
        ).also {
            finish()
        }
    }

    private fun startMain() {
        startActivity(
            Intent(
                this@SplashActivity,
                MainActivity::class.java
            )
        ).also {
            finish()
        }
    }


}