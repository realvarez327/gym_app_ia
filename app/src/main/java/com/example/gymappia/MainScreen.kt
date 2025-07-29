package com.example.gymappia

import androidx.annotation.StringRes
import androidx.compose.foundation.background
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
import com.example.gymappia.ui.DailyViewScreen
import com.example.gymappia.ui.UserInitViewModel

enum class AppScreen(@StringRes val id: Int) {
    Start(id = R.string.app_name),
    Quiz(id = R.string.quiz),
    DailyView(id = R.string.daily)
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
                QuizScreen(
                    modifier = Modifier
                        .background(color = colorScheme.background),
                    userInitViewModel = userInitViewModel,
                    toEndQuiz = {navController.navigate(AppScreen.DailyView.name)}
                )
            }

            composable (route = AppScreen.DailyView.name){
                DailyViewScreen()
            }
        }


    }

}