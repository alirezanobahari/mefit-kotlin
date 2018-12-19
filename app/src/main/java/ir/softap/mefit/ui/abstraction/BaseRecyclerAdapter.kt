package ir.softap.mefit.ui.abstraction

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<D, VH : RecyclerView.ViewHolder>(dataList: MutableList<D> = mutableListOf()) :
        RecyclerView.Adapter<VH>() {

    private var dataList: MutableList<D> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        if (dataList.isNotEmpty())
            this.dataList = dataList
    }

    override fun getItemCount(): Int = dataList.size

    fun getItem(position: Int) = dataList[position]

    fun submitData(dataList: MutableList<D>) {
        this.dataList = dataList
    }

    fun indexOf(d: D) = dataList.indexOf(d)

    fun insert(data: D, index: Int = dataList.size) {
        dataList.add(index, data)
        notifyItemInserted(index)
    }

    fun insertAll(dataList: List<D>, index: Int = dataList.size) {
        this.dataList.addAll(index, dataList)
        notifyItemRangeInserted(index, dataList.size)
    }

    fun update(data: D, index: Int) {
        dataList[index] = data
        notifyItemChanged(index)
    }

    fun remove(index: Int) {
        dataList.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, dataList.size)
    }

}