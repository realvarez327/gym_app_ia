package com.example.gymappia.data.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gymappia.data.NutrimentsInServing
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "foods"
)
data class FoodEntity(
    @PrimaryKey(autoGenerate = true) val id:Int?=null,
    @ColumnInfo(name = "parent_meal_type") val mealType: MealType,
    @ColumnInfo(name = "serving_size") val servingSize: Float,
    @ColumnInfo(name = "food_name") val foodName: String,
    @ColumnInfo(name = "image_url") val imageUrl:String?,
    @ColumnInfo(name = "date_time_eaten") val consumptionDateTime: Long,
    @ColumnInfo(name = "protein_in_serving") val proteinInServing:Float,
    @ColumnInfo(name = "carbs_in_serving") val carbsInServing:Float,
    @ColumnInfo(name = "fat_in_serving") val fatInServing:Float,
    @ColumnInfo(name = "sugar_in_serving") val sugarInServing:Float,
    @ColumnInfo(name = "calories_in_serving") val caloriesInServing:Float



    )

enum class MealType(){
    Breakfast,
    Lunch,
    Dinner,
    Snack
}