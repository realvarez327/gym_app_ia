package com.example.gymappia.ui


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymappia.R
import com.example.gymappia.data.UserSettingsRepository
import com.example.gymappia.model.DailyMetricType
import com.example.gymappia.model.DailyMetrics
import com.example.gymappia.model.Day
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyViewScreen(
    modifier: Modifier = Modifier,
    dayToWeekVM: WeekDayViewModel,
    goToDay: () -> Unit
) {
    Log.d("navigation", "weekly view screen loaded :)")
    Column(modifier = modifier.fillMaxSize()) {
        val today: LocalDate = LocalDate.now()
        val name by UserSettingsRepository.nameFlow.collectAsStateWithLifecycle()
        Text(
            text = stringResource(R.string.welcome_user_to_week, name),
            modifier = modifier.padding(4.dp),
            textAlign = TextAlign.Center
        )
        dayToWeekVM.weekdays.forEach { day ->
            DayPreview(
                modifier = modifier,
                goToDay = { day ->
                    dayToWeekVM.changeDaySelected(day)
                    goToDay()
                },
                day = day,
                isToday = (day.date==today)
            )
        }
    }
}

@Composable
fun DayPreview(
    modifier: Modifier = Modifier,
    goToDay: (Day) -> Unit,
    day: Day,
    isToday:Boolean
) {
    val containerBg: Color = if (isToday) {
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
        Column(modifier = modifier.background(containerBg).padding(4.dp)) {
            Text(
                text = day.date.dayOfWeek.name,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = day.prettyDate,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Column(
            modifier = modifier.background(color = containerBg).padding(4.dp)
        ) {
            ProgressGraphic().DrawProgressGraphicFromMetrics(
                progresses = day.progressInGoals,
                modifier = Modifier
                    .size(50.dp)
                    .background(color = containerBg)
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyViewScreenPreview(modifier: Modifier = Modifier) {
//    val db = HealthDatabase.getDatabase(LocalContext.current.applicationContext)
//    val dwvm: WeekDayViewModel =viewModel(
//        factory = WeekDayViewModelFactory(
//            foodDao = db.foodDao(),
//            workoutDao = db.exerciseDao(),
//            dailyMetricsDao = db.dailyMetricsDao()
//        )
//    )
    val now = LocalDate.now()
    val testProgresses :List<DailyMetrics> = listOf(DailyMetrics(
        progressAmt = 0.5f,
        dailyMetricName = DailyMetricType.Fat,
        day = now
    ), DailyMetrics(
        progressAmt = 0.4f,
        dailyMetricName = DailyMetricType.Calories,
        day = now
    ))
DayPreview(
    goToDay = {  },
    day = Day(
        date = now,
        foods = emptyList(),
        workouts = emptyList(),
        progressInGoals = testProgresses
    ),
    isToday = true
)
}