package com.example.gymappia.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.gymappia.data.FoodApiClient
import com.example.gymappia.data.roomClasses.FoodDao
import com.example.gymappia.data.roomClasses.FoodEntity
import com.example.gymappia.data.FoodProduct
import com.example.gymappia.data.roomClasses.MealType
import com.example.gymappia.data.NutrimentsInServing
import com.example.gymappia.data.roomClasses.WorkoutDao
import com.example.gymappia.data.roomClasses.WorkoutEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneOffset

class AddItemViewModel(
    private val foodDao: FoodDao, private val workoutDao: WorkoutDao
) : ViewModel() {
    //api, search
    var foodQuerySearchResponse by mutableStateOf<List<FoodProduct>>(emptyList())
        private set


    var foodCodeSearchResponse by mutableStateOf<FoodProduct?>(null)
        private set


    var errorMessage by mutableStateOf<String?>(null)
        private set


    fun barcodeFoodSearch(
        codeScanned: String
    ) {
        Log.d("barcode scanning", "code scanned = $codeScanned .")
        searchJob?.cancel()
        foodCodeSearchResponse = null
        _jobLoading.value = true
        errorMessage = null
        searchJob = viewModelScope.launch {
            try {
                val foodResponse = FoodApiClient.apiService.getFoodByBarcode(codeScanned)
                if (foodResponse.isSuccessful) {
                    Log.d("barcode scanning", "response is successful, L52")
                    Log.d("barcode scanning", "status code = ${foodResponse.raw().code}")
                    Log.d("barcode scanning", "response = ${foodCodeSearchResponse.toString()}")
                    val body = foodResponse.body()
                    Log.d("barcode scanning", "body =  ${body.toString()}")
                    if (body != null && body.product!=null) {
                        foodCodeSearchResponse = body.product
                        Log.d(
                            "barcode scanning",
                            "food found = ${foodCodeSearchResponse.toString()}"
                        )
                    } else {
                        Log.d("barcode scanning", "body is empty or products list is empty")
                    }
                } else {
                    errorMessage = "Error! Code : ${foodResponse.code()} and body = ${
                        foodResponse.errorBody().toString()
                    }"
                    Log.d(
                        "barcode scanning", "error, Code : ${foodResponse.code()} and body = ${
                            foodResponse.errorBody().toString()
                        }"
                    )
                }

            } catch (e: Exception) {

                errorMessage = e.localizedMessage
                Log.e("barcode scanning", "exception -> ${e.localizedMessage}")
                foodCodeSearchResponse = null
            } finally {
                _jobLoading.value = false
            }
        }
    }

    private var searchJob: Job? = null

    private val _jobLoading = MutableStateFlow(false)
    val jobLoading: StateFlow<Boolean> = _jobLoading
    fun queryFoodSearch(
        query: String
    ) {
        searchJob?.cancel()
        Log.d("food search", "queryFoodSearch called with $query")
        foodQuerySearchResponse = emptyList()
        searchJob = viewModelScope.launch {
            _jobLoading.value = true
//            foodQuerySearchIsLoading = true
            try {
                val queryResponse = FoodApiClient.apiService.search(query)
                val body = queryResponse.body()
                if (queryResponse.isSuccessful && body != null) {
                    foodQuerySearchResponse = body.products ?: emptyList()
                    Log.d("query search", "prods returned = $foodQuerySearchResponse")
                } else {
                    Log.d("query search", "response error = ${queryResponse.errorBody()}")
                }
                Log.d("food prod", foodQuerySearchResponse.toString())
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("queryFoodSearch", e.message ?: "error")
                foodQuerySearchResponse = emptyList()
            } finally {
                Log.d("queryFoodSearch", "done loading")
//                foodQuerySearchIsLoading = false
                _jobLoading.value = false
            }
        }

    }


    fun clearErrorMessage() {
        errorMessage = null
    }

    //room, add
    fun insertFoodFromProduct(
        given: FoodProduct,
        mealType: MealType,
        day: LocalDateTime,
        serving: Float,
        givenNutrimentsInServing: NutrimentsInServing
    ) {
        val toInsert: FoodEntity = FoodEntity(
            mealType = mealType,
            servingSize = serving,
            foodName = given.product_name,
            imageUrl = given.image_url ?: "",
            consumptionDateTime = day.toEpochSecond(ZoneOffset.UTC),
            proteinInServing = givenNutrimentsInServing.protein,
            carbsInServing = givenNutrimentsInServing.carbs,
            fatInServing = givenNutrimentsInServing.fat,
            sugarInServing = givenNutrimentsInServing.sugar,
            caloriesInServing = givenNutrimentsInServing.energyKcal
        )
        viewModelScope.launch {
            foodDao.addFood(toInsert)
        }
    }


    fun insertWorkout(
        name: String, reps: Int, day: LocalDateTime, parentSet: Int, weight: Float
    ) {
        val toInsert: WorkoutEntity = WorkoutEntity(
            exerciseName = name,
            dayOfWorkout = day.toEpochSecond(ZoneOffset.UTC),
            repetitions = reps,
            setNumber = parentSet,
            weightUsed = weight
        )
        viewModelScope.launch {
            workoutDao.addWorkout(toInsert)
        }
    }

    //storing selected items
    var selectedFood by mutableStateOf<FoodProduct?>(null)

    var selectedWorkout by mutableStateOf<Workout?>(null)
}