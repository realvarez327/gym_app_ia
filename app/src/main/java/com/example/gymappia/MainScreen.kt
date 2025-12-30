package com.example.gymappia

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gymappia.ui.LandingScreen
import com.example.gymappia.ui.QuizScreen
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.navigation
import com.example.gymappia.data.UserSettingsRepository
import com.example.gymappia.data.roomClasses.HealthDatabase
import com.example.gymappia.model.AddItemViewModel
import com.example.gymappia.model.AddItemViewModelFactory
import com.example.gymappia.ui.AddExerciseScreen
import com.example.gymappia.ui.AddFoodScreen
import com.example.gymappia.ui.DailyViewScreen
import com.example.gymappia.ui.SettingsOverviewScreen
import com.example.gymappia.model.UserInitViewModel
import com.example.gymappia.model.WeekDayViewModelFactory
import com.example.gymappia.ui.FoodFocusScreen
import com.example.gymappia.ui.GoalsManagingScreen
import com.example.gymappia.ui.LoadingScreen
import com.example.gymappia.ui.NotifTimeManagingScreen
import com.example.gymappia.ui.PreferencesManagingScreen
import com.example.gymappia.ui.WeekDayViewModel
import com.example.gymappia.ui.WeeklyViewScreen

import java.time.LocalDateTime

enum class AppScreen(@StringRes val id: Int) {
    Start(id = R.string.app_name), Quiz(id = R.string.quiz), DailyView(id = R.string.daily), WeeklyView(
        id = R.string.weekly_view
    ),
    AddFoodSearch(id = R.string.add_food_search), AddExerciseSearch(id = R.string.add_exercise_search), Settings(
        id = R.string.settings_title
    ),
    AddFoodFocus(id = R.string.add_food), AddExerciseFocus(id = R.string.add_exercise), Preferences(
        id = R.string.preferences
    ),
    Notifications(id = R.string.notifTimeControl), Goals(id = R.string.goals)

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    @StringRes currentScreenTitle: Int,
    canGoBack: Boolean,
    goBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreenTitle)) },
        modifier = modifier,
        navigationIcon = {
            if (canGoBack) {
                IconButton(onClick = goBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.back_button_content_desc)
                    )
                }
            }
        })
}

@Composable
fun GymApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Start.name
    )

    Scaffold(topBar = {
        TopBar(
            currentScreenTitle = currentScreen.id,
            goBack = { navController.navigateUp() },
            canGoBack = navController.previousBackStackEntry != null
        )
    }, bottomBar = {
        val currentScreen = backStackEntry?.destination?.route
        if (!(currentScreen == AppScreen.Start.name || currentScreen == AppScreen.Quiz.name)) {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                NavigationBarItem(selected = (currentScreen == AppScreen.WeeklyView.name), icon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.weekly_view)

                    )
                }, onClick = { navController.navigate(AppScreen.WeeklyView.name) })//weekly
                NavigationBarItem(selected = (currentScreen == AppScreen.DailyView.name), icon = {
                    Icon(
                        contentDescription = stringResource(R.string.daily_view),
                        painter = painterResource(R.drawable.sun)

                    )
                }, onClick = { navController.navigate(AppScreen.DailyView.name) })//daily
                NavigationBarItem(selected = (currentScreen == AppScreen.Settings.name), icon = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = stringResource(R.string.settings_title)

                    )
                }, onClick = { navController.navigate(AppScreen.Settings.name) })//settings
            }
        }
    }) { innerPadding ->
        val addFoodGraphName = "add_food_graph"
        val settingsGraphName = "setting_graph"
        val weekGraphName = "week_graph"

        val context = LocalContext.current.applicationContext
        val db = HealthDatabase.getDatabase(context)
        val weekDayViewModelFactory = WeekDayViewModelFactory(
            foodDao = db.foodDao(), workoutDao = db.exerciseDao()
        )
        val addItemViewModelFactory = AddItemViewModelFactory(
            foodDao = db.foodDao(), workoutDao = db.exerciseDao()
        )

        if (UserSettingsRepository.isInitialized()) {
            //todo fix
            Log.d("main_screen", "repo is initialized")
            val name: String = UserSettingsRepository.getName()
            val startDestination:String = if (name == "Unknown") {
                    AppScreen.Start.name
                } else {
                    weekGraphName
                }

            val userInitViewModel: UserInitViewModel = viewModel()
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = startDestination
            ) {
                composable(route = AppScreen.Start.name) {
                    LandingScreen(
                        modifier = Modifier.background(color = colorScheme.background),
                        onNextButtonClicked = { navController.navigate(AppScreen.Quiz.name) })
                }
                composable(route = AppScreen.Quiz.name) {

                    QuizScreen(
                        modifier = Modifier.background(color = colorScheme.background),
                        userInitViewModel = userInitViewModel,
                        endQuizFunction = {
                            userInitViewModel.restartQuiz()
                            navController.navigate(AppScreen.WeeklyView.name)

                        })
                }

                navigation(startDestination = AppScreen.WeeklyView.name, route = weekGraphName) {
                    composable(route = AppScreen.WeeklyView.name) { entry ->
                        val parentEntry = remember(entry) {
                            navController.getBackStackEntry(weekGraphName)
                        }

                        val dayWeekVM: WeekDayViewModel =
                            viewModel(parentEntry, factory = weekDayViewModelFactory)
                        LaunchedEffect(parentEntry) {
                            dayWeekVM.refreshWeek()
                        }
                        WeeklyViewScreen(
                            modifier = Modifier.background(color = colorScheme.background),
                            dayToWeekVM = dayWeekVM,
                            goToDay = { navController.navigate(AppScreen.DailyView.name) })
                    }

                    composable(route = AppScreen.DailyView.name) { entry ->
                        val parentEntry = remember(entry) {
                            navController.getBackStackEntry(weekGraphName)
                        }

                        val dayWeekVM: WeekDayViewModel =
                            viewModel(parentEntry, factory = weekDayViewModelFactory)
                        LaunchedEffect(parentEntry) {
                            dayWeekVM.refreshWeek()
                        }
                        DailyViewScreen(
                            day = dayWeekVM.daySelected,
                            dayWeekVM = dayWeekVM,
                            goToAddFood = {
                                navController.navigate(
                                    AppScreen.AddFoodSearch.name
                                )
                            },
                            goToAddWorkout = {
                                navController.navigate(
                                    AppScreen.AddExerciseSearch.name
                                )
                            })
                    }
                }

                navigation(
                    startDestination = AppScreen.AddFoodSearch.name, route = addFoodGraphName
                ) {
                    composable(route = AppScreen.AddFoodSearch.name) { entry ->
                        val parentEntry = remember(entry) {
                            navController.getBackStackEntry(addFoodGraphName)
                        }
                        val addFoodVM: AddItemViewModel =
                            viewModel(parentEntry, factory = addItemViewModelFactory)
                        AddFoodScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = colorScheme.background),
                            viewModel = addFoodVM,
                            navigateToFoodFocus = { navController.navigate(AppScreen.AddFoodFocus.name) })
                    }

                    composable(route = AppScreen.AddFoodFocus.name) { entry ->
                        val parentEntry = remember(entry) {
                            navController.getBackStackEntry(addFoodGraphName)
                        }
                        val now = remember { LocalDateTime.now() }//todo dc
                        val addFoodVM: AddItemViewModel =
                            viewModel(parentEntry, factory = addItemViewModelFactory)
                        FoodFocusScreen(
                            toShow = addFoodVM.selectedFood,
                            addItemAddViewModel = addFoodVM,
                            day = now,
                            goBackToDay = { navController.navigate(AppScreen.DailyView.name) })
                    }
                }

                composable(route = AppScreen.AddExerciseSearch.name) { entry ->
                    val addExerciseVM: AddItemViewModel =
                        viewModel(factory = addItemViewModelFactory)
                    val now = remember { LocalDateTime.now() }//todo dc
                    AddExerciseScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = colorScheme.background),
                        viewModel = addExerciseVM,
                        goBackToDay = { navController.navigate(AppScreen.DailyView.name) },
                        day = now,
                        toShow = addExerciseVM.selectedWorkout,
                    )
                }



                navigation(startDestination = AppScreen.Settings.name, route = settingsGraphName) {
                    composable(route = AppScreen.Settings.name) {
                        SettingsOverviewScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = colorScheme.background),
                            goToPreferences = { navController.navigate(AppScreen.Preferences.name) },
                            goToNotifications = { navController.navigate(AppScreen.Notifications.name) },
                            goToGoals = { navController.navigate(AppScreen.Goals.name) },
                        )

                    }

                    composable(route = AppScreen.Preferences.name) {
                        PreferencesManagingScreen()
                    }

                    composable(route = AppScreen.Notifications.name) {
                        NotifTimeManagingScreen()
                    }

                    composable(route = AppScreen.Goals.name) {
                        GoalsManagingScreen()
                    }
                }

            }


        } else {
            LoadingScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}