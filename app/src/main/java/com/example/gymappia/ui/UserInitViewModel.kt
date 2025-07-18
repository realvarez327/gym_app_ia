package com.example.gymappia.ui

import androidx.lifecycle.ViewModel
import com.example.gymappia.model.FitnessGoal
import com.example.gymappia.model.UserInitUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserInitViewModel : ViewModel() {
    private val _uiInitState = MutableStateFlow(UserInitUiState())

    val uiInitState: StateFlow<UserInitUiState> = _uiInitState.asStateFlow()


    fun updateUserAge(newAge:Int){
        _uiInitState.update { currentState->
            currentState.copy(
                userAge = newAge
            )
        }
    }
    fun updateUserWeight(newWeight:Int){
        _uiInitState.update { currentState ->
            currentState.copy(
                userWeight = newWeight
            )
        }
    }
    fun updateUserHeight(newHeight:Int){
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

    fun updateUserGender(newGender: Int){
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