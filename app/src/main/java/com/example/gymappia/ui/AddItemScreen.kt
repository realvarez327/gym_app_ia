package com.example.gymappia.ui

import android.app.Activity
import android.util.Log

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import coil3.compose.AsyncImage
import com.example.gymappia.R
import com.example.gymappia.data.FoodProduct
import com.example.gymappia.data.NutrimentsInServing
import com.example.gymappia.data.roomClasses.MealType

import com.example.gymappia.model.AddItemViewModel
import com.example.gymappia.model.Day
import com.example.gymappia.model.Workout
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    modifier: Modifier = Modifier,
    viewModel: AddItemViewModel = viewModel(),
    navigateToFoodFocus: () -> Unit
) {
    val currentActivity = LocalActivity.current

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.add_food_search),
            style = MaterialTheme.typography.headlineMedium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            AddItemSearchBar(
                onSearch = { query ->
                    viewModel.queryFoodSearch(query)

                }, hint = stringResource(R.string.add_food_search)
            )

            IconButton(
                onClick = { searchFoodsByBarcode(currentActivity, viewModel) },
                colors = IconButtonColors(
                    containerColor = colorScheme.secondaryContainer,
                    contentColor = colorScheme.secondary,
                    disabledContainerColor = colorScheme.tertiaryContainer,
                    disabledContentColor = colorScheme.tertiary
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.sharp_barcode_scanner_24),
                    contentDescription = "search by barcode"
                )
            }
        }
        if (viewModel.foodCodeSearchIsLoading || viewModel.foodQuerySearchIsLoading) {
            LoadingScreen(modifier = modifier.weight(1f))
        }else {
            LazyColumn {
                items(viewModel.foodQuerySearchResponse) { food ->
                    Button(
                        onClick = {
                            viewModel.selectedFood = food
                            navigateToFoodFocus()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            AsyncImage(
                                model = food.image_url,
                                placeholder = painterResource(R.drawable.image_not_found),
                                contentDescription = food.product_name + " image"
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            Log.d("food prod", viewModel.foodQuerySearchResponse.toString())
                            Text(text = food.product_name)
                        }
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemSearchBar(onSearch: (String) -> Unit, hint: String) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.primaryContainer,
                unfocusedContainerColor = colorScheme.secondaryContainer,
                disabledContainerColor = colorScheme.tertiaryContainer,
            ),
            shape = shapes.medium,
            label = {
                Text(hint)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search, keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearch(searchQuery)

                }))
        IconButton(onClick = {
            onSearch(searchQuery)
            keyboardController?.hide()
        }) {
            Icon(
                imageVector = Icons.Outlined.Search, contentDescription = "search"
            )
        }
    }

}

@Composable
fun AddExerciseScreen(
    modifier: Modifier = Modifier,
    viewModel: AddItemViewModel = viewModel(),
    goBackToDay:()->Unit,
    day: LocalDateTime,
    toShow: Workout?
) {

    var userEnteredWorkoutName by rememberSaveable { mutableStateOf(toShow?.workoutName?:"")}

    var repsTextValue by rememberSaveable { mutableStateOf(toShow?.repetitions?.toString()?:"")}

    var weightUsedTextValue by rememberSaveable {mutableStateOf(toShow?.weightUsed?.toString()?:"") }

    var parentSetTextValue by rememberSaveable { mutableStateOf(toShow?.setNumber?.toString()?:"")}

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Row() {
            Text(
                text = "Workout name: "
            )
            OutlinedTextField(
                value = userEnteredWorkoutName,
                onValueChange = {userEnteredWorkoutName = it},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
        }
        Row() {
            Text(
                text = "Repetitions: "
            )
            OutlinedTextField(
                value = repsTextValue,
                onValueChange = {newVal->
                    repsTextValue = newVal
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
        }
        Row() {
            Text(
                text = "Weight used: "
            )
            OutlinedTextField(
                value = weightUsedTextValue,
                onValueChange = {newVal->
                    weightUsedTextValue = newVal
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
        }
        Row() {
            Text(
                text = "Set: "
            )
            OutlinedTextField(
                value = parentSetTextValue,
                onValueChange = {newVal->
                    parentSetTextValue = newVal
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
        }
        var canPass = true
        if(!parentSetTextValue.consistsOfOnlyDigits()){
            Text("The set must be a number!")
            canPass = false
        }

        if(!weightUsedTextValue.consistsOfOnlyDigits()){
            Text("The weight must be a number!")
            canPass = false
        }

        if(!repsTextValue.consistsOfOnlyDigits()){
            Text("The reps must be a number!")
            canPass = false
        }
        if(canPass) {
            SmallFloatingActionButton(
                onClick = {
                    Log.d("add workout", "search button clicked")

                    viewModel.insertWorkout(
                        name = userEnteredWorkoutName,
                        reps = repsTextValue.toIntOrNull()?:0,
                        day = day,
                        parentSet = parentSetTextValue.toIntOrNull()?:1,
                        weight = weightUsedTextValue.toFloatOrNull()?:0.0f
                    )
                    goBackToDay()
                },
                shape = shapes.small,
                containerColor = colorScheme.secondaryContainer,
                contentColor = colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add, contentDescription = "add workout"
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "L O A D I N G", style = MaterialTheme.typography.headlineLarge
        )
        Image(
            painter = painterResource(R.drawable.transparent_jim),
            contentDescription = "Jim",
            modifier = Modifier
                .padding(8.dp)
                .background(color = colorScheme.primaryContainer)
        )
    }
}


fun searchFoodsByBarcode(activity: Activity?, viewModel: AddItemViewModel): FoodProduct? {
    if (activity == null) {
        return null
    }
    val options = GmsBarcodeScannerOptions.Builder().setBarcodeFormats(
            Barcode.FORMAT_EAN_13, Barcode.FORMAT_EAN_8, Barcode.FORMAT_UPC_A
        ).build()
    val scanner = GmsBarcodeScanning.getClient(activity, options)
    var response: FoodProduct? = null
    scanner.startScan().addOnSuccessListener { barcode ->
            val rawValue: String = barcode.rawValue ?: ""
            Log.d("barcode search", "barcode = $rawValue")
            viewModel.barcodeFoodSearch(rawValue)
            if (viewModel.errorMessage != null) {
                response = viewModel.foodCodeSearchResponse
            } else {
                Log.d("barcode food search", "${viewModel.errorMessage}")
                viewModel.clearErrorMessage()
            }
        }
    return response
}

enum class ItemType {
    BarcodeFood, OtherFood, Exercise
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodFocusScreen(
    modifier: Modifier = Modifier,
    toShow: FoodProduct?,
    addItemAddViewModel: AddItemViewModel,
    day: LocalDateTime,
    goBackToDay:()->Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        if (toShow != null) {
//            Log.d("food product","toShow is $toShow")
//            Log.d("food product", "toShow contains: ${toShow.nutriments}")
            var textValOfServing by rememberSaveable { mutableStateOf("") }
            var numberValOfServing by rememberSaveable { mutableFloatStateOf(0.0f) }
            var selectedMeal by rememberSaveable { mutableStateOf(MealType.Breakfast) }
            val calIn100 = toShow.nutriments.energy_kcal_100g
            val proteinIn100 = toShow.nutriments.proteins_100g
            val fatIn100 = toShow.nutriments.fat_100g
            val carbsIn100 = toShow.nutriments.carbohydrates_100g
            val sugarIn100 = toShow.nutriments.sugars_100g

            val interactionSource = remember { MutableInteractionSource() }
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = toShow.product_name,
                    style = MaterialTheme.typography.headlineMedium,
                )
                AsyncImage(
                    model = toShow.product_name,
                    placeholder = painterResource(R.drawable.image_not_found),
                    contentDescription = "image of ${toShow.product_name}"
                )

                Column(modifier = Modifier.background(color = colorScheme.inversePrimary)) {
                    Row(
                        modifier = Modifier
                            .height(56.dp)
                            .background(color = colorScheme.primaryContainer)
                    ) {
                        TableCell(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) { Text("Attributes") }
                        TableCell(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text("Per ${toShow.product_quantity} ${toShow.product_quantity_unit}")
                        }
                        TableCell(
                            modifier = Modifier.weight(1f)
                        ) {
                            Row {
                                Text("Per : ")

                                BasicTextField(
                                    value = textValOfServing,
                                    onValueChange = {
                                        textValOfServing = it
                                        if(textValOfServing.consistsOfOnlyDigits()){
                                            numberValOfServing = textValOfServing.toFloatOrNull()?:0f
                                        }
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .heightIn(min = 36.dp, max =40.dp)
                                        .widthIn(min = 60.dp)
                                        .padding(0.dp),
                                    textStyle = LocalTextStyle.current
                                ){innerField->
                                    OutlinedTextFieldDefaults.DecorationBox(
                                        value = textValOfServing,
                                        innerTextField = innerField,
                                        enabled = true,
                                        singleLine = true,
                                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                                        visualTransformation = VisualTransformation.None,
                                        interactionSource = interactionSource
                                    )
                                }
                            }
                        }
                    }
                    Row(modifier = Modifier.background(color = colorScheme.primaryContainer)) {
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text("Calories")
                        }
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text(calIn100.toString())
                        }
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text(((numberValOfServing * calIn100) / 100).toString())
                        }
                    }
                    Row(modifier = Modifier.background(color = colorScheme.primaryContainer)) {
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text("Protein")
                        }
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text(proteinIn100.toString())
                        }
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text(((numberValOfServing * proteinIn100) / 100).toString())
                        }
                    }
                    Row(modifier = Modifier.background(color = colorScheme.primaryContainer)) {
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text("Carbs")
                        }
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text(carbsIn100.toString())
                        }
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text(((numberValOfServing * carbsIn100) / 100).toString())
                        }
                    }
                    Row(modifier = Modifier.background(color = colorScheme.primaryContainer)) {
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text("Fat")
                        }
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text(fatIn100.toString())
                        }
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text(((numberValOfServing * fatIn100) / 100).toString())
                        }

                    }
                    Row(modifier = Modifier.background(color = colorScheme.primaryContainer)) {
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text("Sugar")
                        }
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text(sugarIn100.toString())
                        }
                        TableCell(modifier = Modifier.weight(1f)) {
                            Text(((numberValOfServing * sugarIn100) / 100).toString())
                        }
                    }
                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = colorScheme.primaryContainer)
                        .padding(8.dp)
                ) {
                    var expanded by rememberSaveable { mutableStateOf(false) }
                    Text("Meal: $selectedMeal")
                    Box(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        IconButton(
                            onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Meal type options"
                            )
                        }

                        DropdownMenu(
                            expanded = expanded, onDismissRequest = { expanded = false }) {
                            MealType.entries.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item.name) },
                                    onClick = { selectedMeal = item })
                            }
                        }
                    }
                }
                SmallFloatingActionButton(
                    onClick = {
                        addItemAddViewModel.insertFoodFromProduct(
                            given = toShow,
                            mealType = selectedMeal,
                            day = day,//should be current day
                            serving = numberValOfServing,
                            givenNutrimentsInServing = NutrimentsInServing(
                                energyKcal = (numberValOfServing * calIn100) / 100,
                                fat = (numberValOfServing * fatIn100) / 100,
                                protein = (numberValOfServing * proteinIn100) / 100,
                                sugar = (numberValOfServing * sugarIn100) / 100,
                                carbs = (numberValOfServing * carbsIn100) / 100
                            )
                        )
                        goBackToDay()
                    },
                    shape = shapes.small,
                    containerColor = colorScheme.secondaryContainer,
                    contentColor = colorScheme.secondary
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add, contentDescription = "add food"
                    )
                }

            }


        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemFocusScreenPrev(modifier: Modifier = Modifier, type: ItemType) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        when (type) {
            ItemType.BarcodeFood -> {
                val interactionSource = remember { MutableInteractionSource() }
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ITEM NAME",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Image(
                        painter = painterResource(R.drawable.image_not_found),
                        contentDescription = null,
                        modifier = modifier.size(300.dp)
                    )
                    var textValOfServing by rememberSaveable { mutableStateOf("") }
                    var numberValOfServing by rememberSaveable { mutableFloatStateOf(0.0f) }
                    Column(modifier = Modifier.background(color = colorScheme.inversePrimary)) {
                        Row(
                            modifier = Modifier
                                .height(56.dp)
                                .background(color = colorScheme.primaryContainer)
                        ) {
                            TableCell(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) { Text("Attributes") }
                            TableCell(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) { Text("Per serving size") }
                            TableCell(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row {
                                    Text("Per : ")

                                    BasicTextField(
                                        value = textValOfServing,
                                        onValueChange = {
                                            textValOfServing = it
                                            if(textValOfServing.consistsOfOnlyDigits()){
                                                numberValOfServing = textValOfServing.toFloatOrNull()?:0f
                                            }
                                        },
                                        singleLine = true,
                                        modifier = Modifier
                                            .heightIn(min = 36.dp, max =40.dp)
                                            .widthIn(min = 60.dp)
                                            .padding(0.dp),
                                        textStyle = LocalTextStyle.current
                                    ){innerField->
                                        OutlinedTextFieldDefaults.DecorationBox(
                                            value = textValOfServing,
                                            innerTextField = innerField,
                                            enabled = true,
                                            singleLine = true,
                                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                                            visualTransformation = VisualTransformation.None,
                                            interactionSource = interactionSource
                                        )
                                    }
                                }
                            }
                        }
                        Row(modifier = Modifier.background(color = colorScheme.primaryContainer)) {
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Calories")
                            }
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Calories")
                            }
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Calories")
                            }
                        }
                        Row(modifier = Modifier.background(color = colorScheme.primaryContainer)) {
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Protein")
                            }
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Protein")
                            }
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Protein")
                            }
                        }
                        Row(modifier = Modifier.background(color = colorScheme.primaryContainer)) {
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Carbs")
                            }
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Carbs")
                            }
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Carbs")
                            }
                        }
                        Row(modifier = Modifier.background(color = colorScheme.primaryContainer)) {
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Fat")
                            }
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Fat")
                            }
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Fat")
                            }

                        }
                        Row(modifier = Modifier.background(color = colorScheme.primaryContainer)) {
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Sugar")
                            }
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Sugar")
                            }
                            TableCell(modifier = Modifier.weight(1f)) {
                                Text("Sugar")
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(color = colorScheme.primaryContainer)
                            .padding(8.dp)
                    ) {
                        var expanded by rememberSaveable { mutableStateOf(false) }
                        Text("Meal: ")
                        Box(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            IconButton(
                                onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Meal type options"
                                )
                            }

                            DropdownMenu(
                                expanded = expanded, onDismissRequest = { expanded = false }) {
                                MealType.entries.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item.name) },
                                        onClick = { })
                                }
                            }
                        }
                    }
                    SmallFloatingActionButton(
                        onClick = { },
                        shape = shapes.small,
                        containerColor = colorScheme.secondaryContainer,
                        contentColor = colorScheme.secondary
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add, contentDescription = "add food"
                        )
                    }

                }
            }

            ItemType.OtherFood -> TODO()
            ItemType.Exercise -> TODO()
        }
    }
}

@Composable
fun TableCell(
    modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .border(1.dp, color = colorScheme.inversePrimary)
            .padding(8.dp)
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun ItemFocusScreenPreview() {
    ItemFocusScreenPrev(type = ItemType.BarcodeFood)
}


@Preview(showBackground = true)
@Composable
fun AddExerciseScreenPreview() {
//    AddExerciseScreen()
}
