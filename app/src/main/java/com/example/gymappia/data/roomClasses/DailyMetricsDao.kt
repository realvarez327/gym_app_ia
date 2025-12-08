package com.example.gymappia.data.roomClasses

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDateTime

@Dao
interface DailyMetricsDao {
    @Query("SELECT * FROM dailyMetrics WHERE date_and_time BETWEEN :startOfDay AND :endOfDay")
    suspend fun getMetricsByDay(startOfDay:Long, endOfDay:Long): List<DailyMetricsEntity>

    @Insert
    suspend fun insertMetric(given: DailyMetricsEntity)
}