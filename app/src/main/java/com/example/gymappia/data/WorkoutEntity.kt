package com.example.gymappia.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey val workoutOrderInDay:Int,
    @ColumnInfo(name = "exercise_name") val exerciseName:String,
    @ColumnInfo(name="day_of_workout") val dayOfWorkout: LocalDate,
    @ColumnInfo(name = "reps")val repetitions:Int


)