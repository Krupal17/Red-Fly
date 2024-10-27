package com.narine.kp.redfly

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.narine.kp.redfly.adapter.YearItem
import com.narine.kp.redfly.databinding.ActivityMainBinding
import com.narine.kp.redfly.mvvm.YearViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var yearAdapter: ItemAdapter<YearItem>
    private lateinit var fastAdapter: FastAdapter<YearItem>
    private val yearViewModel: YearViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FastAdapter and ItemAdapter
        yearAdapter = ItemAdapter()
        fastAdapter = FastAdapter.with(yearAdapter)

        // Setup RecyclerView
        binding.yearRecyclerView.itemAnimator = null
        binding.yearRecyclerView.adapter = fastAdapter

        // Observe LiveData from ViewModel to get year data
        yearViewModel.yearData.observe(this) { yearList ->
            if (yearList != null) {
                // Map YearData to YearItem
                val yearItems = yearList.map { yearData ->
                    YearItem(yearData) // Assuming YearData has a 'months' property
                }
                yearAdapter.set(yearItems) // Update the FastAdapter data
//                scrollToCurrentYear()
            }
        }

        // Trigger data loading in ViewModel
        yearViewModel.loadYearDataFromRepository()
    }

    // Scroll to the position of the current year (2024)
    private fun scrollToCurrentYear() {
        val position =
            yearAdapter.adapterItems.indexOfFirst { (it as YearItem).yearData.year == 2024 }
        if (position != -1) {
            binding.yearRecyclerView.scrollToPosition(position)
        }
    }
}
