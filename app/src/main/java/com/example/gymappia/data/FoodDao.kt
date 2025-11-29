package com.example.gymappia.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods WHERE day =:givenDay")
    suspend fun loadDaysFoods(givenDay: LocalDate): List<FoodEntity>

    @Query("SELECT * FROM foods WHERE day=:givenDay AND parent_meal_type=:givenMealType")
    suspend fun loadFoodByMealType(givenDay: LocalDate, givenMealType: MealType): List<FoodEntity>

    @Insert
    suspend fun addFood(foodToAdd: FoodEntity)

    @Delete
    suspend fun deleteFood(foodToKill: FoodEntity)
}