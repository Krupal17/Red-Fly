package com.narine.kp.redfly.mvc

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.narine.kp.redfly.data.CalendarData

class CalendarViewModel : ViewModel() {
    private val repository = CalendarRepository()


    val calendarData: LiveData<List<CalendarData>> = liveData {
        val data = repository.loadAllYearData()
        emit(data)
    }
    val previousYearData: LiveData<List<CalendarData>> = liveData {
        val data = repository.loadAllYearDataPre()
        emit(data)
    }

    fun isDataLoaded() = repository.isDataLoaded()
}

