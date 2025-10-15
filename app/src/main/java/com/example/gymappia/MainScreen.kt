package com.example.gymappia

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymappia.model.QuizHandler
import com.example.gymappia.ui.AddExerciseScreen
import com.example.gymappia.ui.AddFoodScreen
import com.example.gymappia.ui.DailyViewScreen
import com.example.gymappia.ui.SettingsOverviewScreen
import com.example.gymappia.ui.UserInitViewModel
import com.example.gymappia.ui.WeeklyViewScreen

enum class AppScreen(@StringRes val id: Int) {
    Start(id = R.string.app_name),
    Quiz(id = R.string.quiz),
    DailyView(id = R.string.daily),
    WeeklyView(id =R.string.weekly_view),
    AddFoodSearch(id = R.string.add_food_search),
    AddExerciseSearch(id = R.string.add_exercise_search),
    Settings(id = R.string.settings_title)

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
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GymApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Start.name
    )

    Scaffold(
        topBar = {
            TopBar(
                currentScreenTitle = currentScreen.id,
                goBack = { navController.navigateUp() },
                canGoBack = navController.previousBackStackEntry != null
            )
        }
    ) { innerPadding ->

        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppScreen.Start.name
        ) {
            composable(route = AppScreen.Start.name) {
                LandingScreen(
                    modifier = Modifier
                        .background(color = colorScheme.background),
                    onNextButtonClicked = { navController.navigate(AppScreen.Quiz.name) }
                )
            }

            composable(route = AppScreen.Quiz.name) {
                val userInitViewModel: UserInitViewModel = viewModel()
                val quizHandlerToGive = QuizHandler(
                    externalNavController = navController,
                    endQuizFunction = { navController.navigate(AppScreen.WeeklyView.name) },
                )

                QuizScreen(
                    modifier = Modifier
                        .background(color = colorScheme.background),
                    userInitViewModel = userInitViewModel,
                    quizHandlerGiven = quizHandlerToGive
                )
            }

            composable (route = AppScreen.DailyView.name){
                DailyViewScreen()
            }

            composable (route = AppScreen.WeeklyView.name){
                WeeklyViewScreen(
                    modifier = Modifier.background(color= colorScheme.background)
                )
            }

            composable(route = AppScreen.AddFoodSearch.name){
                AddFoodScreen(
                    modifier = Modifier.fillMaxSize().background(color = colorScheme.background)
                )
            }

            composable(route = AppScreen.AddExerciseSearch.name) {
                AddExerciseScreen(
                    modifier = Modifier.fillMaxSize().background(color = colorScheme.background)
                )
            }

            composable (route = AppScreen.Settings.name){
                SettingsOverviewScreen(
                    modifier = Modifier.fillMaxSize().background(color = colorScheme.background)
                )

            }
        }


    }

}