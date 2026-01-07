package com.example.gymappia.data.roomClasses

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDateTime

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods WHERE date_time_eaten BETWEEN :beginOfDay AND :endOfDay")
    suspend fun loadDaysFoods(beginOfDay:Long, endOfDay:Long): List<FoodEntity>

    @Query("SELECT * FROM foods WHERE date_time_eaten=:givenDay AND parent_meal_type=:givenMealType")
    suspend fun loadFoodByMealType(givenDay: LocalDateTime, givenMealType: MealType): List<FoodEntity>

    @Insert
    suspend fun addFood(foodToAdd: FoodEntity)

    @Delete
    suspend fun deleteFood(foodToKill: FoodEntity)

    @Query("DELETE FROM foods WHERE id=:givenID")
    suspend fun deleteFoodById(givenID:Int)

}