package com.example.gymappia.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods WHERE day =:givenDay")
    fun loadDaysFoods(givenDay: LocalDate): List<FoodEntity>

    @Insert
    fun addFood(foodToAdd: FoodEntity)

    @Delete
    fun deleteFood(foodToKill: FoodEntity)
}