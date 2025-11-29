package com.example.gymappia.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappia.data.ExerciseApiClient
import com.example.gymappia.data.ExerciseApiResponse
import com.example.gymappia.data.FoodApiClient
import com.example.gymappia.data.FoodDao
import com.example.gymappia.data.FoodEntity
import com.example.gymappia.data.FoodProduct
import com.example.gymappia.data.MealType
import com.example.gymappia.data.NutrimentsInServing
import com.example.gymappia.data.WorkoutDao
import com.example.gymappia.data.WorkoutEntity
import kotlinx.coroutines.launch
import java.time.LocalDate

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

    var exerciseSearchResponse by mutableStateOf<List<ExerciseApiResponse>>(emptyList())
        private set

    var exerciseSearchIsLoading by mutableStateOf(false)

    var errorMessage by mutableStateOf<String?>(null)
        private set


    fun barcodeFoodSearch(
        codeScanned:String
    ){
        viewModelScope.launch {
            foodCodeSearchIsLoading=true
            try{
                foodCodeSearchResponse = FoodApiClient.apiService.getFoodByBarcode(codeScanned).products?.firstOrNull()
            }catch (e: Exception){
                errorMessage = e.message
                foodCodeSearchResponse = null
            }finally {
                foodCodeSearchIsLoading = false
            }
        }
    }

    fun queryFoodSearch(
        query:String
    ){
        viewModelScope.launch {
            foodQuerySearchIsLoading=true
            try{
                foodQuerySearchResponse = FoodApiClient.apiService.search(query).products?:emptyList()
            }catch (e: Exception){
                errorMessage = e.message
                foodQuerySearchResponse = emptyList()
            }finally {
                foodQuerySearchIsLoading = false
            }
        }
    }

    fun exerciseSearch(
        query:String
    ){
        viewModelScope.launch {
            exerciseSearchIsLoading = true
            try{
                exerciseSearchResponse = ExerciseApiClient.apiService.getExcercisesBySearch(query)
            }catch (e: Exception){
                errorMessage = e.message
            }finally {
                exerciseSearchIsLoading = false
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
        day: LocalDate,
        order: Int,
        serving: Float,
        givenNutrimentsInServing: NutrimentsInServing
    ) {
        val toInsert: FoodEntity = FoodEntity(
            orderInDay = order,
            mealType = mealType,
            servingSize = serving,
            foodName = given.product_name,
            nutrimentsInServing = givenNutrimentsInServing,
            imageUrl = given.image_url?:"",
            dayOfConsumption = day
        )
        viewModelScope.launch {
            foodDao.addFood(toInsert)
        }
    }


    fun insertWorkout(
        name:String,
        reps: Int,
        day: LocalDate,
        order: Int
    ){
        val toInsert: WorkoutEntity= WorkoutEntity(
            workoutOrderInDay = order,
            exerciseName = name,
            dayOfWorkout = day,
            repetitions = reps
        )
        viewModelScope.launch {
            workoutDao.addWorkout(toInsert)
        }
    }

    //storing selected items
    var selectedFood by mutableStateOf<FoodProduct?>(null)

    var selectedWorkout by mutableStateOf<ExerciseApiResponse?>(null)
}