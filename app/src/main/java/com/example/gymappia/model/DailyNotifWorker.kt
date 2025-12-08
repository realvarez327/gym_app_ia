package com.example.gymappia.model

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.gymappia.data.UserSettingsRepository

class DailyNotifWorker (appContext: Context, params:WorkerParameters): Worker(appContext,params){
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun doWork(): Result{
        try {
            NotifHandler.sendLoggingNotification(applicationContext)
        }catch (e: SecurityException){
            //not all permissions given
            Log.e("notifSendingWorker", "security exception, likely permissions prob")
            return Result.failure()
        }catch (e: IllegalStateException){
            //wrong with state or channel, maybe not made yet?
            Log.e("notifSendingWorker", "state or channel prob")
            return Result.retry()
        }catch (e: Exception){
            Log.e("notifSendingWorker", "no clue what problem, but mess up")
            return Result.failure()
        }
        NotifScheduler.scheduleDailyNotif(
            context = applicationContext,
            hour = UserSettingsRepository.hourFlow.value,
            minute = UserSettingsRepository.minuteFlow.value
        )


        return Result.success()
    }

}