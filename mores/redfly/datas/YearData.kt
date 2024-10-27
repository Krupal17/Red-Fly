package com.narine.kp.redfly.datas

data class YearData(
    val year: Int,
    val months: List<MonthData>
)
data class MonthData(val name: String, val days: List<Int>)

data class DayData(val day: Int)
