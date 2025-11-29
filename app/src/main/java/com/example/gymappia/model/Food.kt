package com.example.gymappia.model


import com.example.gymappia.data.MealType
import java.time.LocalDate

class Food(
    val foodName: String,
    val calsPer: Float,
    val protein: Float,
    val fat: Float,
    var servingSize: Float,
    var carbs:Float,
    var sugar:Float,
    var orderInDay: Int,
    var mealType: MealType,
    var day: LocalDate,
    val imageUrl:String?
)


