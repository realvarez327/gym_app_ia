package com.example.gymappia.model


import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class Day(
    val date: LocalDate,
    val foods: List<Food> =emptyList(),
    val workouts: List<Workout> = emptyList(),
    val progressInGoals:List<Progress> = emptyList()
) {
    val prettyDate : String
        get() {
            val dateFormatterPriv = DateTimeFormatter.ofPattern("EEEE ( dd, MMMM, yyyy )")
            return date.format(dateFormatterPriv)
        }


}