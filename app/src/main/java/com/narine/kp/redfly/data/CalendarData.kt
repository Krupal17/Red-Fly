package com.narine.kp.redfly.data

import com.narine.kp.redfly.adapter.DaysAdapter

// Data classes for Calendar structure
//data class CalendarData(val viewType: Int, val yearData: YearData? = null, val monthData: List<DaysAdapter>)
data class CalendarData(var id:Long,val viewType: Int, val yearData: YearData? = null, val monthData: List<MonthData>)

data class YearData(val year: Int)

//data class MonthData(val monthName: String, val days: List<Int>)
data class MonthData(val monthName: String, val daysAdapter: DaysAdapter)

data class DayData(var dayId:Long, val day: Int)
