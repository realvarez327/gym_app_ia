package com.example.gymappia.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.gymappia.R
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gymappia.model.Food
import com.example.gymappia.model.SampleUser


//todo placeholder code, change when user info is persistently stored
val user: SampleUser = SampleUser()

enum class DayOverviewScreens(@StringRes val stringID: Int) {
    FoodView(R.string.food_view),
    WorkoutView(R.string.workout_view)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyViewScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = DayOverviewScreens.FoodView
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(modifier = modifier) { contentPadding ->
        Column(modifier = modifier.fillMaxSize()) {
            PrimaryTabRow(
                selectedTabIndex = selectedDestination,
                modifier = Modifier.padding(contentPadding)
            ) {
                DayOverviewScreens.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.name)
                            selectedDestination = index
                        },
                        text = {
                            Text(
                                text = stringResource(destination.stringID),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            DailyViewNavHost(navController, startDestination, modifier.padding(contentPadding))
        }
    }
}

@Composable
fun DailyViewNavHost(
    navController: NavHostController,
    startDestination: DayOverviewScreens,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.name
    ) {
        DayOverviewScreens.entries.forEach { destination ->
            composable(destination.name) {
                when (destination) {
                    DayOverviewScreens.FoodView -> FoodViewScreen(modifier)
                    DayOverviewScreens.WorkoutView -> WorkoutViewScreen(modifier)
                }
            }
        }
    }
}


@Composable
fun FoodViewScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Column {
                    Text(
                        text = "Breakfast",
                        style = MaterialTheme.typography.labelMedium
                    )
                    LazyColumn {
                        items(user.breakfastFoods){ item ->

                        }
                    }
                }

            }
            Row {
                Text(
                    text = "Lunch",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Row {
                Text(
                    text = "Snacks",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Row {
                Text(
                    text = "Dinner",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun MealFoodsList(modifier: Modifier = Modifier, mealList: List<Food>){
    LazyColumn {
        items(mealList){item ->

        }
    }
}

@Composable
fun FoodBubble(modifier: Modifier = Modifier, food:Food ){
    Button(
        onClick = {},
        shape = RoundedCornerShape(size = 12.dp)//eventually add in automatic sizing todo
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
fun WorkoutViewScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = "workouts")
    }
}

@Preview(showBackground = true)
@Composable
fun DailyViewScreenPreview() {
    DailyViewScreen()
}

