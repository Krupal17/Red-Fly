package com.narine.kp.redfly

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.narine.kp.redfly.adapter.YearAdapter1
import com.narine.kp.redfly.databinding.ActivityMainBinding
import com.narine.kp.redfly.mvvm.YearViewModel

class MainActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var yearAdapter: YearAdapter1
    private val yearViewModel: YearViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ListView and Adapter
        yearAdapter = YearAdapter1(mutableListOf())
        binding.yearRecyclerView.adapter = yearAdapter // Assuming you have a ListView with this ID

        // Observe LiveData from ViewModel to get year data
        yearViewModel.yearData.observe(this) { yearList ->
            if (yearList != null) {
                // Update the adapter with the loaded data
                yearAdapter.updateYearData(yearList)
                scrollToCurrentYear()
            }
        }

        // Trigger data loading in ViewModel
        yearViewModel.loadYearDataFromRepository()
    }

    // Scroll to the position of the current year (2024)
    private fun scrollToCurrentYear() {
        val position = yearAdapter.getPositionForYear(2024)
        if (position != -1) {
            binding.yearRecyclerView.scrollToPosition(position)
        }
    }
}

