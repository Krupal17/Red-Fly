package com.narine.kp.redfly.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.narine.kp.redfly.datas.MonthData
import com.narine.kp.redfly.datas.YearData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YearViewModel : ViewModel() {

    private val _yearData = MutableLiveData<List<YearData>>()
    val yearData: LiveData<List<YearData>> = _yearData
    // Load data from repository into LiveData
    fun loadYearDataFromRepository() {
        _yearData.value = YearRepository.getYearData()
    }
    private val minYear = 1990
    private val maxYear = 2040
    private val yearChunkSize = 10

    // Function to load all year data progressively
    suspend fun loadAllYearData() {
        val allYearsData = mutableListOf<YearData>()
        var currentYear = maxYear

        withContext(Dispatchers.IO) {
            while (currentYear >= minYear) {
                val yearsChunk = generateYearsDataChunk(currentYear, yearChunkSize)
                allYearsData.addAll(yearsChunk)
                currentYear -= yearChunkSize
            }
        }

        // Post data to LiveData
        withContext(Dispatchers.Main) {
            _yearData.value = allYearsData
        }
    }

    // Function to generate year data
    private fun generateYearsDataChunk(startYear: Int, chunkSize: Int): List<YearData> {
        val years = mutableListOf<YearData>()
        for (year in startYear downTo (startYear - chunkSize + 1)) {
            if (year < minYear) break
            val months = generateMonthsData()
            years.add(YearData(year, months))
        }
        return years
    }

    // Dummy months data generation
    private fun generateMonthsData(): List<MonthData> {
        val months = mutableListOf<MonthData>()
        val monthNames = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        monthNames.forEach { monthName ->
            val days = (1..30).toList()
            months.add(MonthData(monthName, days))
        }

        return months
    }
}
