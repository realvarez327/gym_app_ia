package com.example.gymappia.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate


@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts WHERE day_of_workout = :day")
    suspend fun loadWorkoutsOfDay(day: LocalDate): List<WorkoutEntity>

    @Insert
    suspend fun addWorkout(workout: WorkoutEntity)

    @Delete
    suspend fun removeWorkout(workout: WorkoutEntity)
}