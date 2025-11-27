package com.example.gymappia.ui

import androidx.lifecycle.ViewModel
import com.example.gymappia.data.UserSettingsRepository
import com.example.gymappia.model.FitnessGoal
import com.example.gymappia.model.Gender
import com.example.gymappia.model.UserInitUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

class UserInitViewModel : ViewModel() {
    private val _uiInitState = MutableStateFlow(UserInitUiState())

    val uiInitState: StateFlow<UserInitUiState> = _uiInitState.asStateFlow()

    fun updateRepo(){
        val state = _uiInitState.value
        UserSettingsRepository.updateGoals(state.goals?:listOf())
        UserSettingsRepository.changeName(state.userName)
        UserSettingsRepository.putAge(state.userAge?:0)
        UserSettingsRepository.putHeight(state.userHeight?:0.0f)
        UserSettingsRepository.putWeight(state.userWeight?:0.0f)
        UserSettingsRepository.saveGender(state.gender)
        //mifflin st. jeor formula
        if((state.userWeight!=null)
            &&(state.userHeight!=null)
            &&(state.userAge!=null)
            ){
            var cals = (10* state.userWeight!!)+(6.25*state.userHeight!!) -(5*state.userAge!!)
            when(state.gender){
                Gender.Female -> {
                    cals-=161
                }
                Gender.Male -> {
                    cals +=5
                }
            }
            UserSettingsRepository.putDailyCalories(cals.roundToInt())
        }
    }

    fun updateUserAge(newAge:Int){
        _uiInitState.update { currentState->
            currentState.copy(
                userAge = newAge
            )
        }
    }
    fun updateUserWeight(newWeight:Float){
        _uiInitState.update { currentState ->
            currentState.copy(
                userWeight = newWeight
            )
        }
    }
    fun updateUserHeight(newHeight:Float){
        _uiInitState.update { currentState ->
            currentState.copy(
                userHeight = newHeight
            )
        }
    }

    fun updateUserName(newName : String){
        _uiInitState.update { currentState ->
            currentState.copy(
                userName = newName
            )
        }
    }

    fun updateUserGender(newGender: Gender){
        _uiInitState.update { currentState ->
            currentState.copy(
                gender = newGender
            )
        }
    }

    fun updateUserGoals(newGoals : List<FitnessGoal>){
        _uiInitState.update { currentState ->
            currentState.copy(
                goals = newGoals
            )
        }
    }
}