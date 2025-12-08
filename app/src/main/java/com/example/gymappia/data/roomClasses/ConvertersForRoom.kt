package com.example.gymappia.data.roomClasses

import androidx.room.TypeConverter
import com.example.gymappia.data.roomClasses.MealType
import com.example.gymappia.data.NutrimentsInServing
import com.example.gymappia.model.Food
import com.example.gymappia.model.Workout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.TemporalAccessor


class ConvertersForRoom {

    private val gson = Gson()
    @TypeConverter
    fun fromLocalDateToString(date: LocalDate?):String?{
        return date?.toString()
    }

    @TypeConverter
    fun fromStringToLocalDate(string: String?): LocalDate?{
        return string.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?):Long?{
        return dateTime?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun fromLongToLocalDateTime(given:Long?): LocalDateTime?{
        if(given==null){
            return null
        }
        return Instant.ofEpochMilli(given).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }


    @TypeConverter
    fun fromMealTypeToString(meal: MealType?):String?{
        return meal?.name
    }

    @TypeConverter
    fun fromStringToMealType(str: String?): MealType?{
        return str?.let { MealType.valueOf(it) }
    }

    @TypeConverter
    fun fromStringToNutrimentsInServing(str:String?): NutrimentsInServing?{
        if(str==null){
            return null
        }
        val type = object : TypeToken<NutrimentsInServing>() {}.type
        return gson.fromJson(str,type)

    }

    @TypeConverter
    fun fromNutrimentsInServingToString(nutrimentsInServing: NutrimentsInServing?):String?{
        return nutrimentsInServing.let { gson.toJson(nutrimentsInServing) }
    }

    @TypeConverter
    fun fromListOfFoodToString(given:List<Food>?):String?{
        return given.let {
            gson.toJson(given)
        }
    }

    @TypeConverter
    fun fromStringToListOfFood(given:String?):List<Food>?{
        if(given==null){
            return null
        }
        val type = object : TypeToken<List<Food>>() {}.type
        return gson.fromJson(given, type)
    }

    @TypeConverter
    fun fromListOfWorkoutToString(given:List<Workout>?):String?{
        return given.let {
            gson.toJson(given)
        }
    }

    @TypeConverter
    fun fromStringToListOfWorkout(given:String?):List<Workout>?{
        if(given==null){
            return null
        }
        val type = object : TypeToken<List<Workout>>() {}.type
        return gson.fromJson(given, type)
    }


}