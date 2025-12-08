package com.example.gymappia.data.roomClasses

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gymappia.data.roomClasses.FoodEntity
import com.example.gymappia.data.roomClasses.MealType
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods WHERE date_time_eaten =:givenDay")
    suspend fun loadDaysFoods(givenDay: LocalDateTime): List<FoodEntity>

    @Query("SELECT * FROM foods WHERE date_time_eaten=:givenDay AND parent_meal_type=:givenMealType")
    suspend fun loadFoodByMealType(givenDay: LocalDateTime, givenMealType: MealType): List<FoodEntity>

    @Insert
    suspend fun addFood(foodToAdd: FoodEntity)

    @Delete
    suspend fun deleteFood(foodToKill: FoodEntity)
}