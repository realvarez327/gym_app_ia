package com.example.gymappia.ui

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gymappia.R
import com.example.gymappia.model.Question
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.example.gymappia.model.QuestionType
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableFloatStateOf

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo
import com.example.gymappia.data.ActivityLevel
import com.example.gymappia.data.QuestionsDataSource
import com.example.gymappia.data.UserSettingsRepository
import com.example.gymappia.data.YesOrNoResponse
import com.example.gymappia.model.FitnessGoal
import com.example.gymappia.model.Gender
import com.example.gymappia.model.NotifHandler
import com.example.gymappia.model.NotifScheduler
import com.example.gymappia.model.NumberQuestionSubject
import com.example.gymappia.model.SingleChoiceQuestionSubject
import com.example.gymappia.model.UserInitViewModel
import com.example.gymappia.ui.theme.GymAppIATheme
import java.util.jar.Manifest


fun String.consistsOfOnlyLetters(): Boolean {
    return all { it.isLetter() }
}

fun String.consistsOfOnlyDigits(): Boolean {
    return all { it.isDigit() }
}

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier, userInitViewModel: UserInitViewModel, endQuizFunction: () -> Unit
) {
    val questions = QuestionsDataSource.userStartQuestions
    val viewModel: UserInitViewModel = userInitViewModel
    //todo when i learn coroutines, add determinate linear progress indicator
    val currIndex: Int = viewModel.quizHandler.currentIndex.value
    val currQuestion: Question = questions[currIndex]
    val quizFinished by viewModel.isQuizFinished.collectAsStateWithLifecycle()

    LaunchedEffect(quizFinished) {
        if (quizFinished) {
            endQuizFunction()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {

        when (currQuestion.type) {
            QuestionType.MultipleChoice -> {
                MultipleChoiceScreen(
                    question = currQuestion as Question.MultiChooseQuestion,
                    modifier = modifier,
                    viewModel = viewModel
                )
            }

            QuestionType.NumberResponse -> {
                InputNumberSection(
                    question = currQuestion as Question.NumberResponseQuestion,
                    viewModel = viewModel,
                    modifier = modifier,
                    unitStringResource = when (currQuestion.numberQuestionSubject) {
                        NumberQuestionSubject.Weight -> R.string.kilograms
                        NumberQuestionSubject.Height -> R.string.centimeters
                        NumberQuestionSubject.Age -> R.string.years_old
                    }
                )
            }

            QuestionType.SingleChoice -> {
                SingleChoiceSection(
                    question = currQuestion as Question.SingleChooseQuestion,
                    modifier = modifier,
                    viewModel = viewModel
                )
            }

            QuestionType.StringResponse -> StringResponseSection(
                question = currQuestion as Question.StringResponseQuestion,
                modifier = modifier,
                viewModel = viewModel
            )
        }
    }
}


@Composable
fun StringResponseSection(
    modifier: Modifier = Modifier,
    question: Question.StringResponseQuestion,
    viewModel: UserInitViewModel,
) {
    var value by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        QuestionTitle(question)

        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.primaryContainer,
                unfocusedContainerColor = colorScheme.secondaryContainer,
                disabledContainerColor = colorScheme.tertiaryContainer,
            ),
            shape = MaterialTheme.shapes.medium
        )
        if (value.consistsOfOnlyLetters() && !value.isEmpty()) {
            NextButton(
                alsoOnclick = {
                    viewModel.updateUserName(value)
                    Log.d("name q", "name submitted: $value")
                }, viewModel = viewModel
            )
        } else {
            Text(
                text = "You can only have letters here and you must submit a value!",
                textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
fun SingleChoiceSection(
    modifier: Modifier = Modifier,
    question: Question.SingleChooseQuestion,
    viewModel: UserInitViewModel,
) {
    //store gender
    when (question.singleChooseSubject) {
        SingleChoiceQuestionSubject.Gender -> {
            var selectedGender: Gender by rememberSaveable { mutableStateOf(Gender.Female) }
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                QuestionTitle(question)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    //gender passed in is enum!
                    items(items = question.possibleAnswerChoices) { choice ->
                        if (choice is Gender) {
                            Log.d("gender q", "choice I need to show is a Gender and it is $choice")
                            SingleSelectChoiceBubble<Gender>(
                                stringToShow = stringResource(choice.stringId), onClick = {
                                    selectedGender = choice
                                }, selectedOption = selectedGender, myOption = choice
                            )

                        }
                    }

                }
                NextButton(
                    alsoOnclick = {
                        viewModel.updateUserGender(selectedGender)
                        Log.d("gender q", "gender submitted: ${selectedGender.name}")

                    }, viewModel = viewModel
                )
            }
        }

        SingleChoiceQuestionSubject.Notifications -> {

            val context = LocalContext.current

            val notifPermissionsLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    onNotifPermissionsGranted(
                        context,
                        UserSettingsRepository.hourFlow.value,
                        UserSettingsRepository.minuteFlow.value
                    )
                } else {
                    //denied
                    Toast.makeText(
                        context,
                        "You will not receive notifications! \n You can change this in settings later",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            val permissionGranted = ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!permissionGranted) {
                var selectedOption: YesOrNoResponse by rememberSaveable {
                    mutableStateOf(
                        YesOrNoResponse.Yes
                    )
                }
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    QuestionTitle(question)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        //gender passed in is enum!
                        items(items = question.possibleAnswerChoices) { choice ->
                            if (choice is YesOrNoResponse) {
                                SingleSelectChoiceBubble<YesOrNoResponse>(
                                    stringToShow = stringResource(choice.stringID), onClick = {
                                        selectedOption = choice
                                    }, selectedOption = selectedOption, myOption = choice
                                )
                            }

                        }
                    }


                    NextButton(
                        alsoOnclick = {
                            notifPermissionsLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                            viewModel.updateUserNotifPermissionsAsked(true)
//                            if (selectedOption == YesOrNoResponse.Yes) {
//                                onNotifPermissionsGranted()
//                            }

                        }, viewModel = viewModel
                    )
                }
            } else {
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "For some reason, you already gave permissions to send notifications. You can remove this permission once set-up is done, in settings.",
                        style = typography.displayMedium
                    )
                    Image(
                        painter = painterResource(R.drawable.transparent_jim),
                        contentDescription = "Jim"
                    )
                    NextButton(
                        alsoOnclick = {
                            viewModel.updateUserNotifPermissionsAsked(true)
                        },

                        viewModel = viewModel
                    )
                }
            }
        }

        SingleChoiceQuestionSubject.ActivityLevel -> {
            var selectedLevel: ActivityLevel by rememberSaveable { mutableStateOf(ActivityLevel.Sedentary) }
            Column(
                modifier = modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                QuestionTitle(question)
                Text("Exercise is 15-30 minutes of elevated heart rate activity, \n intense exercise is 45-120 minutes of elevated heart rate activity, \n and very intense exercise is more than 2 hours of elevate heart rate activity.")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    //gender passed in is enum!
                    items(items = question.possibleAnswerChoices) { choice ->
                        if (choice is ActivityLevel) {
                            Log.d("gender q", "choice I need to show is a Gender and it is $choice")
                            SingleSelectChoiceBubble<ActivityLevel>(
                                stringToShow = choice.mainName, onClick = {
                                    selectedLevel = choice
                                }, selectedOption = selectedLevel, myOption = choice
                            )

                        }
                    }

                }
                NextButton(
                    alsoOnclick = {
                        viewModel.updateUserActivityLevel(selectedLevel)
                        Log.d("gender q", "gender submitted: ${selectedLevel.name}")

                    }, viewModel = viewModel
                )
            }
        }
    }
}


fun onNotifPermissionsGranted(context: Context, notifHour: Int, notifMinute: Int) {
    //context is not application
    NotifHandler.registerLoggingChannelWithSystem(context.applicationContext)
    NotifScheduler.scheduleDailyNotif(
        context = context.applicationContext, hour = notifHour, minute = notifMinute
    )
    Toast.makeText(context, "Notifications are now enabled", Toast.LENGTH_SHORT).show()
}


@Composable
fun InputNumberSection(
    question: Question.NumberResponseQuestion,
    viewModel: UserInitViewModel,
    @StringRes unitStringResource: Int,
    modifier: Modifier = Modifier
) {
    var textValue by rememberSaveable { mutableStateOf("") }
    var number by rememberSaveable { mutableFloatStateOf(0.0f) }
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QuestionTitle(question)
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textValue, singleLine = true, colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                ), onValueChange = { newVal ->
                    textValue = newVal
                }, keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ), placeholder = { Text("0.0") })
            Text(
                text = stringResource(unitStringResource)
            )


        }
        if (textValue.consistsOfOnlyDigits() && !textValue.isEmpty()) {
            NextButton(
                alsoOnclick = {
                    number = textValue.toFloatOrNull() ?: 0.0f
                    textValue = ""
                    when (question.numberQuestionSubject) {
                        NumberQuestionSubject.Weight -> {
                            viewModel.updateUserWeight(number)
                            Log.d("weight q", "weight submitted: $number")
                        }

                        NumberQuestionSubject.Height -> {
                            Log.d("height q", "height submitted: $number")
                            viewModel.updateUserHeight(number)
                        }

                        NumberQuestionSubject.Age -> {
                            Log.d("age q", "age submitted: $number")
                            viewModel.updateUserAge(number.toInt())
                        }
                    }
                }, viewModel = viewModel
            )
        } else {
            Text(
                text = "You can have only digits here and you must submit a value!",
                textAlign = TextAlign.Center
            )
        }

    }
}


@Composable
fun <T> SingleSelectChoiceBubble(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    stringToShow: String,
    selectedOption: T,
    myOption: T
) {
    val selected = (selectedOption == myOption)

    if (myOption !is ActivityLevel) {
        Button(
            shape = RoundedCornerShape(36.dp), modifier = modifier, onClick = {
                onClick()
            }, colors = ButtonDefaults.buttonColors(
                containerColor = if (selected) {
                    colorScheme.inversePrimary
                } else {
                    colorScheme.primary
                }
            )
        ) {
            Text(
                text = stringToShow
            )
        }
    }else{
        Button(
            shape = RoundedCornerShape(36.dp), modifier = modifier, onClick = {
                onClick()
            }, colors = ButtonDefaults.buttonColors(
                containerColor = if (selected) {
                    colorScheme.inversePrimary
                } else {
                    colorScheme.primary
                }
            )
        ) {
            Column() {
                Text(
                    text = myOption.mainName,
                    style = typography.labelMedium
                )
                Text(
                    text = myOption.desc,
                    style = typography.labelSmall
                )
            }
        }
    }
}


@Composable
fun MultiSelectChoiceBubble(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    stringToShow: String,
    initialSelected: Boolean
) {
    var selected by rememberSaveable { mutableStateOf(initialSelected) }


    Button(
        shape = RoundedCornerShape(36.dp), modifier = modifier, onClick = {
            selected = !selected
            onClick()
        }, colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) {
                colorScheme.inversePrimary
            } else {
                colorScheme.primary
            }
        )
    ) {
        Text(
            text = stringToShow
        )
    }
}


@Composable
fun QuestionTitle(question: Question, modifier: Modifier = Modifier) {

    Text(
        text = question.questionText,
        style = typography.headlineSmall,
        textAlign = TextAlign.Center,
        modifier = modifier
    )

}

@Composable
fun MultipleChoiceScreen(
    question: Question.MultiChooseQuestion,
    viewModel: UserInitViewModel,
    modifier: Modifier = Modifier,

    ) {

    val goals: MutableList<FitnessGoal> = rememberSaveable { mutableListOf() }
    val possibleGoals: List<FitnessGoal>? = question.possibleGoalChoices

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        QuestionTitle(question)
        Spacer(
            modifier = Modifier.height(70.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .padding(8.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (possibleGoals != null) {
                items(count = possibleGoals.size) { index ->
                    val currentItem = possibleGoals[index]
                    MultiSelectChoiceBubble(
                        onClick = {
                            if (goals.contains(currentItem)) {
                                //found
                                goals.remove(currentItem)
                            } else {
                                //not found
                                goals.add(currentItem)
                            }
                        }, stringToShow = currentItem.message, initialSelected = false
                    )
                }
            }

        }
        NextButton(
            alsoOnclick = {
                Log.d("goals q", "goals submitted were $goals")
                viewModel.updateUserGoals(goals)

            }, viewModel = viewModel
        )
    }


}


@Composable
fun NextButton(
    alsoOnclick: () -> Unit, modifier: Modifier = Modifier, viewModel: UserInitViewModel
) {
    Button(
        onClick = {
            Log.d("nextQuiz", "Next button was clicked")
            alsoOnclick()
            if (!viewModel.quizHandler.nextQuestion()) {//nextQuestion returns true if there is a next question, false if not. Also increments current index if possible
                Log.d("navigation", "no next question! will try to move on")
                viewModel.onQuizFinished()
            }
            Log.d("navigation", "got past end quiz function call")

            Log.d("nextQuiz", "also on click executed")
        }, modifier = modifier
    ) {
        Row {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.next_button)
            )
            Text(
                text = stringResource(R.string.next_button)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    GymAppIATheme {
        QuizScreen(
            modifier = Modifier.fillMaxSize(),
            userInitViewModel = viewModel(),
            endQuizFunction = {})
    }
}