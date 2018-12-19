package ir.softap.mefit.ui.main

import android.os.Bundle
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragmentActivity

class MainActivity : DaggerXFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
