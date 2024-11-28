package com.narine.kp.redfly.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.narine.kp.redfly.R
import com.narine.kp.redfly.databinding.ItemYearBinding
import com.narine.kp.redfly.datas.YearData

class YearAdapter1(
    private val yearList: MutableList<YearData>
) : RecyclerView.Adapter<YearAdapter1.YearViewHolder>() {

    init {
        setHasStableIds(true)  // Enable stable IDs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val binding = ItemYearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YearViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        val yearData = yearList[position]
        holder.bind(yearData)
    }

    override fun getItemCount(): Int = yearList.size

    override fun getItemId(position: Int): Long {
        return yearList[position].year.toLong()
    }

    fun updateYearData(newYearList: List<YearData>) {
        val diffResult = DiffUtil.calculateDiff(YearDiffCallback1(yearList, newYearList))
        yearList.clear()
        yearList.addAll(newYearList)
        diffResult.dispatchUpdatesTo(this)
    }

    // Get position of a specific year
    fun getPositionForYear(year: Int): Int {
        return yearList.indexOfFirst { it.year == year }
    }

    inner class YearViewHolder(private val binding: ItemYearBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var monthAdapter: MonthAdapter

        fun bind(yearData: YearData) {
            binding.tvYearTitle.text = yearData.year.toString()

            // Initialize MonthAdapter only if it hasn't been initialized yet
            if (!::monthAdapter.isInitialized) {
                monthAdapter = MonthAdapter(yearData.months.toMutableList()) // Pass mutable list
                binding.monthRecyclerView.adapter = monthAdapter
            } else {
                // Update existing adapter data
                d("FallLife", "bind: YearAdapter-->${yearData.year}")
                monthAdapter.updateMonths(yearData.months)
            }
        }
    }
}
class YearDiffCallback1(
    private val oldList: List<YearData>,
    private val newList: List<YearData>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].year == newList[newItemPosition].year
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}


class YearItem(val yearData: YearData) : AbstractItem<YearItem.ViewHolder>() {

    override val type: Int = R.id.mainYear // Use an appropriate ID for the view type
    override val layoutRes: Int = R.layout.item_year // Your item layout

    override fun getViewHolder(view: View): ViewHolder = ViewHolder(view)

   inner class ViewHolder(view: View) : FastAdapter.ViewHolder<YearItem>(view) {
        private val binding = ItemYearBinding.bind(view)
        private val monthAdapter: ItemAdapter<MonthItem> = ItemAdapter()
        private val fastAdapter: FastAdapter<MonthItem> = FastAdapter.with(monthAdapter)

        init {
            binding.monthRecyclerView.adapter = fastAdapter
            d("SoapLet", "getViewHolder:${yearData.year} ", )
        }

        override fun bindView(item: YearItem, payloads: List<Any>) {
            binding.tvYearTitle.text = item.yearData.year.toString()

            // Only update the adapter if the data changes
            val yearItems = item.yearData.months.map { monthData -> MonthItem(monthData) }
            monthAdapter.set(yearItems)
        }

        override fun unbindView(item: YearItem) {
            binding.tvYearTitle.text = null
        }
    }

}
