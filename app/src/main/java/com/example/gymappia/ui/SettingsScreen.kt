package com.example.gymappia.ui


import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gymappia.AppScreen
import com.example.gymappia.R
import com.example.gymappia.data.UserSettingsRepository
import com.example.gymappia.model.FitnessGoal
import com.example.gymappia.ui.theme.GymAppIATheme
import kotlin.collections.listOf

enum class SettingOption(@StringRes val settingNameId: Int) {
    Preferences(settingNameId = R.string.preferences),
    Notifications(settingNameId = R.string.notifTimeControl),
    Goals(settingNameId = R.string.goals)
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
                containerColor = colorScheme.secondary
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



//todo this does not survive rotation, maybe should in the future
@Composable
fun GoalsManagingScreen(modifier: Modifier = Modifier) {
    val currGoals by UserSettingsRepository.goalsFlow.collectAsState()
    var localGoals = remember { currGoals.toMutableStateList() }

    Column(
        modifier = modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Goals : ", style = MaterialTheme.typography.headlineSmall)
        Row {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                val possibleGoals = FitnessGoal.entries.toList()
                items(items = possibleGoals) { item ->
                    var selected by remember {
                        mutableStateOf(
                            localGoals.contains(item)
                        )

                    }
                    Button(
                        shape = RoundedCornerShape(36.dp),
                        modifier = modifier,
                        onClick = {
                            if (selected) {
                                val index = localGoals.indexOf(item)
                                localGoals.removeAt(index)
                            } else {
                                localGoals.add(item)
                            }
                            selected = !selected
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selected) {
                                colorScheme.inversePrimary
                            } else {
                                colorScheme.primary
                            }
                        )
                    ) {
                        Text(
                            text = item.message
                        )
                    }

                }
            }
        }

        Button(onClick = {
            if (localGoals != currGoals) {
                UserSettingsRepository.updateGoals(localGoals)
            }
        }) {
            Text("Apply Changes")
        }
    }
}

@Composable
fun GoalsManagingScreenPrev(modifier: Modifier = Modifier) {
    var localGoals = remember { mutableListOf<FitnessGoal>() }
    Column(
        modifier = modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Goals : ", style = MaterialTheme.typography.headlineSmall)
        Row {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                val possibleGoals = FitnessGoal.entries.toList()
                items(items = possibleGoals) { item ->
                    var selected by remember { mutableStateOf(localGoals.contains(item)) }
                    Button(
                        shape = RoundedCornerShape(36.dp),
                        modifier = modifier,
                        onClick = {
                            if (selected) {
                                val index = localGoals.indexOf(item)
                                localGoals.removeAt(index)
                            } else {
                                localGoals.add(item)
                            }
                            selected = !selected
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selected) {
                                colorScheme.inversePrimary
                            } else {
                                colorScheme.primary
                            }
                        )
                    ) {
                        Text(
                            text = item.message
                        )
                    }

                }
            }
        }

        Button(onClick = {

        }) {
            Text("Apply Changes")
        }
    }
}

@Composable
fun PreferencesManagingScreen(modifier: Modifier = Modifier) {
    val currName by UserSettingsRepository.nameFlow.collectAsState()
    val currAge by UserSettingsRepository.ageFlow.collectAsState()
    val currWeight by UserSettingsRepository.weightFlow.collectAsState()
    val currHeight by UserSettingsRepository.heightFlow.collectAsState()

    var localAge by rememberSaveable { mutableStateOf(currAge.toString()) }
    var localName by rememberSaveable { mutableStateOf(currName) }
    var localHeight by rememberSaveable { mutableStateOf(currHeight.toString()) }
    var localWeight by rememberSaveable { mutableStateOf(currWeight.toString()) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Yellow)
    ) {
        Row {

            Text("Name : ")
            TextField(
                value = localName,
                onValueChange = { localName = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.primaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                )
            )
        }
        Row {
            Text("Age :")
            TextField(
                value = localAge,
                onValueChange = { localAge = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.primaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                )
            )
        }

        Row {
            Text("Height :")
            TextField(
                value = localHeight,
                onValueChange = { localHeight = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.primaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                )
            )
        }

        Row {
            Text("Weight : ")
            TextField(
                value = localWeight,
                onValueChange = { localWeight = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.primaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                )
            )
        }


        Spacer(modifier = Modifier.size(10.dp))
        Button(onClick = {
            //submit
            if (localAge.toInt() != currAge) {
                UserSettingsRepository.putAge(localAge.toIntOrNull() ?: 0)
            }
            if (localHeight.toFloat() != currHeight) {
                UserSettingsRepository.putHeight(localHeight.toFloatOrNull() ?: 0.0f)
            }
            if (localWeight.toFloat() != currWeight) {
                UserSettingsRepository.putWeight(localWeight.toFloatOrNull() ?: 0.0f)
            }

            if (localName != currName) {
                UserSettingsRepository.changeName(localName)
            }
        }) {
            Text("Apply Changes")
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifTimeManagingScreen() {
    val initHour by UserSettingsRepository.hourFlow.collectAsState()
    val initMinute by UserSettingsRepository.minuteFlow.collectAsState()
    val timePickerState = rememberTimePickerState(
        initialHour = initHour,
        initialMinute = initMinute,
        is24Hour = true
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Select when you would like to be reminded to log!")
        TimeInput(
            state = timePickerState,
            colors = TimePickerDefaults.colors(
                containerColor = colorScheme.primaryContainer,
                clockDialColor = colorScheme.secondary,
                clockDialSelectedContentColor = colorScheme.primary,
                clockDialUnselectedContentColor = colorScheme.secondary
            )
        )
        Button(onClick = {
            UserSettingsRepository.putMinute(timePickerState.minute)
            UserSettingsRepository.putHour(timePickerState.hour)
        }) {
            Text("Confirm selection")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifTimeManagingScreenPrev() {

    val timePickerState = rememberTimePickerState(
        initialHour = 8,
        initialMinute = 30,
        is24Hour = true
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Select when you would like to be reminded to log!")
        TimeInput(
            state = timePickerState,
            colors = TimePickerDefaults.colors(
                containerColor = colorScheme.primaryContainer,
                clockDialColor = colorScheme.secondary,
                clockDialSelectedContentColor = colorScheme.primary,
                clockDialUnselectedContentColor = colorScheme.secondary
            )
        )
        Button(onClick = {

        }) {
            Text("Confirm selection")
        }
    }

}

@Composable
fun PreferencesManagingScreenPrev(modifier: Modifier = Modifier) {

    var localAge by rememberSaveable { mutableStateOf("") }
    var localName by rememberSaveable { mutableStateOf("") }
    var localHeight by rememberSaveable { mutableStateOf("") }
    var localWeight by rememberSaveable { mutableStateOf("") }
    var localGoals = rememberSaveable { mutableStateListOf<FitnessGoal>() }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row {

            Text("Name : ")
            TextField(
                value = localName,
                onValueChange = { localName = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.primaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                )
            )
        }
        Row {
            Text("Age :")
            TextField(
                value = localAge,
                onValueChange = { localAge = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.primaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                )
            )
        }

        Row {
            Text("Height :")
            TextField(
                value = localHeight,
                onValueChange = { localHeight = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.primaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                )
            )
        }

        Row {
            Text("Weight : ")
            TextField(
                value = localWeight,
                onValueChange = { localWeight = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.primaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                )
            )
        }


        Spacer(modifier = Modifier.size(5.dp))
        Button(onClick = {
            //submit

        }) {
            Text("Apply Changes")
        }
        Spacer(modifier = Modifier.size(5.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun SettingOverviewScreenPreview() {
    GymAppIATheme {

        SettingsOverviewScreen(modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun NotifScreenPreview() {
    GymAppIATheme {
        NotifTimeManagingScreenPrev()
    }
}

@Preview(showBackground = true)
@Composable
fun PreferencesScreenPreview() {
    GymAppIATheme {
        PreferencesManagingScreenPrev()
    }
}

@Preview(showBackground = true)
@Composable
fun GoalsScreenPreview() {
    GymAppIATheme {
        GoalsManagingScreenPrev()
    }
}
