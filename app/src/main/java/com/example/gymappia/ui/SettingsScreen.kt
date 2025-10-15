package com.example.gymappia.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymappia.R
import com.example.gymappia.ui.theme.GymAppIATheme

enum class SettingOption(@StringRes val settingNameId: Int) {
    Preferences(settingNameId = R.string.preferences),
    Time(settingNameId = R.string.time_date),
    Goals(settingNameId = R.string.goals)
}

enum class SubSettingScreens(@StringRes val id: Int) {
    Preferences(id = R.string.preferences),
    Time(id = R.string.time_date),
    Goals(id = R.string.goals),
    Main(id = R.string.settings_title)
}

@Composable
fun SettingsOverviewScreen(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.displaySmall,
                modifier = modifier.padding(start = 8.dp)
            )
            Spacer(
                modifier = modifier.height(16.dp)
            )
            for (setting: SettingOption in SettingOption.entries) {
                SettingOptionButton(
                    modifier = modifier,
                    goToClickedScreen = {},
                    settingID = setting.settingNameId
                )
            }
        }

    }
}

@Composable
fun SettinsNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SubSettingScreens.Main.name
    ) {
        composable(route = SubSettingScreens.Main.name) {
            SettingsOverviewScreen()
        }
        composable(route = SubSettingScreens.Goals.name) {
            IndividualSettingsScreen(
                modifier = modifier,
                settingOption = SettingOption.Goals,
                goBack = { navController.navigateUp() }
            )
        }
        composable(route = SubSettingScreens.Time.name) {
            IndividualSettingsScreen(
                modifier = modifier,
                settingOption = SettingOption.Time,
                goBack = { navController.navigateUp() }
            )
        }

        composable(route = SubSettingScreens.Preferences.name) {
            IndividualSettingsScreen(
                modifier = modifier,
                settingOption = SettingOption.Preferences,
                goBack = { navController.navigateUp() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingOptionButton(
    modifier: Modifier = Modifier,
    goToClickedScreen: () -> Unit,
    @StringRes settingID: Int
) {
    Row(modifier = modifier.padding(8.dp)) {
        Button(
            onClick = goToClickedScreen,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = modifier.fillMaxWidth()

        ) {
            Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = stringResource(settingID),
                    style = MaterialTheme.typography.headlineSmall
                )
                Icon(

                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.next_button),
                    modifier = modifier.align(alignment = Alignment.CenterVertically)
                )

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualSettingsScreen(
    modifier: Modifier = Modifier,
    settingOption: SettingOption,
    goBack: () -> Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(settingOption.settingNameId)) },
                modifier = modifier,
                navigationIcon = {
                    IconButton(onClick = { goBack }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_content_desc)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            when (settingOption) {
                SettingOption.Preferences -> {
                    Text(text = "Here you will be able to change your name and other personal data. ")
                    TODO()
                }

                SettingOption.Time -> {
                    Text(text = "Here you will be able to change the time and date used by this app (maybe)")
                    TODO()
                }

                SettingOption.Goals -> {
                    Text(text = "Here you can change your goals.")
                    TODO()

                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun SettingOverviewScreenPreview() {
    GymAppIATheme {
        SettingsOverviewScreen(modifier = Modifier)
    }
}