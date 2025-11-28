package com.example.gymappia.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey val workoutOrderInDay:Int,
    @ColumnInfo(name = "exercise_name") val exerciseName:String,
    @ColumnInfo(name="duration_in_minutes") val durationInMinutes: Double,
    @ColumnInfo(name = "calories_burned") val caloriesBurned:Double,
    @ColumnInfo(name="day_of_workout") val dayOfWorkout: LocalDate


)