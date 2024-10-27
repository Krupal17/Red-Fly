package com.narine.kp.redfly

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.narine.kp.redfly.adapter.CalendarAdapter
import com.narine.kp.redfly.adapter.CustomLinearLayoutManager
import com.narine.kp.redfly.databinding.ActivityMainBinding
import com.narine.kp.redfly.mvc.CalendarViewModel

class MainActivity : AppCompatActivity() {
    private val calendarViewModel: CalendarViewModel by viewModels() // ViewModel initialization
    private lateinit var adapter_: CalendarAdapter // Your RecyclerView adapter

    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Set up RecyclerView and its adapter
        setupRecyclerView()

        // Observe the calendar data from the ViewModel
        calendarViewModel.calendarData.observe(this) { calendarData ->
            // Update the adapter with the new data
            adapter_.updateNew(calendarData)

            // Scroll to the target year (e.g., 2024) after data is loaded
//            scrollToYear(2024)
        }

    }

    private fun setupRecyclerView() {
        adapter_ = CalendarAdapter()
        binding.yearRecyclerView.apply {
            setItemViewCacheSize(40)
            layoutManager = CustomLinearLayoutManager(this@MainActivity).apply {
                isItemPrefetchEnabled = true
                initialPrefetchItemCount = 20
            }
            adapter = adapter_
            isNestedScrollingEnabled = false
        }
    }

    private fun scrollToYear(year: Int) {
        val position = adapter_.getPositionForYear(year)
        if (position != -1) {
            binding.yearRecyclerView.scrollToPosition(position)
            adapter_.preCacheItems(binding.yearRecyclerView, 5, position) // Pre-cache around the current year
        }
    }
}


