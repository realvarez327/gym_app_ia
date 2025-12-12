package com.example.gymappia.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappia.data.DataModelConverters
import com.example.gymappia.data.UserSettingsRepository

import com.example.gymappia.data.roomClasses.FoodDao
import com.example.gymappia.data.roomClasses.WorkoutDao
import com.example.gymappia.model.DailyMetricType
import com.example.gymappia.model.DailyMetrics
import com.example.gymappia.model.Day
import com.example.gymappia.model.Food
import com.example.gymappia.model.Workout
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters


class WeekDayViewModel(
    private val foodDao: FoodDao,
    private val workoutDao: WorkoutDao,
) : ViewModel() {


    private val converters: DataModelConverters = DataModelConverters()

    init {
        weeklySetUp()
    }

    var daySelected by mutableStateOf(Day(LocalDate.now()))

    private val _weekdays = mutableStateOf<List<Day>>(emptyList())
    val weekdays: List<Day>
        get() = _weekdays.value

    fun refreshWeek(){
        weeklySetUp()
    }

    // load in from room
    private fun weeklySetUp() {
        val today = LocalDate.now()
        var day = today.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
        val days = mutableListOf<Day>()
        viewModelScope.launch {
            for (i in 1..7) {
                val localDateForm = day.atStartOfDay().plusMinutes(1)//just in case
                val foods = getFoodsOfDay(localDateForm)
                val workouts = getWorkoutsOfDay(localDateForm)
                val proteinGoal = UserSettingsRepository.dailyProteinFlow.value;
                val carbsGoal = UserSettingsRepository.dailyCarbsFlow.value;
                val sugarsGoal = UserSettingsRepository.dailySugarFlow.value
                val fatsGoal = UserSettingsRepository.dailyFatFlow.value
                val caloriesGoal = UserSettingsRepository.dailyCaloriesFlow.value;
                val metrics = getMetricsOfDay(
                    day,
                    foods,
                    proteinGoal,
                    carbsGoal,
                    sugarsGoal,
                    fatsGoal,
                    caloriesGoal
                )
                days += Day(
                    date = day,
                    foods = foods,
                    workouts = workouts,
                    progressInGoals = metrics
                )
                day = day.plusDays(1)

            }
            _weekdays.value = days
            daySelected =  days.firstOrNull { it.date==daySelected.date }?:days.first()
        }

    }

    suspend fun getFoodsOfDay(given: LocalDateTime): List<Food> {
        val start = given.toLocalDate().atStartOfDay()
        val end = start.plusDays(1).minusNanos(1)
        val startEpoch = start.toEpochSecond(ZoneOffset.UTC)
        val endEpoch = end.toEpochSecond(ZoneOffset.UTC)
        return converters.ListOfFoodEntityToListOfFood(foodDao.loadDaysFoods(startEpoch, endEpoch))


    }

    suspend fun getWorkoutsOfDay(given: LocalDateTime): List<Workout> {
        val start = given.toLocalDate().atStartOfDay()
        val end = start.plusDays(1).minusNanos(1)
        val startEpoch = start.toEpochSecond(ZoneOffset.UTC)
        val endEpoch = end.toEpochSecond(ZoneOffset.UTC)
        return converters.ListOfWorkoutEntityToListOfWorkout(
            workoutDao.loadWorkoutsOfDay(
                startEpoch,
                endEpoch
            )
        )

    }

    fun getMetricsOfDay(
        day: LocalDate,
        foods: List<Food>,
        proteinGoal: Int,
        carbsGoal: Int,
        sugarsGoal: Int,
        fatsGoal: Int,
        caloriesGoal: Int
    ): List<DailyMetrics> {
        var proteinProgress = foods.map { food -> food.protein }.sum() / proteinGoal
        if (proteinProgress>1){
            proteinProgress = 1.0f
        }
        var carbProgress = foods.map { food -> food.carbs }.sum() / carbsGoal
        if (carbProgress>1){
            carbProgress = 1.0f
        }
        var sugarProgress = foods.map { food -> food.sugar }.sum() / sugarsGoal
        if (sugarProgress>1){
            sugarProgress = 1.0f
        }

        var caloriesProgress = foods.map { food -> food.calsPer }.sum() / caloriesGoal
        if (caloriesProgress>1){
            caloriesProgress = 1.0f
        }
        var fatProgress = foods.map { food -> food.fat }.sum() / fatsGoal
        if (fatProgress>1){
            fatProgress = 1.0f
        }

        return mutableListOf(
            DailyMetrics(
                proteinProgress,
                DailyMetricType.Protein,
                day
            ),
            DailyMetrics(
                carbProgress,
                DailyMetricType.Carbs,
                day
            ),
            DailyMetrics(
                sugarProgress,
                DailyMetricType.Sugar,
                day
            ),
            DailyMetrics(
                fatProgress,
                DailyMetricType.Fat,
                day
            ),
            DailyMetrics(
                caloriesProgress,
                DailyMetricType.Calories,
                day
            ),


            )
    }
}