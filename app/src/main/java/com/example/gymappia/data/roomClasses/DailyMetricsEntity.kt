package com.example.gymappia.data.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "dailyMetrics")
data class DailyMetricsEntity(
    @PrimaryKey(autoGenerate = true)val id:Int?,
    @ColumnInfo(name = "metric_name")val metricName:String,
    @ColumnInfo(name = "progress_amount")val progressAmount:Float,
    @ColumnInfo(name = "date_and_time")val dateAndTime: LocalDateTime
)
