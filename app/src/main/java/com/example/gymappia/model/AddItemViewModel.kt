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
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class AddItemViewModel (
    private val foodDao: FoodDao,
    private val workoutDao: WorkoutDao
): ViewModel(){
    //api, search
    var foodQuerySearchResponse by mutableStateOf<List<FoodProduct>>(emptyList())
        private set

    var foodQuerySearchIsLoading by mutableStateOf(false)

    var foodCodeSearchResponse by mutableStateOf<FoodProduct?>(null)
        private set

    var foodCodeSearchIsLoading by mutableStateOf(false)

    var errorMessage by mutableStateOf<String?>(null)
        private set


    fun barcodeFoodSearch(
        codeScanned:String
    ){
        searchJob?.cancel()
        foodCodeSearchResponse = null
        searchJob = viewModelScope.launch {
            foodCodeSearchIsLoading=true
            try{
                foodCodeSearchResponse = FoodApiClient.apiService.getFoodByBarcode(codeScanned).products?.firstOrNull()
            }catch (e: Exception){
                Log.e("barcode scanning",e.message.toString())
                errorMessage = e.message
                foodCodeSearchResponse = null
            }finally {
                foodCodeSearchIsLoading = false
            }
        }
    }

    private var searchJob: Job? = null

    fun queryFoodSearch(
        query:String
    ){
        searchJob?.cancel()
        Log.d("food search", "queryFoodSearch called with $query")
        searchJob = viewModelScope.launch {
            foodQuerySearchResponse = emptyList()
            foodQuerySearchIsLoading=true
            try{
                foodQuerySearchResponse = FoodApiClient.apiService.search(query).products?:emptyList()
                Log.d("food prod", foodQuerySearchResponse.toString())
            }catch (e: Exception){
                errorMessage = e.message
                Log.e("queryFoodSearch",e.message?:"error")
                foodQuerySearchResponse = emptyList()
            }finally {
                Log.d("queryFoodSearch", "done loading")
                foodQuerySearchIsLoading = false
            }
        }

    }



    fun clearErrorMessage(){
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
            imageUrl = given.image_url?:"",
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
        name:String,
        reps: Int,
        day: LocalDateTime,
        parentSet:Int,
        weight: Float
    ){
        val toInsert: WorkoutEntity= WorkoutEntity(
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