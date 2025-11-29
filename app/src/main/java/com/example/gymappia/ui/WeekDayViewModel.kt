package com.example.gymappia.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappia.data.DataModelConverters
import com.example.gymappia.data.FoodDao
import com.example.gymappia.data.WorkoutDao
import com.example.gymappia.model.Day
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate


class WeekDayViewModel(
    private val foodDao: FoodDao,
    private val workoutDao: WorkoutDao
) : ViewModel(){

    private val converters: DataModelConverters = DataModelConverters()
    init {
        generateWeek()
        loadWeekFromDatabase()
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

    fun loadWeekFromDatabase(){
        viewModelScope.launch {
            val updatedDays = mutableListOf<Day>()
            for (oldDay in _weekdays.value) {
                val foods =
                    converters.ListOfFoodEntityToListOfFood(foodDao.loadDaysFoods(oldDay.date))
                val workouts = converters.ListOfWorkoutEntityToListOfWorkout(
                    workoutDao.loadWorkoutsOfDay(oldDay.date)
                )
                val newDay = oldDay.copy(
                    foods = foods,
                    workouts = workouts
                )
                updatedDays.add(newDay)
            }
            _weekdays.value = updatedDays
        }
    }

}