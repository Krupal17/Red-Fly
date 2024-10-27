package com.narine.kp.redfly.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.narine.kp.redfly.data.CalendarData
import com.narine.kp.redfly.data.MonthData
import com.narine.kp.redfly.data.YearData
import com.narine.kp.redfly.databinding.ItemMonth2Binding
import com.narine.kp.redfly.databinding.ItemYear2Binding

// Define constants for view types
const val VIEW_TYPE_YEAR = 1
const val VIEW_TYPE_MONTH = 2

class CalendarAdapter(
    private val calendarData: MutableList<CalendarData> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true) // Use stable IDs for better recycling
    }
    fun preCacheItems(parent: ViewGroup, numberOfItemsToPreCache: Int) {
        for (i in 0 until minOf(numberOfItemsToPreCache, itemCount)) {
            val viewType = getItemViewType(i)
            val holder = onCreateViewHolder(parent, viewType)
            onBindViewHolder(holder, i)
        }
    }
    fun preCacheItems(parent: ViewGroup, numberOfItemsToPreCache: Int, position: Int) {
        val itemCount = itemCount // Get the total number of items in the adapter

        Log.d("PreCache", "Starting to pre-cache around position: $position")

        // Pre-cache items before the current position
        val start = maxOf(0, position - numberOfItemsToPreCache)
        for (i in start until position) {
            val viewType = getItemViewType(i)
            val holder = onCreateViewHolder(parent, viewType) // Create the ViewHolder
            onBindViewHolder(holder, i) // Bind the ViewHolder to the item
            Log.d("PreCache", "Pre-cached item before at position: $i")
        }

        // Pre-cache items after the current position
        val end = minOf(itemCount - 1, position + numberOfItemsToPreCache)
        for (i in position until end) {
            val viewType = getItemViewType(i)
            val holder = onCreateViewHolder(parent, viewType) // Create the ViewHolder
            onBindViewHolder(holder, i) // Bind the ViewHolder to the item
            Log.d("PreCache", "Pre-cached item after at position: $i")
        }
    }

    // ViewHolder classes
    inner class YearViewHolder(val binding: ItemYear2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(yearData: YearData) {
            Log.d("CalendarAdapter", "YearViewHolder created ${yearData.year}")
            binding.yearTextView.text = yearData.year.toString()
        }
    }

    inner class MonthViewHolder(val binding: ItemMonth2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(monthData: List<MonthData>) {
            Log.d("CalendarAdapter", "MonthViewHolder created")

            // Bind month data to the view
            setupRecyclerView(binding.daysRecyclerView1, monthData[0], binding.monthTextView1)
            setupRecyclerView(binding.daysRecyclerView2, monthData[1], binding.monthTextView2)
            setupRecyclerView(binding.daysRecyclerView3, monthData[2], binding.monthTextView3)
            setupRecyclerView(binding.daysRecyclerView4, monthData[3], binding.monthTextView4)
            setupRecyclerView(binding.daysRecyclerView5, monthData[4], binding.monthTextView5)
            setupRecyclerView(binding.daysRecyclerView6, monthData[5], binding.monthTextView6)
            setupRecyclerView(binding.daysRecyclerView7, monthData[6], binding.monthTextView7)
            setupRecyclerView(binding.daysRecyclerView8, monthData[7], binding.monthTextView8)
            setupRecyclerView(binding.daysRecyclerView9, monthData[8], binding.monthTextView9)
            setupRecyclerView(binding.daysRecyclerView10, monthData[9], binding.monthTextView10)
            setupRecyclerView(binding.daysRecyclerView11, monthData[10], binding.monthTextView11)
            setupRecyclerView(binding.daysRecyclerView12, monthData[11], binding.monthTextView12)
        }

        private fun setupRecyclerView(recyclerView: RecyclerView, monthData: MonthData, monthTextView: TextView) {
            recyclerView.setItemViewCacheSize(2)
            recyclerView.adapter = monthData.daysAdapter
            monthTextView.text = monthData.monthName
        }

    }


    override fun getItemViewType(position: Int): Int {
        return calendarData[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_YEAR -> {
                val binding =
                    ItemYear2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                YearViewHolder(binding)
            }

            VIEW_TYPE_MONTH -> {
                val binding =
                    ItemMonth2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                MonthViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        Log.e("TAG", "onDetachedFromRecyclerView: ")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is YearViewHolder -> holder.bind(calendarData[position].yearData!!)
            is MonthViewHolder -> {
                holder.bind(calendarData[position].monthData)

                // Use the RecyclerView as the parent for pre-caching
                val parent = holder.itemView.parent as? RecyclerView ?: return
                preCacheItems(parent, 3, position) // Adjust the number of items to pre-cache as needed
            }
        }
    }


    override fun getItemCount(): Int = calendarData.size

    override fun getItemId(position: Int): Long {
        return calendarData[position].id.toLong() // Assuming each CalendarData has a unique 'id'
    }

    fun updateNew(newItems: List<CalendarData>) {
        val diffCallback = CalendarDiffCallback(calendarData, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        calendarData.clear()
        calendarData.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)

    }

    fun getPositionForYear(year: Int): Int {
        return calendarData.indexOfFirst { it.yearData?.year == year }
    }

    fun updateNewPrev(newItems: List<CalendarData>) {
        // Find the position to insert new items
        val insertPosition = if (calendarData.isEmpty()) 0 else calendarData.size

        // Add the new items to the list
        calendarData.addAll(insertPosition, newItems)

        // Notify the adapter of the range changed, so it can handle this efficiently
        notifyItemRangeInserted(insertPosition, newItems.size)
    }
}

class CalendarDiffCallback(
    private val oldList: List<CalendarData>,
    private val newList: List<CalendarData>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Implement your logic for item uniqueness, e.g., ID
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
