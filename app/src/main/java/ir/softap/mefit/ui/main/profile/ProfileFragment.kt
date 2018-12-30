package ir.softap.mefit.ui.main.profile

import android.os.Bundle
import android.view.View
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : DaggerXFragment() {

    override val layoutRes: Int = R.layout.fragment_profile

    override val initViews: (View, Bundle?) -> Unit = { _, _ ->

        btnSaveChanges.setOnClickListener {  }
        btnUnsubscribe.setOnClickListener {  }
        btnLogout.setOnClickListener {  }




    }


}