package com.example.gymappia.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey val orderInDay:Int,
    @ColumnInfo(name = "parent_meal_type") val mealType: MealType,
    @ColumnInfo(name = "serving_size") val servingSize: Float,
    @ColumnInfo(name = "food_name") val foodName: String,
    @ColumnInfo(name = "nutriments_in_serving_size") val nutrimentsInServing: NutrimentsInServing,
    @ColumnInfo(name = "image_url") val imageUrl:String?,
    @ColumnInfo(name = "day") val dayOfConsumption: LocalDate
)

enum class MealType(){
    Breakfast,
    Lunch,
    Dinner,
    Snack
}