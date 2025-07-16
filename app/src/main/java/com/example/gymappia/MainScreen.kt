package com.example.gymappia

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gymappia.ui.AppViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gymappia.ui.LandingScreen

enum class AppScreen(@StringRes val id:Int){
    Start(id = R.string.app_name),
    Quiz(id = R.string.quiz)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    @StringRes currentScreenTitle: Int,
    canGoBack: Boolean,
    goBack: () -> Boolean
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(currentScreenTitle)
            )

        },
        modifier = modifier,
        navigationIcon = {
            if(canGoBack){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_content_desc)
                )
            }
        }
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun GymApp(){
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Start.name
    )

    val viewModel : AppViewModel= AppViewModel()
    Scaffold (
        topBar = {
            TopBar(
                currentScreenTitle = currentScreen.id,
                goBack = {navController.navigateUp()},
                canGoBack = navController.previousBackStackEntry != null
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppScreen.Start.name
        ){
            composable(route = AppScreen.Start.name){
                LandingScreen(modifier = Modifier.fillMaxSize())
            }

            composable (route = AppScreen.Quiz.name){

            }
        }


    }

}