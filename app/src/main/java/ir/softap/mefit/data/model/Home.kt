package ir.softap.mefit.data.model

import androidx.recyclerview.widget.DiffUtil

data class Home(
    val id: Int,
    val title: String,
    val videos: List<Video>
)

const val SLIDE_SHOW_KEY = "slide_show"

val HOME_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Home>() {

    override fun areItemsTheSame(oldItem: Home, newItem: Home): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Home, newItem: Home): Boolean =
        oldItem == newItem

}