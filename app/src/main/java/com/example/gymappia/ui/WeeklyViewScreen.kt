package com.example.gymappia.ui


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gymappia.AppScreen
import com.example.gymappia.R
import com.example.gymappia.model.UserInitUiState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyViewScreen(
    modifier: Modifier = Modifier,
    externalNavController: NavHostController,
    dayToWeekVM: WeekDayViewModel
) {
    Log.d("navigation", "weekly view screen loaded :)")
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.welcome_user_to_week, UserInitUiState().userName),
            modifier = modifier.padding(4.dp),
            textAlign = TextAlign.Center
        )
        val year = LocalDate.now().year
        val month = LocalDate.now().month
        val currentDayOfMonth = LocalDate.now().dayOfMonth
        val today: LocalDate = LocalDate.of(year, month, currentDayOfMonth)
        val tempAdj: TemporalAdjuster = TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)
        var iteratingDay = today.with(tempAdj)

        repeat(7) {
            DayPreview(
                modifier = modifier,
                goToDay = { externalNavController.navigate(AppScreen.DailyView.name) },
                date = iteratingDay,
                dayIsToday = (iteratingDay.isEqual(today))
            )
            iteratingDay = iteratingDay.plusDays(1)
        }

    }
}

@Composable
fun DayPreview(
    modifier: Modifier = Modifier,
    goToDay: () -> Unit,
    date: LocalDate,
    dayIsToday: Boolean
) {
    val containerBg: Color = if (dayIsToday) {
        MaterialTheme.colorScheme.tertiaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    Button(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        onClick = goToDay,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerBg,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(modifier = modifier.padding(4.dp)) {
            Text(
                text = date.dayOfWeek.name,
                style = MaterialTheme.typography.labelLarge,
                modifier = modifier.background(containerBg)
            )
            Text(
                text = date.toString(),
                style = MaterialTheme.typography.labelMedium,
                modifier = modifier.background(containerBg)
            )
        }
        Column(
            modifier = modifier.padding(4.dp)
        ) {
            ProgressGraphic().DrawProgressGraphic(
                goalColors = listOf(
                    Color.Red,
                    Color.Magenta,
                    Color.Blue,
                    Color.Cyan,
                    Color.Yellow,
                    Color.Green,
                    Color.DarkGray,
                    Color.Black
                ),
                modifier = modifier
                    .size(60.dp)
                    .background(color = containerBg)
            )
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun WeeklyViewScreenPreview(modifier: Modifier = Modifier) {
    WeeklyViewScreen(
        modifier,
        externalNavController = rememberNavController(),
        dayToWeekVM = viewModel()
    )
}