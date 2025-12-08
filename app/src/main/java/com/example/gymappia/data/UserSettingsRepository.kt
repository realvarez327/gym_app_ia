package com.example.gymappia.data

import android.content.Context
import android.content.SharedPreferences

import androidx.core.content.edit

import com.example.gymappia.model.FitnessGoal
import com.example.gymappia.model.Gender
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.collections.mutableListOf

private const val SETTINGS_PREF_NAME = "userPreferences"
private const val GOALS_KEY = "goals"
private const val NAME_KEY = "name"
private const val NOTIF_HOUR_KEY = "notifHour"
private const val NOTIF_MINUTE_KEY = "notifMinute"
private const val PROTEIN_KEY = "protein"
private const val FAT_KEY = "fat"
private const val SUGAR_KEY = "sugar"
private const val CARBS_KEY = "carbs"
private const val AGE_KEY = "age"
private const val GENDER_KEY = "gender"
private const val WEIGHT_KEY = "weight"
private const val HEIGHT_KEY = "height"
private const val CALORIES_KEY = "calories"
object UserSettingsRepository {

    fun init(context: Context){
        sharedPreferences  = context.applicationContext.getSharedPreferences(SETTINGS_PREF_NAME,
            Context.MODE_PRIVATE)
        _nameFlow.value = loadName()
        _genderFlow.value = loadGender()
        _goalsFlow.value = loadGoals()
        _hourFlow.value = loadHour()
        _minuteFlow.value = loadMinute()
        _heightFlow.value = loadHeight()
        _ageFlow.value = loadAge()
        _weightFlow.value = loadWeight()
        _dailyCaloriesFlow.value = loadCalories()
        _dailyProteinFlow.value = loadProtein()
        _dailySugarFlow.value = loadDailySugar()
        _dailyFatFlow.value = loadDailyFat()
        _dailyCarbsFlow.value = loadDailyCarbs()
    }
    private val gson = Gson()
    private lateinit var sharedPreferences : SharedPreferences

    private val _nameFlow = MutableStateFlow("Unknown")
    val nameFlow: StateFlow<String> = _nameFlow

    fun changeName(name:String){
        sharedPreferences.edit {
            putString(NAME_KEY, name)
        }
        _nameFlow.value = name
    }

    fun loadName():String{
        return sharedPreferences.getString(NAME_KEY, "Unknown")?:"Unknown"
    }

    private val _genderFlow = MutableStateFlow(Gender.Female)
    val genderFlow: StateFlow<Gender> = _genderFlow

    fun saveGender(gender:Gender){
        val jsonVersion = gson.toJson(gender)
        sharedPreferences.edit {
            putString(GENDER_KEY,jsonVersion)
        }
        _genderFlow.value = gender
    }

    fun loadGender():Gender{
        val jsonVersion = sharedPreferences.getString(GENDER_KEY,gson.toJson(Gender.Female))
        val gender = gson.fromJson(jsonVersion, Gender::class.java)
        return gender
    }


    private val _goalsFlow = MutableStateFlow(listOf<FitnessGoal>())
    val goalsFlow: StateFlow<List<FitnessGoal>> = _goalsFlow

    fun saveGoals(goals:List<FitnessGoal>){
        val jsonVer = gson.toJson(goals)
        sharedPreferences.edit {
            putString(GOALS_KEY,jsonVer)
        }
        _goalsFlow.value = goals
    }

    fun loadGoals(): List<FitnessGoal>{
        val defaultIfNone = "[]"
        val jsonVersion = sharedPreferences.getString(GOALS_KEY,defaultIfNone)?:defaultIfNone
        val type = object : TypeToken<List<FitnessGoal>>() {}.type
        return gson.fromJson(jsonVersion,type)
    }

    fun updateGoals(newGoals: List<FitnessGoal>){
        saveGoals(newGoals)
        _goalsFlow.value = newGoals
    }

    private val _hourFlow = MutableStateFlow(-1)
    val hourFlow : StateFlow<Int> = _hourFlow

    fun putHour(newHour:Int){
        sharedPreferences.edit {
            putInt(NOTIF_HOUR_KEY,newHour)
        }
        _hourFlow.value = newHour
    }

    fun loadHour():Int{
        return sharedPreferences.getInt(NOTIF_HOUR_KEY,-1)
    }

    private val _minuteFlow = MutableStateFlow(0)
    val minuteFlow: StateFlow<Int> = _minuteFlow

    fun putMinute(newMinute:Int){
        sharedPreferences.edit{
            putInt(NOTIF_MINUTE_KEY,newMinute)
        }
        _minuteFlow.value = newMinute
    }

    fun loadMinute():Int{
        return sharedPreferences.getInt(NOTIF_MINUTE_KEY,0)
    }

    private val _heightFlow = MutableStateFlow(0.0f)
    val heightFlow: StateFlow<Float> = _heightFlow

    fun putHeight(height: Float){
        sharedPreferences.edit{
            putFloat(HEIGHT_KEY,height)
        }
        _heightFlow.value = height
    }
    fun loadHeight():Float{
        return sharedPreferences.getFloat(HEIGHT_KEY,0.0f)
    }

    private val _ageFlow = MutableStateFlow(0)
    val ageFlow: StateFlow<Int> = _ageFlow

    fun putAge(ageToPut:Int){
        sharedPreferences.edit{
            putInt(AGE_KEY,ageToPut)
        }
        _ageFlow.value = ageToPut
    }
    fun loadAge():Int{
        return sharedPreferences.getInt(AGE_KEY,0)
    }

    private val _weightFlow = MutableStateFlow(0.0f)
    val weightFlow: StateFlow<Float> = _weightFlow

    fun putWeight(weight: Float){
        sharedPreferences.edit{
            putFloat(WEIGHT_KEY,weight)
        }
        _weightFlow.value = weight
    }
    fun loadWeight():Float{
        return sharedPreferences.getFloat(WEIGHT_KEY,0.0f)
    }

    private val _dailyCaloriesFlow = MutableStateFlow(0)
    val dailyCaloriesFlow: StateFlow<Int> = _dailyCaloriesFlow

    fun putDailyCalories(cals:Int){
        sharedPreferences.edit {
            putInt(CALORIES_KEY,cals)
        }
        _dailyCaloriesFlow.value = cals
    }

    fun loadCalories():Int{
        return sharedPreferences.getInt(CALORIES_KEY,0)
    }

    private val _dailyProteinFlow = MutableStateFlow(0)
    val dailyProteinFlow:StateFlow<Int> =_dailyProteinFlow

    fun loadProtein():Int{
       return sharedPreferences.getInt(PROTEIN_KEY, 0)
    }

    fun putProtein(given:Int){
        sharedPreferences.edit {
            putInt(PROTEIN_KEY,given)
        }
        _dailyProteinFlow.value = given
    }

    private val _dailySugarFlow =MutableStateFlow(0)
    val dailySugarFlow:StateFlow<Int> = _dailySugarFlow

    fun loadDailySugar():Int{
        return sharedPreferences.getInt(SUGAR_KEY,0)
    }

    fun putDailySugar(given:Int){
        sharedPreferences.edit{
            putInt(SUGAR_KEY,given)
        }
        _dailySugarFlow.value=given
    }


    private val _dailyFatFlow = MutableStateFlow(0)
    val dailyFatFlow = _dailyFatFlow
    fun loadDailyFat():Int {
        return sharedPreferences.getInt(FAT_KEY, 0)
    }

    fun putDailyFat(given:Int){
        sharedPreferences.edit {
            putInt(FAT_KEY,given)
        }
        _dailyFatFlow.value = given
    }

    private val _dailyCarbsFlow = MutableStateFlow(0)
    val dailyCarbsFlow = _dailyCarbsFlow

    fun loadDailyCarbs():Int {
        return sharedPreferences.getInt(CARBS_KEY, 0)
    }

    fun putDailyCarbs(given:Int){
        sharedPreferences.edit{
            putInt(CARBS_KEY, given)
        }
        _dailyCarbsFlow.value = given
    }
}