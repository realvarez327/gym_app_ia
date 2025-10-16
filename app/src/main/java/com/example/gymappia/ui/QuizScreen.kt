package com.example.gymappia.ui

import android.util.Log
import androidx.annotation.StringRes
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.example.gymappia.model.QuestionType
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.gymappia.data.QuestionsDataSource
import com.example.gymappia.model.FitnessGoal
import com.example.gymappia.model.NumberQuestionSubject
import com.example.gymappia.model.QuizHandler
import com.example.gymappia.model.SingleChoiceQuestionSubject
import com.example.gymappia.ui.theme.GymAppIATheme




lateinit var quizHandler: QuizHandler 
@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    userInitViewModel: UserInitViewModel,
    quizHandlerGiven: QuizHandler
) {
    quizHandler = quizHandlerGiven
    val questions = QuestionsDataSource.userStartQuestions
    val viewModel: UserInitViewModel = userInitViewModel
    //todo when i learn coroutines, add determinate linear progress indicator
    val currIndex: Int = quizHandler.currentIndex.value
    var currQuestion: Question = questions[currIndex]//todo check if this can be val
    Column(modifier = modifier.fillMaxSize()) {

        when (currQuestion.type) {
            QuestionType.MultipleChoice -> {
                MultipleChoiceScreen(
                    question = currQuestion as Question.MultiChooseQuestion,
                    modifier = modifier,
                    viewModel = viewModel
                )
            }

            QuestionType.NumberResponse -> InputNumberSection(
                question = currQuestion as Question.NumberResponseQuestion,
                viewModel = viewModel,
                modifier = modifier,
                unitStringResource = when (currQuestion.numberQuestionSubject) {
                    NumberQuestionSubject.Weight -> R.string.kilograms
                    NumberQuestionSubject.Height -> R.string.centimeters
                    NumberQuestionSubject.Age -> R.string.years_old
               }
            )

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
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(color = colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        QuestionTitle(question)

        OutlinedTextField(
            value = value,
            onValueChange = {value = it},
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.primaryContainer,
                unfocusedContainerColor = colorScheme.secondaryContainer,
                disabledContainerColor = colorScheme.tertiaryContainer,
            ),
            shape = MaterialTheme.shapes.medium
        )

        NextButton(alsoOnclick = {
            viewModel.updateUserName(value)
            Log.d("name q", "name submitted: $value")
        })
    }

}

@Composable
fun SingleChoiceSection(
    modifier: Modifier = Modifier,
    question: Question.SingleChooseQuestion,
    viewModel: UserInitViewModel,
) {

    QuestionTitle(question)
    //store string resource id of choice
    @StringRes var selectedOption: Int by rememberSaveable { mutableIntStateOf(0) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = question.possibleAnswerChoices) { choice ->
            if(choice is Int) {
                Log.d("gender q", "one choice i need to show is ${stringResource(choice)}")
            }else{
                Log.d("gender q", "choice was not int")
            }
            if(choice is Int) {
                ChoiceBubble(
                    stringToShow = stringResource(choice),
                    onClick = { selectedOption = choice }
                )
            }
        }

    }

    NextButton(alsoOnclick = {
        when (question.singleChooseSubject) {
            SingleChoiceQuestionSubject.Gender -> viewModel.updateUserGender(selectedOption)
        }
        Log.d("gender q", "gender submitted: $selectedOption")
//        if(quizHandler.onLastQuestion()){
//            quizHandler.endQuizFunction()
//        }
    })
}


@Composable
fun InputNumberSection(//todo make it possible to dropdown specific units
    question: Question.NumberResponseQuestion,
    viewModel: UserInitViewModel,
    @StringRes unitStringResource: Int,
    modifier: Modifier = Modifier
) {
    var number by rememberSaveable { mutableDoubleStateOf(0.0) }
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
                value = number.toString(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                ),
                onValueChange = {
                    number = it.toDouble()
                }
            )
            Text(
                text = stringResource(unitStringResource)
            )

            NextButton(alsoOnclick = {
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
            })
        }

    }
}


@Composable
fun ChoiceBubble(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    stringToShow: String
) {
    Button(
        shape = RoundedCornerShape(36.dp),
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            text = stringToShow
        )
    }
}

@Composable
fun GoalChoiceOptionBubble(
    goal: FitnessGoal,
    onclick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ChoiceBubble(
        onClick = onclick,
        stringToShow = goal.message,
        modifier = modifier
    )
}


@Composable
fun QuestionTitle(question: Question, modifier: Modifier = Modifier){

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

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

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
                    GoalChoiceOptionBubble(
                        goal = currentItem,
                        onclick = {
                            if (goals.contains(currentItem)) {
                                //found
                                goals.remove(currentItem)
                            } else {
                                //not found
                                goals.add(currentItem)
                            }
                        }
                    )
                }
            }

        }
        NextButton(
            alsoOnclick = {
                Log.d("goals q","goals submitted were $goals")
                viewModel.updateUserGoals(goals)
                if(quizHandler.onLastQuestion()){
                    quizHandler.endQuizFunction()
                }
            }
        )
    }


}


@Composable
fun NextButton(alsoOnclick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = {
            Log.d("nextQuiz", "Next button was clicked")

            if(quizHandler.nextQuestion()==-1) {
                Log.d("navigation", "no next question! will try to move on")
                quizHandler.endQuizFunction()
                Log.d("navigation", "got past end quiz function call")

            }
            alsoOnclick()
            Log.d("nextQuiz", "also on click executed")
        },
        modifier = modifier
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
            quizHandlerGiven = QuizHandler(endQuizFunction = {Log.e( "quiz Info", "quiz ended")}, externalNavController = rememberNavController()),
        )
    }
}