@file:Suppress("UNCHECKED_CAST")

package com.example.gymappia.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gymappia.data.roomClasses.DailyMetricsDao
import com.example.gymappia.data.roomClasses.FoodDao
import com.example.gymappia.data.roomClasses.WorkoutDao
import com.example.gymappia.ui.WeekDayViewModel

class WeekDayViewModelFactory(
    private val foodDao: FoodDao,
    private val workoutDao: WorkoutDao,

): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WeekDayViewModel::class.java)){
            return WeekDayViewModel(
                foodDao = foodDao,
                workoutDao = workoutDao
            ) as T
        }
        throw IllegalArgumentException("Not week day view model class! instead is $modelClass")
    }
}

class AddItemViewModelFactory(
    private val foodDao: FoodDao,
    private val workoutDao: WorkoutDao
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddItemViewModel::class.java)){
            return AddItemViewModel(
                foodDao = foodDao,
                workoutDao = workoutDao
            ) as T
        }
        throw IllegalArgumentException("Not add item view model class, instead is $modelClass")
    }
}

