package com.example.gymappia.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymappia.R
import com.example.gymappia.model.UserInitUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyViewScreen(modifier: Modifier = Modifier){

    Column {
        Text(
            text = stringResource(R.string.welcome_user_to_week, UserInitUiState().userName)
        )

        repeat(7){
            DayPreview()
        }


    }
}

@Composable
fun DayPreview(modifier: Modifier = Modifier){


    Row (modifier = modifier){
        Column {
            Text(
                text = "weekday",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text= "DD.MM.YYYY",
                style = MaterialTheme.typography.labelSmall
            )
        }
        Column(
            modifier = modifier
        ){
            ProgressGraphic().DrawProgressGraphic(
                goalColors = listOf( Color.Red,
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
fun WeeklyViewScreenPreview(modifier: Modifier = Modifier){
    WeeklyViewScreen(modifier)
}