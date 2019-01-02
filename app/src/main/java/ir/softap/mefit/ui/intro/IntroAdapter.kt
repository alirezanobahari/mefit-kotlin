package ir.softap.mefit.ui.intro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.softap.mefit.R
import ir.softap.mefit.ui.abstraction.BaseRecyclerAdapter
import ir.softap.mefit.ui.common.glide.GlideApp
import kotlinx.android.synthetic.main.item_intro.view.*

class IntroAdapter : BaseRecyclerAdapter<Int, IntroAdapter.IntroViewHolder>(
    listOf(
        R.drawable.img_intro_1,
        R.drawable.img_intro_2,
        R.drawable.img_intro_3
    ).toMutableList()
) {

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        if (!::layoutInflater.isInitialized)
            layoutInflater = LayoutInflater.from(parent.context)
        return IntroViewHolder(layoutInflater.inflate(R.layout.item_intro, parent, false))
    }

    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onViewRecycled(holder: IntroViewHolder) {
        holder.onRecycled()
        super.onViewRecycled(holder)
    }

    inner class IntroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(position: Int) {
            val drawableRes = getItem(position)
            with(itemView) {
                GlideApp.with(context)
                    .load(drawableRes)
                    .into(imgIntro)
            }
        }

        fun onRecycled() {
            with(itemView) {
                GlideApp.with(context).clear(imgIntro)
            }
        }
    }

}