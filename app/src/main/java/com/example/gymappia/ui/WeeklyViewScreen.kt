package com.example.gymappia.ui

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.gymappia.R
import com.example.gymappia.model.UserInitUiState
import java.time.temporal.WeekFields
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyViewScreen(modifier: Modifier = Modifier){

    Column {
        Text(
            text = stringResource(R.string.welcome_user_to_week, UserInitUiState().userName)
        )


    }
}

@Composable
fun DayPreview(modifier: Modifier = Modifier){

    Row (modifier = modifier){
        Column {
            Text(
                text = "weekday",
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text= "date DD.MM.YYYY",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}