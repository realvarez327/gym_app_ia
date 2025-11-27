package com.example.gymappia.model


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.gymappia.R
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotifHandler {

    //logging variables
    private const val DAILY_NOTIF_ID = 1
    const val LOGGING_CHANNEL_ID = "Logging Channel ID"
    const val LOGGING_CHANNEL_NAME = "Logging Channel Name"
    const val LOGGING_CHANNEL_DESC = "Logging Channel Desc"

    fun registerLoggingChannelWithSystem(appContext: Context){
        val loggingChannel = NotificationChannel(
            LOGGING_CHANNEL_ID, LOGGING_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
        )
        loggingChannel.description = LOGGING_CHANNEL_DESC
        val loggingNotifManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        loggingNotifManager.createNotificationChannel(loggingChannel)
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun sendLoggingNotification(appContext: Context){
        val loggingNotifBuilder = NotificationCompat.Builder(appContext, LOGGING_CHANNEL_ID)
            .setSmallIcon(R.drawable.jim)
            .setContentTitle("Remember to log!")
            .setContentText("Log your info or Jim will get it himself...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        NotificationManagerCompat.from(appContext).notify(DAILY_NOTIF_ID, loggingNotifBuilder.build())
    }
}