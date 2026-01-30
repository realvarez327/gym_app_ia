package com.example.gymappia.ui


import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymappia.R
import com.example.gymappia.data.UserSettingsRepository
import com.example.gymappia.model.FitnessGoal
import com.example.gymappia.model.NotifScheduler
import com.example.gymappia.ui.theme.GymAppIATheme

enum class SettingOption(@StringRes val settingNameId: Int) {
    Preferences(settingNameId = R.string.preferences),
    Notifications(settingNameId = R.string.notifTimeControl),
    Goals(settingNameId = R.string.goals),
    CalculatedOptions(settingNameId = R.string.calculatedGoalsSettings)
}


@Composable
fun SettingsOverviewScreen(
    modifier: Modifier = Modifier,
    goToPreferences: () -> Unit,
    goToNotifications: () -> Unit,
    goToGoals: () -> Unit,
    goToCalculatedGoals:()->Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val mod = Modifier

        Spacer(
            modifier = mod.height(16.dp)
        )

        SettingOptionButton(
            modifier = mod,
            goToClickedScreen = { goToNotifications() },
            settingID = SettingOption.Notifications.settingNameId
        )
        SettingOptionButton(
            modifier = mod,
            goToClickedScreen = { goToGoals() },
            settingID = SettingOption.Goals.settingNameId
        )
        SettingOptionButton(
            modifier = mod,
            goToClickedScreen = { goToPreferences() },
            settingID = SettingOption.Preferences.settingNameId
        )
        SettingOptionButton(
            modifier = mod,
            goToClickedScreen = { goToCalculatedGoals() },
            settingID = SettingOption.CalculatedOptions.settingNameId
        )


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingOptionButton(
    modifier: Modifier = Modifier,
    goToClickedScreen: () -> Unit,
    @StringRes settingID: Int
) {
    Log.d("setting btn", "setting button called")
    Row(modifier = Modifier.padding(8.dp)) {
        Button(
            onClick = goToClickedScreen,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.secondaryContainer,
                contentColor = colorScheme.secondary
            ),
            modifier = Modifier.fillMaxWidth()

        ) {
            Row( horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = stringResource(settingID),
                    style = MaterialTheme.typography.headlineSmall
                )
                Icon(

                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.next_button),
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
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
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
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
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
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
fun CalculatedGoalsManagingScreen(modifier: Modifier = Modifier) {
    val currCals by UserSettingsRepository.dailyCaloriesFlow.collectAsStateWithLifecycle()
    val currCarbs by UserSettingsRepository.dailyCarbsFlow.collectAsStateWithLifecycle()
    val currSugar by UserSettingsRepository.weightFlow.collectAsStateWithLifecycle()
    val currFat by UserSettingsRepository.heightFlow.collectAsStateWithLifecycle()
    val currProtein by UserSettingsRepository.dailyProteinFlow.collectAsStateWithLifecycle()

    var localCals by rememberSaveable { mutableStateOf(currCals.toString()) }
    var localCarbs by rememberSaveable { mutableStateOf(currCarbs.toString()) }
    var localSugar by rememberSaveable { mutableStateOf(currSugar.toString()) }
    var localFat by rememberSaveable { mutableStateOf(currFat.toString()) }
    var localProtein by rememberSaveable { mutableStateOf(currProtein.toString()) }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row {

            Text("Calories (kcal) : ")
            TextField(
                value = localCals,
                onValueChange = { localCals = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.primaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                )
            )
        }
        Row {
            Text("Protein (g) :")
            TextField(
                value = localProtein,
                onValueChange = { localProtein = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.primaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                )
            )
        }

        Row {
            Text("Carbohydrates (g) :")
            TextField(
                value = localCarbs,
                onValueChange = { localCarbs = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.primaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                )
            )
        }

        Row {
            Text("Sugar (g) : ")
            TextField(
                value = localSugar,
                onValueChange = { localSugar = it },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.primaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                )
            )
        }
        Row {
            Text("Fat (g) : ")
            TextField(
                value = localFat,
                onValueChange = { localFat = it },
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
            if (localCarbs.toInt() != currCarbs) {
                UserSettingsRepository.putDailyCarbs(localCarbs.toIntOrNull() ?: 0)
            }
            if (localFat.toFloat() != currFat) {
                UserSettingsRepository.putDailyFat(localFat.toIntOrNull() ?: 0)
            }
            if (localSugar.toFloat() != currSugar) {
                UserSettingsRepository.putDailySugar(localSugar.toIntOrNull() ?: 0)
            }
            if (localProtein.toInt() != currProtein) {
                UserSettingsRepository.putProtein(localProtein.toIntOrNull()?:0)
            }
            if (localCals.toInt() != currCals) {
                UserSettingsRepository.putDailyCalories(localCals.toIntOrNull()?:0)
            }
        }) {
            Text("Apply Changes")
        }

    }
}

@Composable
fun PreferencesManagingScreen(modifier: Modifier = Modifier) {
    val currName by UserSettingsRepository.nameFlow.collectAsStateWithLifecycle()
    val currAge by UserSettingsRepository.ageFlow.collectAsStateWithLifecycle()
    val currWeight by UserSettingsRepository.weightFlow.collectAsStateWithLifecycle()
    val currHeight by UserSettingsRepository.heightFlow.collectAsStateWithLifecycle()

    var localAge by rememberSaveable { mutableStateOf(currAge.toString()) }
    var localName by rememberSaveable { mutableStateOf(currName) }
    var localHeight by rememberSaveable { mutableStateOf(currHeight.toString()) }
    var localWeight by rememberSaveable { mutableStateOf(currWeight.toString()) }
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

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
fun NotifsManagingScreen() {
    val initHour by UserSettingsRepository.hourFlow.collectAsState()
    val initMinute by UserSettingsRepository.minuteFlow.collectAsState()
    val timePickerState = rememberTimePickerState(
        initialHour = initHour,
        initialMinute = initMinute,
        is24Hour = true
    )
    val context = LocalContext.current
    val notifsAreAllowed = NotificationManagerCompat.from(context).areNotificationsEnabled()


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val textToShow = "You "+if(!notifsAreAllowed){
            "do not "
        }else{
            ""
        }+"have notifications enabled. To change this, visit your system settings."

        Text(
            text = textToShow,
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.weight(0.5f))
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
            if(notifsAreAllowed){
                NotifScheduler.scheduleDailyNotif(
                    context.applicationContext,
                    hour = timePickerState.hour,
                    minute = timePickerState.minute,
                )
            }else{
                Toast.makeText(context, "We saved the new time, but notifications aren't allowed. We'll send them when they are", Toast.LENGTH_SHORT).show()
            }
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
        val textToShow = "You have notifications enabled.\nTo change this, visit your system settings."

        Text(
            text = textToShow,
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.titleSmall
        )
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

        SettingsOverviewScreen(
            modifier = Modifier,
            goToPreferences = { },
            goToNotifications = { },
            goToGoals = {},
            goToCalculatedGoals = {}
        )
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
