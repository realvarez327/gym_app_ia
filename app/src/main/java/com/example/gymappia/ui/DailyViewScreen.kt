package com.example.gymappia.ui


import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymappia.R
import com.example.gymappia.data.UserSettingsRepository
import com.example.gymappia.data.roomClasses.MealType
import com.example.gymappia.model.Day
import com.example.gymappia.model.Food
import com.example.gymappia.model.Workout
import java.time.LocalDate
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip


enum class DayOverviewScreens(@StringRes val stringID: Int) {
    FoodView(R.string.food_view),
    WorkoutView(R.string.workout_view),
    StatsView(R.string.stats_view)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyViewScreen(
    modifier: Modifier = Modifier,
    dayWeekVM: WeekDayViewModel,
    goToAddFood: () -> Unit,
    goToAddWorkout: () -> Unit
) {
    var selectedDestination by rememberSaveable { mutableStateOf(DayOverviewScreens.FoodView) }
    val selectedDay by dayWeekVM.daySelected.collectAsStateWithLifecycle()
    Scaffold(modifier = modifier) { contentPadding ->
        Column(modifier = modifier.fillMaxSize()) {
            Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = selectedDay.prettyDate,
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
                                text = stringResource(screen.stringID),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    )

                }
            }
            when (selectedDestination) {
                DayOverviewScreens.FoodView -> {
                    FoodViewScreen(
                        modifier,
                        dayWeekVM,
                        addFoodFunction = { goToAddFood() },
                        currentDay = selectedDay
                    )
                }

                DayOverviewScreens.WorkoutView -> {
                    WorkoutViewScreen(
                        modifier,
                        dayWeekVM,
                        goToAddWorkout = { goToAddWorkout() },
                        currentDay = selectedDay
                    )
                }

                DayOverviewScreens.StatsView -> {
                    StatsViewScreen(
                        modifier = modifier,
                        WDVM = dayWeekVM
                    )
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
                    else -> {
                        throw Error("Add item button somehow called in stats screen")
                    }
                }
            )
        }
    }
}
//todo add truncating food bubble name if too long to show delete button
@Composable
fun FoodViewScreen(
    modifier: Modifier = Modifier,
    DWVM: WeekDayViewModel,
    addFoodFunction: () -> Unit,
    currentDay: Day
) {
    val foods = currentDay.foods
    val breakfastEntities =
        foods.filter { food -> food.mealType == MealType.Breakfast }
    val lunchEntities = foods.filter { food -> food.mealType == MealType.Lunch }
    val dinnerEntities = foods.filter { food -> food.mealType == MealType.Dinner }
    val snackEntities = foods.filter { food -> food.mealType == MealType.Snack }

    Scaffold(floatingActionButton = {
        AddItemButton(
            startingTab = DayOverviewScreens.FoodView,
            goToAddScreen = { addFoodFunction() })
    }) {paddingValues ->
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(paddingValues).padding(8.dp)
        ) {
            item {
                Text(
                    text = "Breakfast",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            items(breakfastEntities){food->
                FoodBubble(food = food, onClick = {}, toDeleteFood = { food: Food -> DWVM.deleteFood(food) } )
            }

            item {
                Text(
                    text = "Lunch",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            items(lunchEntities){food->
                FoodBubble(food = food, onClick = {}, toDeleteFood = { food: Food -> DWVM.deleteFood(food) } )
            }

            item {
                Text(
                    text = "Snacks",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            items(snackEntities){food->
                FoodBubble(food = food, onClick = {}, toDeleteFood = { food: Food -> DWVM.deleteFood(food) } )
            }

            item {
                Text(
                    text = "Dinner",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            items(dinnerEntities){food->
                FoodBubble(food = food, onClick = {}, toDeleteFood = { food: Food -> DWVM.deleteFood(food) } )
            }


        }
    }

}

//todo add routing from bubbles to focus screens for updating
@Composable
fun FoodBubble(
    modifier: Modifier = Modifier,
    food: Food,
    onClick: () -> Unit,
    toDeleteFood: (Food) -> Unit
) {
    Button(
        onClick = { onClick() },//enable travelling to same focus screen todo
        shape = RoundedCornerShape(size = 12.dp)
    ) {
        Row {
            Text(
                text = "${food.foodName},\n ${food.calsPer} cal.",
                style = Typography().bodyMedium
            )
            Spacer(modifier = modifier.weight(1.5f))
            IconButton(
                onClick = { toDeleteFood(food) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Food"
                )
            }
        }

    }
}


@Composable
fun WorkoutBubble(
    modifier: Modifier = Modifier,
    workout: Workout,
    toDeleteWorkout: (Workout) -> Unit
) {
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
            Spacer(modifier = modifier.weight(1.5f))
            Text(
                text = "${workout.setNumber} sets",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = modifier.weight(1.5f))

            IconButton(
                onClick = { toDeleteWorkout(workout) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Food"
                )
            }

        }

    }
}


@Composable
fun WorkoutViewScreen(
    modifier: Modifier = Modifier,
    DWVM: WeekDayViewModel,
    goToAddWorkout: () -> Unit,
    currentDay: Day
) {
//todo add organizing by sets
    Scaffold(
        floatingActionButton = {
            AddItemButton(
                modifier = modifier,
                startingTab = DayOverviewScreens.WorkoutView,
                goToAddScreen = { goToAddWorkout() })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Text(text = "Workouts:") }
            items(currentDay.workouts) { workout ->
                WorkoutBubble(
                    workout = workout,
                    toDeleteWorkout = { workout -> DWVM.deleteWorkout(workout) })

            }
        }
    }

}


@Composable
fun StatsViewScreen(
    modifier: Modifier = Modifier,
    WDVM: WeekDayViewModel
) {
    val today = WDVM.daySelected.collectAsStateWithLifecycle().value
    val foods = today.foods
    val cals = foods.map { food -> food.calsPer }.sum()
    val protein = foods.map { food -> food.protein }.sum()
    val carbs = foods.map { food -> food.carbs }.sum()
    val fat = foods.map { food -> food.fat }.sum()
    val sugar = foods.map { food -> food.sugar }.sum()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Statistics for ${today.prettyDate}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.size(16.dp))
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inversePrimary)
                .padding(4.dp)
                .clip(shape = RoundedCornerShape(12.dp))
        ) {
            val rowModifier =
                Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)
            val cellModifier = Modifier.weight(1f)
            Row(rowModifier) {
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "Metric Name") }
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "Progress") }
            }
            Row(rowModifier) {
                TableCell(cellModifier) { Text(text = "Calories: ") }
                TableCell(cellModifier) { Text(text = "$cals / ${UserSettingsRepository.dailyCaloriesFlow.collectAsState().value} calories") }
            }
            Row(rowModifier) {
                TableCell(cellModifier) { Text(text = "Fat: ") }
                TableCell(cellModifier) { Text(text = "$fat / ${UserSettingsRepository.dailyFatFlow.collectAsState().value} grams") }
            }
            Row(rowModifier) {
                TableCell(cellModifier) { Text(text = "Protein: ") }
                TableCell(cellModifier) { Text(text = "$protein / ${UserSettingsRepository.dailyProteinFlow.collectAsState().value} grams") }
            }
            Row(rowModifier) {
                TableCell(cellModifier) { Text(text = "Carbohydrates: ") }
                TableCell(cellModifier) { Text(text = "$carbs / ${UserSettingsRepository.dailyCarbsFlow.collectAsState().value} grams") }
            }
            Row(rowModifier) {
                TableCell(cellModifier) { Text(text = "Sugar: ") }
                TableCell(cellModifier) { Text(text = "$sugar / ${UserSettingsRepository.dailySugarFlow.collectAsState().value} grams") }
            }
        }
    }
}

@Composable
fun StatsViewScreenPrev(
    modifier: Modifier = Modifier
) {
    val today = Day(
        date = LocalDate.now()
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Statistics for ${today.prettyDate}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.size(16.dp))
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inversePrimary)
                .padding(4.dp)
                .clip(shape = RoundedCornerShape(12.dp))
        ) {
            val rowModifier =
                Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)
            Row(rowModifier) {
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "Metric Name") }
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "Progress") }
            }
            Row(rowModifier) {
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "Calories: ") }
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "200 / 2000") }
            }
            Row(rowModifier) {
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "Fat: ") }
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "15 / 45") }
            }
            Row(rowModifier) {
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "Protein: ") }
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "30 / 50") }
            }
            Row(rowModifier) {
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "Carbohydrates: ") }
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "30 / 50") }
            }
            Row(rowModifier) {
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "Sugar: ") }
                TableCell(modifier = Modifier.weight(1f)) { Text(text = "10 / 20") }
            }
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun DailyViewScreenPreview() {
//    val db = HealthDatabase.getDatabase(LocalContext.current.applicationContext)
//    val dwvm: WeekDayViewModel = viewModel(
//        factory = WeekDayViewModelFactory(
//            foodDao = db.foodDao(),
//            workoutDao = db.exerciseDao()
//        )
//    )
//    DailyViewScreen(
//        dayWeekVM = dwvm,
//        goToAddFood = { },
//        goToAddWorkout = { }
//    )
//
//
//}


@Preview(showBackground = true)
@Composable
fun StatsScreenPreview() {
    StatsViewScreenPrev()
}