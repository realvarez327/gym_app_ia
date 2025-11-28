package com.example.gymappia.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gymappia.data.HealthDatabase
import com.example.gymappia.data.WorkoutEntity

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class Day(val date: LocalDate) {
    var foods: List<Food> =listOf<Food>()
    var workouts: MutableList<Workout> = mutableListOf()
    private var dateFormatterPriv = DateTimeFormatter.ofPattern("EEEE ( dd, MMMM, yyyy )")
    val prettyDate = date.format(dateFormatterPriv)

    fun logTodaysWorkoutsInDatabase(toLog: Day, db: HealthDatabase){
        val dao = db.exerciseDao()
        var order:Int = 0
        toLog.workouts.forEach { workout ->
            val entity = WorkoutEntity(
                workoutOrderInDay = order,
                exerciseName = workout.workoutName,
                durationInMinutes = workout.durationMinutes,
                caloriesBurned = workout.caloriesBurned,
                dayOfWorkout = date
            )
            order++
            dao.addWorkout(entity)
        }
    }
}