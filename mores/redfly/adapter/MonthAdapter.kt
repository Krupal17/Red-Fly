package com.narine.kp.redfly.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.narine.kp.redfly.R
import com.narine.kp.redfly.databinding.ItemMonthBinding
import com.narine.kp.redfly.datas.MonthData
import com.narine.kp.redfly.mvvm.YearRepository.TYPE_MONTH

class MonthAdapter(private val monthList: MutableList<MonthData>) :
    RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val binding = ItemMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val monthData = monthList[position]
        holder.bind(monthData)
    }

    override fun getItemCount(): Int {
        return monthList.size
    }

    inner class MonthViewHolder(private val binding: ItemMonthBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var dayAdapter: DayAdapter

        fun bind(monthData: MonthData) {
            binding.txtMonthName.text = monthData.name

            // Initialize the DayAdapter only if it hasn't been initialized yet
            if (!::dayAdapter.isInitialized) {
                dayAdapter = DayAdapter(monthData.days.toMutableList()) // Create a mutable list
                binding.daysRecyclerView.adapter = dayAdapter
                binding.daysRecyclerView.setRecycledViewPool(viewPool)
            } else {
                dayAdapter.updateDays(monthData.days) // Update existing adapter data
            }
        }
    }

    fun updateMonths(newMonths: List<MonthData>) {
        val diffResult = DiffUtil.calculateDiff(MonthDiffCallback(monthList, newMonths))
        monthList.clear()
        monthList.addAll(newMonths.toMutableList())
        diffResult.dispatchUpdatesTo(this)
    }

}

class MonthDiffCallback(
    private val oldList: List<MonthData>,
    private val newList: List<MonthData>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Logic to check if items are the same (e.g., by checking unique identifiers like month number)
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Logic to check if contents are the same (e.g., by comparing month data)
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}


class MonthItem(val monthData: MonthData) : AbstractItem<MonthItem.ViewHolder>() {

    override val type: Int = TYPE_MONTH // Use an appropriate ID for the view type
    override val layoutRes: Int = R.layout.item_month // Your item layout

    override fun getViewHolder(view: View): ViewHolder = ViewHolder(view)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<MonthItem>(view) {
        private val binding = ItemMonthBinding.bind(view)
        private val dayAdapter: ItemAdapter<DayItem> = ItemAdapter()
        private val fastAdapter: FastAdapter<DayItem> = FastAdapter.with(dayAdapter)

        init {
            binding.daysRecyclerView.adapter = fastAdapter
        }

        override fun bindView(item: MonthItem, payloads: List<Any>) {
            binding.txtMonthName.text = item.monthData.name

            val dayItems = item.monthData.days.map { dayData -> DayItem(dayData) }
            dayAdapter.set(dayItems)
        }

        override fun unbindView(item: MonthItem) {
            binding.txtMonthName.text = null
        }
    }

}