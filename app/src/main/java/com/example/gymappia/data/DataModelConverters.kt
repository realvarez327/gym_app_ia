package com.example.gymappia.data

import androidx.compose.ui.geometry.Offset

import com.example.gymappia.data.roomClasses.FoodEntity
import com.example.gymappia.data.roomClasses.WorkoutEntity
import com.example.gymappia.model.DailyMetrics
import com.example.gymappia.model.Day
import com.example.gymappia.model.Food
import com.example.gymappia.model.Workout
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class DataModelConverters {
    fun FoodEntityToFood(given: FoodEntity): Food {

        val created = Food(
            foodName = given.foodName,
            calsPer = given.caloriesInServing,
            protein = given.proteinInServing,
            fat = given.fatInServing,
            servingSize = given.servingSize,
            carbs = given.carbsInServing,
            sugar = given.sugarInServing,
            mealType = given.mealType,
            dayTime = LocalDateTime.ofEpochSecond(
                given.consumptionDateTime,
                0,
                ZoneId.systemDefault().rules.getOffset(LocalDateTime.now())
            ),
            imageUrl = given.imageUrl,
            id = given.id?:0
        )
        return created
    }

    fun FoodToFoodEntity(given: Food): FoodEntity {
        val created = FoodEntity(
            mealType = given.mealType,
            servingSize = given.servingSize,
            foodName = given.foodName,
            imageUrl = given.imageUrl,
            consumptionDateTime = given.dayTime.toEpochSecond(ZoneOffset.UTC),
            proteinInServing = given.protein,
            carbsInServing = given.carbs,
            fatInServing = given.fat,
            sugarInServing = given.sugar,
            caloriesInServing = given.calsPer,
            id=given.id
        )
        return created
    }

    fun ListOfFoodEntityToListOfFood(given: List<FoodEntity>): List<Food> {
        var toReturn: MutableList<Food> = mutableListOf()
        for (item in given) {
            toReturn.add(FoodEntityToFood(item))
        }
        return toReturn.toList()
    }

    fun WorkoutEntityToWorkout(given: WorkoutEntity): Workout {
        val toReturn = Workout(
            workoutName = given.exerciseName,
            repetitions = given.repetitions,
//            parentDay = given.dayOfWorkout,
            parentDay = LocalDateTime.ofEpochSecond(
                given.dayOfWorkout,
                0,
                ZoneId.systemDefault().rules.getOffset(LocalDateTime.now())
            ),
            setNumber = given.setNumber,
            weightUsed = given.weightUsed,
            id = given.id?:0,
        )
        return toReturn
    }

    fun WorkoutEntityListToWorkout(given: List<WorkoutEntity>): List<Workout> {
        var toReturn: MutableList<Workout> = mutableListOf()
        for (item in given) {
            toReturn.add(WorkoutEntityToWorkout(item))
        }
        return toReturn.toList()
    }

    fun WorkoutToWorkoutEntity(given: Workout): WorkoutEntity {
        return WorkoutEntity(
            exerciseName = given.workoutName,
            dayOfWorkout = given.parentDay.toEpochSecond(ZoneOffset.UTC),
            repetitions = given.repetitions,
            setNumber = given.setNumber,
            weightUsed = given.weightUsed,
            id = given.id
        )
    }

    fun ListOfWorkoutEntityToListOfWorkout(given: List<WorkoutEntity>): List<Workout> {
        var toReturn: MutableList<Workout> = mutableListOf()
        for (item in given) {
            toReturn.add(WorkoutEntityToWorkout(item))
        }
        return toReturn.toList()
    }






}