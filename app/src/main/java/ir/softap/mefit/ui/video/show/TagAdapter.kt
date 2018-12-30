package ir.softap.mefit.ui.video.show

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.softap.mefit.R
import ir.softap.mefit.data.model.TAG_DIFF_CALLBACK
import ir.softap.mefit.data.model.Video
import kotlinx.android.synthetic.main.item_tag.view.*

class TagAdapter(private val selectTag: (Video.Tag) -> Unit) :
    ListAdapter<Video.Tag, TagAdapter.TagViewHolder>(TAG_DIFF_CALLBACK) {

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        if (!::layoutInflater.isInitialized)
            layoutInflater = LayoutInflater.from(parent.context)
        return TagViewHolder(layoutInflater.inflate(R.layout.item_tag, parent, false))
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onViewRecycled(holder: TagViewHolder) {
        holder.onRecycled()
        super.onViewRecycled(holder)
    }

    inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val tag = getItem(position)
            with(itemView) {
                tvTagTitle.text = tag.title
                setOnClickListener { selectTag(tag) }
            }
        }

        fun onRecycled() {
            with(itemView) {
                tvTagTitle.text = ""
            }
        }

    }

}