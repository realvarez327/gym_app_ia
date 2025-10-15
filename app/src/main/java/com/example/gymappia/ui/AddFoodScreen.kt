package com.example.gymappia.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymappia.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(modifier: Modifier = Modifier) {
    val textFieldState: TextFieldState = rememberTextFieldState()


    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.add_food_search),
            style = MaterialTheme.typography.headlineMedium
        )
        AddItemSearchBar(modifier, onSearch = {}, hint = stringResource(R.string.add_food_search))//todo add search function with api, go through model

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemSearchBar(modifier: Modifier = Modifier, onSearch: (String)->Unit, hint:String ){
    var searchQuery by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorScheme.primaryContainer,
            unfocusedContainerColor = colorScheme.secondaryContainer,
            disabledContainerColor = colorScheme.tertiaryContainer,
        ),
        shape = MaterialTheme.shapes.medium,
        label = {
            Row {

                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search)
                )
                Text(text = hint)
            }},
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}

@Composable
fun AddExerciseScreen(modifier: Modifier = Modifier) {


    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.add_exercise_search),
            style = MaterialTheme.typography.headlineMedium
        )
        AddItemSearchBar(modifier, onSearch = {}, hint = stringResource(R.string.add_exercise_search))//todo add search function with api, go through model

    }
}

@Preview(showBackground = true)
@Composable
fun AddFoodScreenPreview(modifier: Modifier = Modifier) {
    AddFoodScreen()
}

@Preview(showBackground = true)
@Composable
fun AddExerciseScreenPreview(modifier: Modifier = Modifier) {
    AddExerciseScreen()
}