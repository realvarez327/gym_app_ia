package com.example.gymappia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymappia.data.UserSettingsRepository
import com.example.gymappia.model.NotifHandler
import com.example.gymappia.model.NotifScheduler
import com.example.gymappia.ui.theme.GymAppIATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        NotifHandler.registerLoggingChannelWithSystem(this)
//        val defaultHour = 8
//        val defaultMinute = 30
//        NotifScheduler.scheduleDailyNotif(this,defaultHour,defaultMinute)
        UserSettingsRepository.init(this)
        enableEdgeToEdge()
        setContent {
            GymAppIATheme {
                GymApp()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GymAppIATheme {
        GymApp()
    }
}