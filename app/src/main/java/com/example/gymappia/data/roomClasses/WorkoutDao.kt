package com.example.gymappia.data.roomClasses

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gymappia.data.roomClasses.WorkoutEntity
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface
WorkoutDao {
    @Query("SELECT * FROM workouts WHERE workout_date_time BETWEEN :startOfDay AND :endOfDay")
    suspend fun loadWorkoutsOfDay(startOfDay:Long, endOfDay:Long): List<WorkoutEntity>

    @Insert
    suspend fun addWorkout(workout: WorkoutEntity)

    @Delete
    suspend fun removeWorkout(workout: WorkoutEntity)
}