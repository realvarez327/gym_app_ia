package com.example.gymappia.data

import com.example.gymappia.model.Food
import com.example.gymappia.model.Workout

class DataModelConverters {
    fun FoodEntityToFood(given: FoodEntity):Food{
        val created = Food(
            foodName = given.foodName,
            calsPer = given.nutrimentsInServing.energyKcal,
            protein = given.nutrimentsInServing.protein,
            fat = given.nutrimentsInServing.fat,
            servingSize = given.servingSize,
            carbs = given.nutrimentsInServing.carbs,
            sugar = given.nutrimentsInServing.sugar,
            orderInDay = given.orderInDay,
            mealType = given.mealType,
            day = given.dayOfConsumption,
            imageUrl = given.imageUrl
        )
        return created
    }

    fun FoodToFoodEntity(given:Food): FoodEntity{
        val created = FoodEntity(
            orderInDay =given.orderInDay,
            mealType = given.mealType,
            servingSize = given.servingSize,
            foodName = given.foodName,
            nutrimentsInServing = NutrimentsInServing(
                energyKcal = given.calsPer,
                fat = given.fat,
                protein = given.protein,
                sugar = given.sugar,
                carbs = given.carbs
            ),
            imageUrl = given.imageUrl,
            dayOfConsumption = given.day
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
            orderInDay = given.workoutOrderInDay,
            parentDay = given.dayOfWorkout
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