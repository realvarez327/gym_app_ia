package com.example.gymappia.ui

import android.app.Activity
import android.util.Log

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.gymappia.R
import com.example.gymappia.data.ExerciseApiResponse
import com.example.gymappia.data.FoodProduct
import com.example.gymappia.data.MealType
import com.example.gymappia.data.NutrimentsInServing

import com.example.gymappia.model.AddItemViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import java.time.LocalDate

enum class AddItemScreenNames(val navName: String) {
    AddFood("add_food"),
    AddWorkout("add_workout"),
    FocusWorkout("focus_workout"),
    FocusFood("focus_food")
}


@Composable
fun AddItemNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: AddItemScreenNames,
    viewModel: AddItemViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(route = AddItemScreenNames.AddFood.navName) {
            AddFoodScreen(viewModel = viewModel)
        }
        composable(route = AddItemScreenNames.AddWorkout.navName) {
            AddExerciseScreen(viewModel = viewModel)
        }
        composable(route = AddItemScreenNames.FocusWorkout.navName) {
            WorkoutFocusScreen(viewModel = viewModel, toShow = viewModel.selectedWorkout)
        }
        composable(route = AddItemScreenNames.FocusFood.navName) {
            FoodFocusScreen(toShow = viewModel.selectedFood, addItemAddViewModel = viewModel)
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(modifier: Modifier = Modifier, viewModel: AddItemViewModel = viewModel()) {
    val currentActivity = LocalActivity.current
    AddItemNavHost(startDestination = AddItemScreenNames.AddFood)

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.add_food_search),
            style = MaterialTheme.typography.headlineMedium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            AddItemSearchBar(
                modifier,
                onSearch = { query -> viewModel.queryFoodSearch(query) },//todo
                hint = stringResource(R.string.add_food_search)
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
        LazyColumn {
            items(viewModel.foodQuerySearchResponse) { food ->
                Button(onClick = {}) {
                    AsyncImage(
                        model = food.image_url,
                        placeholder = painterResource(R.drawable.image_not_found),
                        contentDescription = food.product_name + " image"
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = food.product_name)
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemSearchBar(modifier: Modifier = Modifier, onSearch: (String) -> Unit, hint: String) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
        )
        IconButton(onClick = { onSearch(searchQuery) }) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "search"
            )
        }
    }

}

@Composable
fun AddExerciseScreen(
    modifier: Modifier = Modifier,
    viewModel: AddItemViewModel = viewModel()
) {
    AddItemNavHost(startDestination = AddItemScreenNames.AddWorkout)

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.add_exercise_search),
            style = MaterialTheme.typography.headlineMedium
        )
        AddItemSearchBar(
            modifier,
            onSearch = { query -> viewModel.exerciseSearch(query) },
            hint = stringResource(R.string.add_exercise_search)
        )

    }
}


@Composable
fun WorkoutFocusScreen(
    modifier: Modifier = Modifier,
    toShow: ExerciseApiResponse?,
    viewModel: AddItemViewModel = viewModel()
) {
    if(toShow!=null){
        var textFieldValue by rememberSaveable { mutableStateOf("") }
        var numberOfReps by rememberSaveable { mutableIntStateOf(0) }
        Column(modifier = modifier.fillMaxSize()) {
            AsyncImage(
                model = toShow.imageUrl,
                placeholder = painterResource(R.drawable.image_not_found),
                contentDescription = "image of ${toShow.name}"
            )
            Card(
                shape = shapes.medium,
                modifier = Modifier
                    .background(colorScheme.secondaryContainer)
                    .padding(8.dp)
            ) {
                Column {
                    Text("Name: ${toShow.name}")
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Reps: ")
                        TextField(
                            value = textFieldValue,
                            onValueChange = {
                                textFieldValue = it
                                numberOfReps = it.toIntOrNull() ?: 0
                            }
                        )
                    }

                    IconButton(onClick = {
                        viewModel.insertWorkout(
                            name = toShow.name,
                            reps = numberOfReps,
                            day = LocalDate.now(),
                            order = 2//todo add functionality
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "add workout"
                        )
                    }
                }
            }

        }
    }
}


fun searchFoodsByBarcode(activity: Activity?, viewModel: AddItemViewModel): FoodProduct? {
    if (activity == null) {
        return null
    }
    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_UPC_A
        ).build()
    val scanner = GmsBarcodeScanning.getClient(activity, options)
    var response: FoodProduct? = null
    scanner.startScan()
        .addOnSuccessListener { barcode ->
            val rawValue: String = barcode.rawValue ?: ""
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
    BarcodeFood,
    OtherFood,
    Exercise
}

@Composable
fun FoodFocusScreen(
    modifier: Modifier = Modifier,
    toShow: FoodProduct?,
    addItemAddViewModel: AddItemViewModel = viewModel()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        if (toShow != null) {
            var textValue by rememberSaveable { mutableStateOf("") }
            var number by rememberSaveable { mutableFloatStateOf(0.0f) }
            val calIn100 = toShow.nutrimentsPer100g.energy_kcal_100g
            val proteinIn100 = toShow.nutrimentsPer100g.proteins_100g
            val fatIn100 = toShow.nutrimentsPer100g.fat_100g
            val carbsIn100 = toShow.nutrimentsPer100g.carbohydrates_100g
            val sugarIn100 = toShow.nutrimentsPer100g.sugars_100g
            Text(
                text = toShow.product_name,
                style = MaterialTheme.typography.headlineMedium,
            )
            AsyncImage(
                model = toShow.image_url,
                placeholder = painterResource(R.drawable.image_not_found),
                contentDescription = "image not found"
            )
            Row(modifier = modifier.fillMaxHeight()) {

                Column(
                    modifier = modifier
                        .fillMaxSize(0.5f)
                        .clip(shape = shapes.medium)
                        .padding(8.dp)
                        .background(color = colorScheme.secondaryContainer)
                ) {
                    Text("Per 100g")
                    Text("Calories = $calIn100")
                    Text("Protein = $proteinIn100")
                }
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .clip(shapes.medium)
                        .padding(8.dp)
                        .background(color = colorScheme.primaryContainer)
                ) {
                    Row {
                        Text("For serving size: ")
                        OutlinedTextField(
                            value = textValue,
                            onValueChange = {
                                textValue = it
                                number = textValue.toFloatOrNull() ?: 100.0f
                            }
                        )
                    }
                    Text("Calories = ${(number * calIn100) / 100}")
                    Text("Protein = ${(number * proteinIn100) / 100}")
                    Text("Sugar = ${(number * sugarIn100) / 100}")
                    Text("Carbs = ${(number * carbsIn100) / 100}")
                    Text("Fat = ${(number * fatIn100) / 100}")

                }
            }
            IconButton(
                onClick = {
                    addItemAddViewModel.insertFoodFromProduct(
                        given = toShow,
                        mealType = MealType.Breakfast,//todo add functionality
                        day = LocalDate.now(),//todo add functionality
                        order = 1,//todo add functionality
                        serving = number,
                        givenNutrimentsInServing = NutrimentsInServing(
                            energyKcal = (number * calIn100) / 100,
                            fat = (number * fatIn100) / 100,
                            protein = (number * proteinIn100) / 100,
                            sugar = (number * sugarIn100) / 100,
                            carbs = (number * carbsIn100) / 100
                        ),
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add food"
                )
            }
        }

    }
}

@Composable
fun ItemFocusScreenPrev(modifier: Modifier = Modifier, type: ItemType) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "ITEM NAME",
            style = MaterialTheme.typography.headlineMedium,
        )
        when (type) {
            ItemType.BarcodeFood -> {
                Image(
                    painter = painterResource(R.drawable.image_not_found),
                    contentDescription = null,
                    modifier = modifier.size(400.dp)
                )
                Row(modifier = modifier.fillMaxHeight()) {
                    Column(
                        modifier = modifier
                            .fillMaxSize(0.5f)
                            .clip(shape = shapes.medium)
                            .padding(8.dp)
                            .background(color = colorScheme.secondaryContainer)
                    ) {
                        Text("Per 100g")
                        Text("Calories = xxx")
                        Text("Protein = xxx")
                    }
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .clip(shapes.medium)
                            .padding(8.dp)
                            .background(color = colorScheme.primaryContainer)
                    ) {
                        Row {
                            Text("For serving size: ")
                            OutlinedTextField(
                                value = "zzz",
                                onValueChange = {}
                            )
                        }
                        Text("Calories = xxx")
                        Text("Protein = xxx")
                    }
                }
            }

            ItemType.OtherFood -> TODO()
            ItemType.Exercise -> TODO()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemFocusScreenPreview() {
    ItemFocusScreenPrev(type = ItemType.BarcodeFood)
}

@Preview(showBackground = true)
@Composable
fun AddFoodScreenPreview() {
    AddFoodScreen()
}

@Preview(showBackground = true)
@Composable
fun AddExerciseScreenPreview() {
    AddExerciseScreen()
}
