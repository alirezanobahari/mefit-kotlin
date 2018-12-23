package ir.softap.mefit.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import ir.softap.mefit.R
import kotlinx.android.synthetic.main.item_list_state.view.*

class ListStateItemViewHolder private constructor(
    itemView: View,
    private val enableNothingFound: Boolean = true
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(
            inflater: LayoutInflater,
            parent: ViewGroup,
            enableNothingFound: Boolean = true
        ) =
            ListStateItemViewHolder(
                inflater.inflate(
                    R.layout.item_list_state, parent, false
                ), enableNothingFound
            )

    }

    fun onBind(
        listState: ListState,
        @StringRes nothingFoundTitle: Int = R.string.msg_nothing_found,
        retry: (() -> Unit)? = null
    ) {
        with(itemView) {
            when (listState) {
                ListState.ERROR -> {
                    imgListState.setImageResource(R.drawable.ic_reload)
                    tvTitleListState.setText(R.string.action_retry)
                    with(clListStateContainer) {
                        isClickable = true
                        isFocusable = true
                        setOnClickListener { retry?.invoke() }
                    }
                }
                ListState.SUCCESS -> {
                    if (enableNothingFound) {
                        imgListState.setImageResource(R.drawable.ic_magnifier_not_found)
                        tvTitleListState.setText(nothingFoundTitle)
                        with(clListStateContainer) {
                            isClickable = false
                            isFocusable = false
                        }
                    }
                }
                else -> {
                }

            }
        }
    }
}