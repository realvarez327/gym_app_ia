package com.example.gymappia.ui


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymappia.R
import com.example.gymappia.data.UserSettingsRepository
import com.example.gymappia.model.Day


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyViewScreen(
    modifier: Modifier = Modifier,
    dayToWeekVM: WeekDayViewModel,
    goToDay: () -> Unit
) {
    Log.d("navigation", "weekly view screen loaded :)")
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.welcome_user_to_week, UserSettingsRepository.nameFlow),
            modifier = modifier.padding(4.dp),
            textAlign = TextAlign.Center
        )
        dayToWeekVM.weekdays.forEach { day ->
            DayPreview(
                modifier = modifier,
                goToDay = { day ->
                    dayToWeekVM.daySelected = day
                    goToDay()
                },
                dayWeekVM = dayToWeekVM,
                day = day
            )
        }
    }
}

@Composable
fun DayPreview(
    modifier: Modifier = Modifier,
    goToDay: (Day) -> Unit,
    dayWeekVM: WeekDayViewModel,
    day: Day
) {
    val containerBg: Color = if (dayWeekVM.daySelected == day) {
        MaterialTheme.colorScheme.tertiaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    Button(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        onClick = { goToDay(day) },
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerBg,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(modifier = modifier.padding(4.dp)) {
            Text(
                text = day.date.dayOfWeek.name,
                style = MaterialTheme.typography.labelLarge,
                modifier = modifier.background(containerBg)
            )
            Text(
                text = day.prettyDate,
                style = MaterialTheme.typography.labelMedium,
                modifier = modifier.background(containerBg)
            )
        }
        Column(
            modifier = modifier.padding(4.dp)
        ) {
            ProgressGraphic().RealDrawProgressGraphic(
                progresses = day.progressInGoals,
                modifier = modifier
                    .size(60.dp)
                    .background(color = containerBg)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyViewScreenPreview(modifier: Modifier = Modifier) {
    WeeklyViewScreen(
        modifier,
        dayToWeekVM = viewModel(),
        goToDay = { }
    )
}