package com.narine.kp.redfly.mvvm

import android.util.Log
import com.narine.kp.redfly.datas.MonthData
import com.narine.kp.redfly.datas.YearData
import java.util.Calendar

object YearRepository {
    private var yearList: List<YearData>? = null
    const val TYPE_YEAR = 1
    const val TYPE_MONTH = 2
    const val TYPE_DAY = 3
    // Function to check if data is already loaded
    fun isDataLoaded(): Boolean {
        return yearList != null
    }

    // Function to load data
    suspend fun loadAllYearData(): List<YearData> {
        if (yearList == null) {
            yearList = loadDataFromSource()
        }
        return yearList ?: emptyList()
    }

    // Function to return the already loaded data
    fun getYearData(): List<YearData>? {
        Log.e("BlackCoat", "getYearData: ${yearList?.size}", )
        return yearList
    }

    // Simulate the loading of year data
    private suspend fun loadDataFromSource(): List<YearData> {
        val allYearsData = mutableListOf<YearData>()
        val minYear = 1990
        val maxYear = 2040
        val yearChunkSize = 10
        var currentYear = maxYear

        while (currentYear >= minYear) {
            val yearsChunk = generateYearsDataChunk(currentYear, yearChunkSize)
            allYearsData.addAll(yearsChunk)
            currentYear -= yearChunkSize
        }

        return allYearsData
    }

    private fun generateYearsDataChunk(startYear: Int, chunkSize: Int): List<YearData> {
        val years = mutableListOf<YearData>()
        for (year in startYear downTo (startYear - chunkSize + 1)) {
            val months = generateMonthsData(year)
            years.add(YearData(year, months))
        }
        return years
    }

    private fun generateMonthsData(year: Int): List<MonthData> {
        val months = mutableListOf<MonthData>()
        val monthNames = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        val daysInMonth = listOf(
            31, // January
            if (isLeapYear(year)) 29 else 28, // February
            31, // March
            30, // April
            31, // May
            30, // June
            31, // July
            31, // August
            30, // September
            31, // October
            30, // November
            31  // December
        )

        // Start from January 1st of the given year
        val calendar = Calendar.getInstance()
        calendar.set(year, 0, 1) // Set to January 1st of the specified year

        // Generate month data
        for (i in monthNames.indices) {
            // Get the first day of the month (0 = Sunday, 1 = Monday, ..., 6 = Saturday)
            calendar.set(year, i, 1)
            val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1 = Sunday, 2 = Monday, ..., 7 = Saturday

            // Create a list of days with null for the preceding days
            val days = mutableListOf<Int>()

            // Add empty slots for days before the first day of the month
            for (j in 1 until firstDayOfWeek) {
                days.add(0) // Use null for empty slots
            }

            // Add actual days of the month
            for (day in 1..daysInMonth[i]) {
                days.add(day)
            }

            months.add(MonthData(monthNames[i], days))
        }

        return months
    }

    // Function to check if a year is a leap year
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
}