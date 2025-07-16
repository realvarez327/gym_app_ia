package com.example.gymappia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.gymappia.model.QuestionType
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = AppViewModel(),
    questions: List<Question>
) {
    //todo when i learn coroutines, add determinate linear progress indicator
    val currQuestion: Question = questions[0] //todo add functionality
    Column(modifier = modifier) {
        Text(
            text = currQuestion.questionText,

            )
        when (currQuestion.type) {
            QuestionType.MultipleChoice -> MultipleChoiceLayout(question = currQuestion as Question.MultiChooseQuestion, modifier = modifier)
            QuestionType.NumberResponse -> InputNumberSection(userNumber = viewModel.userWeight)
            QuestionType.SingleChoice -> TODO()
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

@Composable
fun SingleChoiceSection(
    modifier: Modifier = Modifier,
    question: Question.SingleChooseQuestion
){
    var selectedOption: String? = null

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = question.possibleAnswerChoices){
                choice -> ChoiceOptionBubble(text = choice, onclick = {selectedOption = choice}, bgColor = Color(1))
        }

    }
}



//this is probably the worst code ever but hey it exists now
@Composable
fun InputNumberSection(//todo make it possible to add specific units
    userNumber:Int,
    modifier: Modifier = Modifier
){
    var number = userNumber
    Column {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = userNumber.toString(),
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
        }
        Button(
            onClick = { AppViewModel().updateUserWeight(number)},
            modifier = modifier.padding(4.dp),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text(
                text = stringResource(R.string.next_button)
            )
        }
    }

}

@Composable
fun ChoiceOptionBubble(text:String, onclick:()->Unit, bgColor: Color, modifier: Modifier = Modifier){
    Card(
        shape = RoundedCornerShape(36.dp),
        modifier = modifier.background(color = bgColor)
    ){
        Row (horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top){
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
fun MultipleChoiceLayout(question: Question.MultiChooseQuestion, modifier: Modifier = Modifier){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = question.possibleAnswerChoices){
            choice -> ChoiceOptionBubble(choice, onclick = {}, bgColor = Color(1))
        }

    }


}
/*
*  items(items = DataSource.topics){ topic ->
            TopicGridItem( topic = topic)
        }*/