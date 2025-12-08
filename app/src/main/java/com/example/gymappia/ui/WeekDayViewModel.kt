package com.example.gymappia.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappia.data.DataModelConverters
import com.example.gymappia.data.roomClasses.DailyMetricsDao
import com.example.gymappia.data.roomClasses.FoodDao
import com.example.gymappia.data.roomClasses.WorkoutDao
import com.example.gymappia.model.Day
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate


class WeekDayViewModel(
    private val foodDao: FoodDao,
    private val workoutDao: WorkoutDao,
    private val dailyMetricsDao: DailyMetricsDao
) : ViewModel(){


    private val converters: DataModelConverters = DataModelConverters()
    init {
        generateWeek()
        setUpWeekInDatabase()
    }
    private fun generateWeek() {
        val today = LocalDate.now()
        val sunday = today.with(DayOfWeek.SUNDAY)
        val days = mutableListOf<Day>()
        for ( i in 0..6){
            days+= Day(
                date = sunday.plusDays(i.toLong())
            )
        }
        _weekdays.value = days

    }


    var daySelected by mutableStateOf(Day(LocalDate.now()))

    private val _weekdays =mutableStateOf<List<Day>>(emptyList())
    val weekdays:List<Day>
        get() = _weekdays.value

    fun setUpWeekInDatabase(){
        viewModelScope.launch {

        }
    }

    private fun weeklySetUp(){
        val today = LocalDate.now()
        val sunday = today.with(DayOfWeek.SUNDAY)

    }

}