package ir.softap.mefit.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.etiennelenhart.eiffel.state.peek
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.DaggerXFragmentActivity
import ir.softap.mefit.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : DaggerXFragmentActivity() {

    private val introViewModel: IntroViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[IntroViewModel::class.java]
    }

    override val layoutRes: Int = R.layout.activity_intro

    override val initViews: (Bundle?) -> Unit = { _ ->

        with(lstIntro) {
            //Disable swipe scroll on recyclerView
            setOnTouchListener { _, _ ->
                true
            }
            layoutManager = LinearLayoutManager(this@IntroActivity, RecyclerView.HORIZONTAL, false)
            adapter = IntroAdapter()
            PagerSnapHelper().attachToRecyclerView(this)
        }

        btnIntroNext.setOnClickListener { introViewModel.gotoNext() }
        btnIntroPrev.setOnClickListener { introViewModel.gotoPrevious() }
        btnIntroStart.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java)).also {
                finish()
            }
        }

        introViewModel.observeState(this) { introViewState ->

            introViewState.introViewEvent?.peek { introViewEvent ->
                when (introViewEvent) {
                    is IntroViewEvent.NextEvent -> {
                        val hasNext = introViewEvent.page != introViewModel.lastIntroPosition
                        btnIntroStart.apply {
                            visibility = if (!hasNext) View.VISIBLE else View.INVISIBLE
                            isEnabled = !hasNext
                        }
                        btnIntroNext.apply {
                            visibility = if (hasNext) View.VISIBLE else View.INVISIBLE
                            isEnabled = hasNext
                        }
                        btnIntroPrev.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                        }
                        lstIntro.smoothScrollToPosition(introViewEvent.page)
                        true
                    }
                    is IntroViewEvent.PreviousEvent -> {
                        val hasPrev = introViewEvent.page > 0
                        btnIntroStart.apply {
                            visibility = View.INVISIBLE
                            isEnabled = false
                        }
                        btnIntroNext.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                        }
                        btnIntroPrev.apply {
                            visibility = if (hasPrev) View.VISIBLE else View.INVISIBLE
                            isEnabled = hasPrev
                        }
                        lstIntro.smoothScrollToPosition(introViewEvent.page)
                        true
                    }
                }
            }
        }

    }
}