package com.narine.kp.redfly.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.narine.kp.redfly.R
import com.narine.kp.redfly.data.DayData

class DaysAdapter(private val days: List<DayData>) : RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

    init {
        setHasStableIds(true)
    }

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView =
            itemView.findViewById(R.id.tvDay) // Assuming you have this TextView in your day item layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position].day
        if (day != 0) holder.dayTextView.text = day.toString() // Set day text
        if (position == 0 || position % 7 == 0) holder.dayTextView.setTextColor(Color.RED)
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun getItemId(position: Int): Long {
        return days[position].dayId.toLong()
    }
}
