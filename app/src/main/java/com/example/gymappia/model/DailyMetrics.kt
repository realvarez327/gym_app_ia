package com.example.gymappia.model


import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime

data class DailyMetrics(
    val progressAmt:Float,
    val dailyMetricName:DailyMetricType,
    val day: LocalDate
)

enum class DailyMetricType (val color: Color){
    Protein(color =Color.Red),
    Sugar(color = Color.Blue),
    Fat(color =Color.Cyan),
    Carbs(color = Color.Yellow),
    Calories (color = Color.Green)
}
