package com.example.gymappia.model


import com.example.gymappia.data.roomClasses.MealType
import java.time.LocalDate
import java.time.LocalDateTime

class Food(
    val id:Int,
    val foodName: String,
    val calsPer: Float,
    val protein: Float,
    val fat: Float,
    var servingSize: Float,
    var carbs:Float,
    var sugar:Float,
    var mealType: MealType,
    var dayTime: LocalDateTime,
    val imageUrl:String?
)


