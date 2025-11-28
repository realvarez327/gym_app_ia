package com.example.gymappia.ui

import android.app.Activity

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import coil3.compose.AsyncImage
import com.example.gymappia.R
import com.example.gymappia.data.FoodApiClient
import com.example.gymappia.data.FoodBarcodeResponse
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(modifier: Modifier = Modifier) {
    val textFieldState: TextFieldState = rememberTextFieldState()
    val currentActivity = LocalActivity.current
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.add_food_search),
            style = MaterialTheme.typography.headlineMedium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            AddItemSearchBar(
                modifier,
                onSearch = { searchFoodByQuery()},
                hint = stringResource(R.string.add_food_search)
            )

            IconButton(
                onClick = {searchFoodsByBarcode(currentActivity, scope) },
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
    }
}

fun searchFoodByQuery(query:String, scope: CoroutineScope){
    var response:Food
    scope.launch {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemSearchBar(modifier: Modifier = Modifier, onSearch: (String) -> Unit, hint: String) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    Row{
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
fun AddExerciseScreen(modifier: Modifier = Modifier) {


    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.add_exercise_search),
            style = MaterialTheme.typography.headlineMedium
        )
        AddItemSearchBar(
            modifier,
            onSearch = {},
            hint = stringResource(R.string.add_exercise_search)
        )

    }
}


fun searchFoodsByBarcode(activity: Activity?, scope: CoroutineScope) {
    if (activity == null) {
        return
    }
    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_UPC_A
        ).build()
    val scanner = GmsBarcodeScanning.getClient(activity, options)

    scanner.startScan()
        .addOnSuccessListener { barcode ->
            val rawValue: String = barcode.rawValue ?: ""
            var response: FoodBarcodeResponse
            scope.launch {
                response = FoodApiClient.apiService.getFoodByBarcode(rawValue)

            }

        }

}

enum class ItemType {
    BarcodeFood,
    OtherFood,
    Exercise
}

@Composable
fun ItemFocusScreen(
    modifier: Modifier = Modifier,
    type: ItemType,
    barcodeResponse: FoodBarcodeResponse?
) {
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
                if (barcodeResponse != null) {
                    var textValue by rememberSaveable { mutableStateOf("") }
                    var number by rememberSaveable { mutableFloatStateOf(0.0f) }
                    AsyncImage(
                        model = barcodeResponse.image_url,
                        placeholder = painterResource(R.drawable.image_not_found),
                        contentDescription = "image not found"
                    )
                    Row(modifier = modifier.fillMaxHeight()) {
                        val calIn100 = barcodeResponse.nutrimentsPer100g.energy_kcal_100g
                        val proteinIn100 = barcodeResponse.nutrimentsPer100g.proteins_100g
                        Column(
                            modifier = modifier
                                .fillMaxSize(0.5f)
                                .clip(shape = MaterialTheme.shapes.medium)
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
                                .clip(MaterialTheme.shapes.medium)
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
                        }
                    }
                }
            }

            ItemType.OtherFood -> {
                Text("coming soon")
            }

            ItemType.Exercise -> {
                Text("also coming soon")
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
                            .clip(shape = MaterialTheme.shapes.medium)
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
                            .clip(MaterialTheme.shapes.medium)
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
