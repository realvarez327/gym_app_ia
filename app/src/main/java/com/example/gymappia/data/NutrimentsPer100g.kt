package com.example.gymappia.data

import com.google.gson.annotations.SerializedName

data class NutrimentsPer100g (//todo see if same responses for liquids! need to see if 100g exists
    val carbohydrates_100g: Float,
    @SerializedName("energy-kcal_100g")
    val energy_kcal_100g:Float,
    val fat_100g: Float,
    val proteins_100g:Float,
    val sugars_100g:Float
    //todo see if we can get some of the estimated in her (fiber)

)
