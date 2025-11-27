package com.example.gymappia.data

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

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
}