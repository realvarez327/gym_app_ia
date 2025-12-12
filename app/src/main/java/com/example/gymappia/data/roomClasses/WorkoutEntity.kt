package com.example.gymappia.data.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "workouts"
)
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id:Int?=null,
    @ColumnInfo(name = "exercise_name") val exerciseName:String,
    @ColumnInfo(name="workout_date_time") val dayOfWorkout: Long,
    @ColumnInfo(name = "reps")val repetitions:Int,
    @ColumnInfo(name = "set_number")val setNumber:Int,
    @ColumnInfo(name = "weight_used")val weightUsed:Float

)