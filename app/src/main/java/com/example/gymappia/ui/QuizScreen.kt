package com.example.gymappia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gymappia.R
import com.example.gymappia.model.Question
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import com.example.gymappia.model.QuestionType
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymappia.model.NumberQuestionSubject


@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    questions: List<Question>
) {

    val viewModel: UserInitViewModel = viewModel()
    //todo when i learn coroutines, add determinate linear progress indicator
    val currQuestion: Question = questions[0] //todo add functionality
    Column(modifier = modifier) {
        Text(
            text = currQuestion.questionText,

            )
        when (currQuestion.type) {
            QuestionType.MultipleChoice -> {
                MultipleChoiceLayout(
                    question = currQuestion as Question.MultiChooseQuestion,
                    modifier = modifier
                )


            }

            QuestionType.NumberResponse -> InputNumberSection(
                question = currQuestion as Question.NumberResponseQuestion,
                viewModel = viewModel,
                modifier = modifier
            )

            QuestionType.SingleChoice -> SingleChoiceSection(
                question = currQuestion as Question.SingleChooseQuestion,
                modifier = modifier,
                viewModel = viewModel
            )

            QuestionType.StringResponse -> TODO()
        }
        Button(
            onClick = {},
            modifier = modifier.padding(4.dp),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text(
                text = stringResource(R.string.next_button)
            )
        }
    }
}

//this wont work, use that it is only used for gender? todo fix
@Composable
fun SingleChoiceSection(
    modifier: Modifier = Modifier,
    question: Question.SingleChooseQuestion,
    viewModel: UserInitViewModel
) {
    var selectedOption: Int by rememberSaveable { mutableIntStateOf(0) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = question.possibleAnswerChoices) { choice ->
            ChoiceOptionBubble(
                text = choice,
                onclick = { selectedOption = choice.toInt() },
                bgColor = Color(1)
            )
        }

    }

    NextButton(alsoOnclick = { viewModel.updateUserGender(selectedOption) })
}


@Composable
fun InputNumberSection(//todo make it possible to add specific units
    question: Question.NumberResponseQuestion,
    viewModel: UserInitViewModel,
    modifier: Modifier = Modifier
) {
    var number by rememberSaveable { mutableIntStateOf(0) }
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center

        ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
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
                    number = it.toInt()
                }
            )
            Text(
                text = stringResource(R.string.kilograms)
            )

            Button(
                onClick = {
                    when (question.numberQuestionSubject) {
                        NumberQuestionSubject.Weight -> viewModel.updateUserWeight(number)
                        NumberQuestionSubject.Height -> viewModel.updateUserHeight(number)
                        NumberQuestionSubject.Age -> viewModel.updateUserAge(number)
                    }
                },
                modifier = modifier
                    .padding(4.dp)
                    .weight(1f),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.next_button)
                )
            }
        }

    }
}

@Composable
fun ChoiceOptionBubble(
    text: String,
    onclick: () -> Unit,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(36.dp),
        modifier = modifier.background(color = bgColor)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            RadioButton(
                selected = false,
                onClick = onclick
            )
        }
        Row {
            Text(
                text = text
            )
        }
    }
}

@Composable
fun MultipleChoiceLayout(
    question: Question.MultiChooseQuestion,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = question.possibleAnswerChoices) { choice ->
            ChoiceOptionBubble(choice, onclick = {}, bgColor = colorScheme.secondaryContainer)
        }

    }


}

@Composable
fun NextButton(alsoOnclick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = { alsoOnclick /*todo add another function to go to next question here*/},
        modifier = modifier.background(color = colorScheme.secondaryContainer)
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
