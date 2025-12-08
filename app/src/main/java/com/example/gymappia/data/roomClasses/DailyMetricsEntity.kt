package com.example.gymappia.data.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gymappia.model.DailyMetricType
import java.time.LocalDate

@Entity(tableName = "dailyMetrics")
data class DailyMetricsEntity(
    @PrimaryKey(autoGenerate = true)val id:Int?=null,
    @ColumnInfo(name = "metric_name")val metricName: DailyMetricType,
    @ColumnInfo(name = "progress_amount")val progressAmount:Float,
    @ColumnInfo(name = "date_and_time")val dateAndTime: LocalDate
)
