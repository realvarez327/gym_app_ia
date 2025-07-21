package com.example.gymappia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymappia.ui.theme.GymAppIATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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