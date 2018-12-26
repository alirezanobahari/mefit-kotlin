package ir.softap.mefit.ui.video.show

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.softap.mefit.data.model.Comment
import ir.softap.mefit.data.model.Comment.Companion.COMMENT_DIFF_CALLBACK

class VideoDetailAdapter() : PagedListAdapter<Comment, RecyclerView.ViewHolder>(COMMENT_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
