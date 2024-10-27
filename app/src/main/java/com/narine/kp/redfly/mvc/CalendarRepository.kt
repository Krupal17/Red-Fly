package com.narine.kp.redfly.mvc

import android.util.Log
import com.narine.kp.redfly.adapter.DaysAdapter
import com.narine.kp.redfly.data.CalendarData
import com.narine.kp.redfly.data.DayData
import com.narine.kp.redfly.data.MonthData
import com.narine.kp.redfly.data.YearData
import java.util.Calendar

// Repository class for calendar data
class CalendarRepository {

    private var yearList: List<CalendarData>? = null
    private var yearListPre: List<CalendarData>? = null

    // Function to check if data is already loaded
    fun isDataLoaded(): Boolean {
        return yearList != null
    }

    // Function to load data
    suspend fun loadAllYearData(): List<CalendarData> {
        if (yearList == null) {
            yearList = loadDataFromSource()
        }
        return yearList ?: emptyList()
    }
    // Function to load data
    suspend fun loadAllYearDataPre(): List<CalendarData> {
        if (yearListPre == null) {
            yearListPre = loadDataFromSource2()
        }
        return yearListPre ?: emptyList()
    }

    // Function to return the already loaded data
    fun getYearData(): List<CalendarData>? {
        Log.e("CalendarRepository", "getYearData: ${yearList?.size}")
        return yearList
    }

    val id: Long
        get() = _id++

    var _id: Long = 0

    val day_id: Long
        get() = _day_id++

    var _day_id: Long = 0

    // Simulate the loading of year data
    private suspend fun loadDataFromSource(): List<CalendarData> {
        val allYearsData = mutableListOf<CalendarData>()
        val startYear = 1960
        val endYear = 2100

        var emptyYearData = YearData(0)
        var emptyMonthData = emptyList<MonthData>()
        for (year in startYear..endYear) {
            var yearData = YearData(year)
            val monthData = generateMonthsData(year)
            // Split months into four lists
            val firstQuarter = monthData.subList(0, 3)   // January, February, March
            val secondQuarter = monthData.subList(3, 6)  // April, May, June
            val thirdQuarter = monthData.subList(6, 9)   // July, August, September
            val fourthQuarter = monthData.subList(9, 12) // October, November, December

            allYearsData.add(CalendarData(id, 1, yearData, emptyMonthData))
            allYearsData.add(CalendarData(id, 2, emptyYearData, monthData))
//            allYearsData.add(CalendarData(id, 2, emptyYearData, secondQuarter))
//            allYearsData.add(CalendarData(id, 2, emptyYearData, thirdQuarter))
//            allYearsData.add(CalendarData(id, 2, emptyYearData, fourthQuarter))
        }

        return allYearsData
    }

    suspend fun loadDataFromSource2(): List<CalendarData> {
        val allYearsData = mutableListOf<CalendarData>()
        val startYear = 1970
        val endYear = 2024

        var emptyYearData = YearData(0)
        var emptyMonthData = emptyList<MonthData>()
        for (year in startYear..endYear) {
            var yearData = YearData(year)
            val monthData = generateMonthsData(year)
            // Split months into four lists
            val firstQuarter = monthData.subList(0, 3)   // January, February, March
            val secondQuarter = monthData.subList(3, 6)  // April, May, June
            val thirdQuarter = monthData.subList(6, 9)   // July, August, September
            val fourthQuarter = monthData.subList(9, 12) // October, November, December

            allYearsData.add(CalendarData(id, 1, yearData, emptyMonthData))
            allYearsData.add(CalendarData(id, 2, emptyYearData, firstQuarter))
//            allYearsData.add(CalendarData(id, 2, emptyYearData, firstQuarter))
//            allYearsData.add(CalendarData(id, 2, emptyYearData, secondQuarter))
//            allYearsData.add(CalendarData(id, 2, emptyYearData, thirdQuarter))
//            allYearsData.add(CalendarData(id, 2, emptyYearData, fourthQuarter))
        }

        return allYearsData
    }


    //    private fun generateMonthsData(year: Int): List<MonthData> {
//        val months = mutableListOf<MonthData>()
//        val monthNames = listOf(
//            "January", "February", "March", "April", "May", "June",
//            "July", "August", "September", "October", "November", "December"
//        )
//
//        // Number of days in each month
//        val daysInMonth = listOf(
//            31, // January
//            if (isLeapYear(year)) 29 else 28, // February
//            31, // March
//            30, // April
//            31, // May
//            30, // June
//            31, // July
//            31, // August
//            30, // September
//            31, // October
//            30, // November
//            31  // December
//        )
//
//        // Use a Calendar instance to determine the first day of the month
//        val calendar = Calendar.getInstance()
//
//        for (i in monthNames.indices) {
//            // Set the calendar to the first day of the month
//            calendar.set(year, i, 1)
//            val firstDayOfWeek =
//                calendar.get(Calendar.DAY_OF_WEEK) // 1 = Sunday, 2 = Monday, ..., 7 = Saturday
//
//            // Create a list of days with 0 for preceding days
//            val days = mutableListOf<Int>()
//
//            // Add empty slots for days before the first day of the month
//            for (j in 1 until firstDayOfWeek) {
//                days.add(0) // Use 0 for empty slots
//            }
//
//            // Add actual days of the month
//            for (day in 1..daysInMonth[i]) {
//                days.add(day)
//            }
//
//            // Create and add MonthData for the current month
//            months.add(MonthData(monthNames[i], days))
//        }
//
//        return months
//    }
    private fun generateMonthsData(year: Int): List<MonthData> {
        val months = mutableListOf<MonthData>()
        val monthNames = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        // Number of days in each month
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

        // Use a Calendar instance to determine the first day of the month
        val calendar = Calendar.getInstance()

        for (i in monthNames.indices) {
            // Set the calendar to the first day of the month
            calendar.set(year, i, 1)
            val firstDayOfWeek =
                calendar.get(Calendar.DAY_OF_WEEK) // 1 = Sunday, 2 = Monday, ..., 7 = Saturday

            // Create a list of days with 0 for preceding days
            val days = mutableListOf<DayData>()

            // Add empty slots for days before the first day of the month
            for (j in 1 until firstDayOfWeek) {
                days.add(DayData(day_id,0)) // Use 0 for empty slots
            }

            // Add actual days of the month
            for (day in 1..daysInMonth[i]) {
                days.add(DayData(day_id,day))
            }

            // Create and add MonthData for the current month
            months.add(MonthData(monthNames[i], DaysAdapter(days)))
        }

        return months
    }

    // Function to check if a year is a leap year
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
}