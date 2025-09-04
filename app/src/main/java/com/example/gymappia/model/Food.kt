package com.example.gymappia.model

sealed class Food(
    open val foodName: String,
    open val calsPer100g: Int,
    open val proteinPer100g: Int,
    open val fiberPer100g: Int,
    open val fatPer100g: Int,
    open var servingSize: Double,
    open val userLogged: Boolean
) {


    class UserGivenFood(
        val foodNameGiven: String,
        val calsPer100gGiven: Int,
        val proteinPer100gGiven: Int,
        val fiberPer100gGiven: Int,
        val fatPer100gGiven: Int,
        var servingSizeGiven: Double

    ) : Food(
        foodName = foodNameGiven,
        calsPer100g = calsPer100gGiven,
        proteinPer100g = proteinPer100gGiven,
        fiberPer100g = fiberPer100gGiven,
        fatPer100g = fatPer100gGiven,
        userLogged = true,
        servingSize = servingSizeGiven

    )

}


