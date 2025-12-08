package com.example.gymappia.data.roomClasses

import androidx.room.Dao
import androidx.room.Query
import java.time.LocalDateTime

@Dao
interface DailyMetricsDao {
    @Query("SELECT * FROM dailyMetrics WHERE date_and_time=:givenDate")
    suspend fun getMetricsByDay(givenDate: LocalDateTime): DailyMetricsEntity
}