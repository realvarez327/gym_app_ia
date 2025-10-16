package com.example.gymappia.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class Day(val date: LocalDate) {
    var foods: List<Food> =listOf<Food>()
    var workouts: List<Workout> = listOf<Workout>()
    private var dateFormatterPriv = DateTimeFormatter.ofPattern("EEEE ( dd, MMMM, yyyy )")
    val prettyDate = date.format(dateFormatterPriv)
}