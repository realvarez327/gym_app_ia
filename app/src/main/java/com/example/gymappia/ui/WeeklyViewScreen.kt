package com.example.gymappia.ui


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import com.example.gymappia.R
import com.example.gymappia.model.UserInitUiState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyViewScreen(modifier: Modifier = Modifier) {

    Column {
        Text(
            text = stringResource(R.string.welcome_user_to_week, UserInitUiState().userName),
            modifier = modifier.padding(4.dp),
            textAlign = TextAlign.Center
        )
        val year = LocalDate.now().year
        val month = LocalDate.now().month
        val currenDayOfMonth = LocalDate.now().dayOfMonth
        val today: LocalDate = LocalDate.of(year, month, currenDayOfMonth)
        val tempAdj: TemporalAdjuster = TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)
        var iteratingDay = today.with(tempAdj)
        repeat(7) {
            DayPreview(modifier = modifier, goToDay = {}, date = iteratingDay)
            iteratingDay = iteratingDay.plusDays(1)
       }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayPreview(modifier: Modifier = Modifier, goToDay: () -> Unit, date:LocalDate) {

    Button(
        modifier = modifier
            .padding(4.dp)
        , onClick = goToDay,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = modifier.padding(4.dp)) {
            Text(
                text = date.dayOfWeek.name,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = date.toString(),
                style = MaterialTheme.typography.labelSmall
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
                modifier = modifier.size(30.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyViewScreenPreview(modifier: Modifier = Modifier) {
    WeeklyViewScreen(modifier)
}