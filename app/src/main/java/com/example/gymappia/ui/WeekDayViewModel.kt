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
import com.example.gymappia.model.DailyMetrics
import com.example.gymappia.model.Day
import com.example.gymappia.model.Food
import com.example.gymappia.model.Workout
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId


class WeekDayViewModel(
    private val foodDao: FoodDao,
    private val workoutDao: WorkoutDao,
    private val dailyMetricsDao: DailyMetricsDao
) : ViewModel() {


    private val converters: DataModelConverters = DataModelConverters()

    init {
        weeklySetUp()
//        generateWeek()
//        viewModelScope.launch {
//            setUpWeekInDatabase()
//        }

    }

    private fun generateWeek() {
        val today = LocalDate.now()
        val sunday = today.with(DayOfWeek.SUNDAY)
        val days = mutableListOf<Day>()
        for (i in 0..6) {
            days += Day(
                date = sunday.plusDays(i.toLong())
            )
        }
        _weekdays.value = days

    }


    var daySelected by mutableStateOf(Day(LocalDate.now()))

    private val _weekdays = mutableStateOf<List<Day>>(emptyList())
    val weekdays: List<Day>
        get() = _weekdays.value

    //do I need this?
    fun setUpWeekInDatabase() {
        viewModelScope.launch {
            for (day in _weekdays.value) {
                day.workouts.forEach { workout ->
                    workoutDao.addWorkout(converters.WorkoutToWorkoutEntity(workout))
                }
                day.foods.forEach { food ->
                    foodDao.addFood(converters.FoodToFoodEntity(food))
                }
                day.progressInGoals.forEach { metric ->
                    dailyMetricsDao.insertMetric(converters.DailyMetricsToDailyMetricsEntity(metric))
                }
            }
        }
    }

    // load in from room
    private fun weeklySetUp() {
        val today = LocalDate.now()
        val days = mutableListOf<Day>()

        for (day in _weekdays.value) {
            val localDateForm = day.date.atStartOfDay().plusMinutes(1)//just in case
            val foods = getFoodsOfDay(localDateForm)
            val workouts = getWorkoutsOfDay(localDateForm)
            val metrics = getMetricsOfDay(localDateForm)
            days +=Day(
                date = day.date,
                foods = foods,
                workouts = workouts,
                progressInGoals = metrics
            )
        }
        _weekdays.value = days
    }

    fun getFoodsOfDay(given: LocalDateTime): List<Food> {
        val start = given.toLocalDate().atStartOfDay()
        val end = start.plusDays(1).minusNanos(1)

        val startEpoch = start.toEpochSecond(ZoneId.systemDefault().rules.getOffset(start))
        val endEpoch = end.toEpochSecond(ZoneId.systemDefault().rules.getOffset(end))
        var result = emptyList<Food>()
        viewModelScope.launch {
            result =
                converters.ListOfFoodEntityToListOfFood(foodDao.loadDaysFoods(startEpoch, endEpoch))
        }
        return result
    }

    fun getWorkoutsOfDay(given: LocalDateTime): List<Workout> {
        val start = given.toLocalDate().atStartOfDay()
        val end = start.plusDays(1).minusNanos(1)

        val startEpoch = start.toEpochSecond(ZoneId.systemDefault().rules.getOffset(start))
        val endEpoch = end.toEpochSecond(ZoneId.systemDefault().rules.getOffset(end))
        var result = emptyList<Workout>()
        viewModelScope.launch {
            result = converters.ListOfWorkoutEntityToListOfWorkout(
                workoutDao.loadWorkoutsOfDay(
                    startEpoch,
                    endEpoch
                )
            )
        }
        return result
    }

    fun getMetricsOfDay(given: LocalDateTime): List<DailyMetrics> {
        val start = given.toLocalDate().atStartOfDay()
        val end = start.plusDays(1).minusNanos(1)

        val startEpoch = start.toEpochSecond(ZoneId.systemDefault().rules.getOffset(start))
        val endEpoch = end.toEpochSecond(ZoneId.systemDefault().rules.getOffset(end))
        var result = emptyList<DailyMetrics>()
        viewModelScope.launch {
            result = converters.ListOfDailyMetricsEntitiesToListOfDailyMetrics(
                dailyMetricsDao.getMetricsByDay(
                    startEpoch,
                    endEpoch
                )
            )
        }
        return result
    }

}