package com.narine.kp.redfly.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.narine.kp.redfly.R
import com.narine.kp.redfly.databinding.ItemDayBinding
import com.narine.kp.redfly.mvvm.YearRepository.TYPE_DAY


class DayAdapter(private val dayList: MutableList<Int?>) :
    RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = dayList[position]
        holder.bind(day)
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    inner class DayViewHolder(private val binding: ItemDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: Int?) {
            if (day != null) {
                binding.tvDay.text = day.toString()
                binding.tvDay.visibility = View.VISIBLE
            } else {
                binding.tvDay.visibility = View.GONE
            }
        }
    }

    fun updateDays(newDays: List<Int?>) {
        val diffResult = DiffUtil.calculateDiff(DayDiffCallback(dayList, newDays))
        dayList.clear()
        dayList.addAll(newDays)
        diffResult.dispatchUpdatesTo(this)
    }
}

class DayDiffCallback(
    private val oldList: List<Int?>,
    private val newList: List<Int?>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

class DayItem(val day: Int?) : AbstractItem<DayItem.ViewHolder>() {

    override val type: Int = TYPE_DAY // Use an appropriate ID for the view type
    override val layoutRes: Int = R.layout.item_day // Your item layout

    override fun getViewHolder(view: View): ViewHolder = ViewHolder(view)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<DayItem>(view) {
        private val binding = ItemDayBinding.bind(view)

        override fun bindView(item: DayItem, payloads: List<Any>) {
            if (item.day != 0) {
                binding.tvDay.text = item.day.toString()
                binding.tvDay.visibility = View.VISIBLE
            } else {
                binding.tvDay.visibility = View.GONE
            }
        }

        override fun unbindView(item: DayItem) {
            binding.tvDay.text = null
        }
    }
}

