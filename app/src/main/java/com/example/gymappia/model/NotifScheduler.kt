package com.example.gymappia.model

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

object NotifScheduler {
    fun scheduleDailyNotif(context: Context, hour:Int, minute:Int){
        val now = LocalDateTime.now()
        var target = now.withHour(hour).withMinute(minute).withSecond(0)
        if(target.isBefore(now)){
            target = target.plusDays(1)
        }

        val delay = java.time.Duration.between(now,target).toMillis()

        val request = OneTimeWorkRequestBuilder<DailyNotifWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag("daily_reminder_notif")
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork("daily_notification", ExistingWorkPolicy.REPLACE,request)
    }
}