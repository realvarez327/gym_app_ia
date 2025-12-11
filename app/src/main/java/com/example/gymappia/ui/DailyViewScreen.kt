package com.example.gymappia.ui


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymappia.R
import com.example.gymappia.data.roomClasses.HealthDatabase
import com.example.gymappia.data.roomClasses.MealType
import com.example.gymappia.model.Day
import com.example.gymappia.model.Food
import com.example.gymappia.model.WeekDayViewModelFactory
import com.example.gymappia.model.Workout
import java.time.LocalDate


enum class DayOverviewScreens(@StringRes val stringID: Int) {
    FoodView(R.string.food_view),
    WorkoutView(R.string.workout_view)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyViewScreen(
    modifier: Modifier = Modifier,
    day: Day,
    dayWeekVM: WeekDayViewModel,
    goToAddFood: () -> Unit,
    goToAddWorkout: () -> Unit
) {
    var selectedDestination by rememberSaveable { mutableStateOf(DayOverviewScreens.FoodView) }

    Scaffold(modifier = modifier) { contentPadding ->
        Column(modifier = modifier.fillMaxSize()) {
            Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = day.prettyDate,
                    style = MaterialTheme.typography.headlineSmall
                )

            }
            PrimaryTabRow(
                selectedTabIndex = selectedDestination.ordinal,
                modifier = Modifier.padding(contentPadding)
            ) {
                DayOverviewScreens.entries.forEach { screen ->
                    Tab(
                        selected = (selectedDestination == screen),
                        onClick = { selectedDestination = screen },
                        text = {
                            Text(
                                text = stringResource(screen.stringID)
                            )
                        }
                    )

                }
            }
            when (selectedDestination) {
                DayOverviewScreens.FoodView -> {
                    FoodViewScreen(modifier, dayWeekVM, addFoodFunction = { goToAddFood() })
                }

                DayOverviewScreens.WorkoutView -> {
                    WorkoutViewScreen(modifier, dayWeekVM, goToAddWorkout = { goToAddWorkout() })
                }
            }
        }
    }
}


@Composable
fun AddItemButton(
    modifier: Modifier = Modifier,
    startingTab: DayOverviewScreens,
    goToAddScreen: () -> Unit
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        FloatingActionButton(
            onClick = { goToAddScreen() },
            modifier = modifier,
            shape = MaterialTheme.shapes.small,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "add" + when (startingTab) {
                    DayOverviewScreens.FoodView -> " food"
                    DayOverviewScreens.WorkoutView -> " workout"
                }
            )
        }
    }
}

@Composable
fun FoodViewScreen(
    modifier: Modifier = Modifier,
    DWVM: WeekDayViewModel,
    addFoodFunction: () -> Unit
) {
    val breakfastEntities =
        DWVM.daySelected.foods.filter { food -> food.mealType == MealType.Breakfast }
    val lunchEntities = DWVM.daySelected.foods.filter { food -> food.mealType == MealType.Lunch }
    val dinnerEntities = DWVM.daySelected.foods.filter { food -> food.mealType == MealType.Dinner }
    val snackEntities = DWVM.daySelected.foods.filter { food -> food.mealType == MealType.Snack }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Column {
                Text(
                    text = "Breakfast",
                    style = MaterialTheme.typography.labelMedium
                )
                MealFoodsList(
                    mealList = breakfastEntities
                )

            }
        }


        Row {
            Column {
                Text(
                    text = "Lunch",
                    style = MaterialTheme.typography.labelMedium
                )
                MealFoodsList(
                    mealList = lunchEntities
                )

            }
        }
        Row {
            Column {
                Text(
                    text = "Snacks",
                    style = MaterialTheme.typography.labelMedium
                )
                MealFoodsList(
                    mealList = snackEntities
                )

            }
        }
        Row {
            Column {
                Text(
                    text = "Dinner",
                    style = MaterialTheme.typography.labelMedium
                )
                MealFoodsList(
                    mealList = dinnerEntities
                )

            }
        }

        AddItemButton(
            startingTab = DayOverviewScreens.FoodView,
            goToAddScreen = { addFoodFunction() }
        )


    }

}

@Composable
fun MealFoodsList(mealList: List<Food>) {
    LazyColumn {
        items(mealList) { item ->
            FoodBubble(food = item)
        }
    }
}

@Composable
fun FoodBubble(modifier: Modifier = Modifier, food: Food) {
    Button(
        onClick = {},//enable travelling to same focus screen todo
        shape = RoundedCornerShape(size = 12.dp)
    ) {
        Row {
            Text(
                text = food.foodName,
                style = Typography().bodyMedium
            )
            Spacer(modifier = modifier.weight(1.5f))

        }

    }
}


@Composable
fun WorkoutBubble(modifier: Modifier = Modifier, workout: Workout) {
    Button(
        onClick = {},//enable travelling to same focus screen todo
        shape = RoundedCornerShape(size = 12.dp)
    ) {
        Row {
            Text(
                text = workout.workoutName,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = modifier.weight(1.5f))
            Text(
                text = "${workout.repetitions} repetitions",
                style = MaterialTheme.typography.bodySmall
            )

        }

    }
}


@Composable
fun WorkoutsList(workoutsList: List<Workout>) {
    LazyColumn {
        items(workoutsList) { item ->
            WorkoutBubble(workout = item)
        }
    }
}

@Composable
fun WorkoutViewScreen(
    modifier: Modifier = Modifier,
    DWVM: WeekDayViewModel,
    goToAddWorkout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "workouts")//todo add set organization
        WorkoutsList(workoutsList = DWVM.daySelected.workouts)
        Spacer(modifier = modifier.weight(1f))
        AddItemButton(
            modifier = modifier,
            startingTab = DayOverviewScreens.WorkoutView,
            goToAddScreen = { goToAddWorkout() }
        )
    }

}


@Preview(showBackground = true)
@Composable
fun DailyViewScreenPreview() {
    val db = HealthDatabase.getDatabase(LocalContext.current.applicationContext)
    val dwvm: WeekDayViewModel =viewModel(
        factory = WeekDayViewModelFactory(
            foodDao = db.foodDao(),
            workoutDao = db.exerciseDao()
        )
    )
    DailyViewScreen(
        day = Day(LocalDate.now()), dayWeekVM = dwvm,
        goToAddFood = { },
        goToAddWorkout = { }
    )


}

