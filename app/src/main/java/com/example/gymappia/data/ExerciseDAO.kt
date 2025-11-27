package com.example.gymappia.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate


@Dao
interface ExerciseDAO {
    @Query("SELECT * FROM workouts WHERE day_of_workout = :day")
    fun loadWorkoutsOfDay(day: LocalDate): List<ExerciseEntity>

    @Insert
    fun addWorkout(workout: ExerciseEntity)

    @Delete
    fun removeWorkout(workout: ExerciseEntity)
}