package com.example.gymappia.data

import com.example.gymappia.data.roomClasses.FoodEntity
import com.example.gymappia.data.roomClasses.WorkoutEntity
import com.example.gymappia.model.Day
import com.example.gymappia.model.Food
import com.example.gymappia.model.Workout

class DataModelConverters {
    fun FoodEntityToFood(given: FoodEntity):Food{
        val created = Food(
            foodName = given.foodName,
            calsPer = given.caloriesInServing,
            protein = given.proteinInServing,
            fat = given.fatInServing,
            servingSize = given.servingSize,
            carbs = given.carbsInServing,
            sugar = given.sugarInServing,
            mealType = given.mealType,
            dayTime = given.consumptionDateTime,
            imageUrl = given.imageUrl
        )
        return created
    }

    fun FoodToFoodEntity(given:Food): FoodEntity {
        val created = FoodEntity(
            mealType = given.mealType,
            servingSize = given.servingSize,
            foodName = given.foodName,
            imageUrl = given.imageUrl,
            consumptionDateTime = given.dayTime,
            proteinInServing = given.protein,
            carbsInServing = given.carbs,
            fatInServing = given.fat,
            sugarInServing = given.sugar,
            caloriesInServing = given.calsPer,
        )
        return created
    }

    fun ListOfFoodEntityToListOfFood(given:List<FoodEntity>):List<Food>{
        var toReturn: MutableList<Food> = mutableListOf()
        for (item in given){
            toReturn.add(FoodEntityToFood(item))
        }
        return toReturn.toList()
    }

    fun WorkoutEntityToWorkout(given: WorkoutEntity): Workout{
        val toReturn = Workout(
            workoutName = given.exerciseName,
            repetitions = given.repetitions,
            parentDay = given.dayOfWorkout.toLocalDate()
        )
        return toReturn
    }

    fun ListOfWorkoutEntityToListOfWorkout(given:List<WorkoutEntity>):List<Workout>{
        var toReturn: MutableList<Workout> = mutableListOf()
        for (item in given){
            toReturn.add(WorkoutEntityToWorkout(item))
        }
        return toReturn.toList()
    }


}