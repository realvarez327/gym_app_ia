package com.example.gymappia.data

data class FoodBarcodeResponse(
    val code: String,
    val image_url:String?,
    val nutrimentsPer100g:NutrimentsPer100g,
    val product_name:String,
    val product_quantity : Float,
    val product_quantity_unit:String,//todo make this enum (g, ml)
    val status:Int

)


