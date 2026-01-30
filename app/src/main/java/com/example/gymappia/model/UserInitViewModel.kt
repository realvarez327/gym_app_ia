package com.example.gymappia.model

import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.example.gymappia.data.ActivityLevel
import com.example.gymappia.data.UserSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

class UserInitViewModel (): ViewModel() {
    private val _uiInitState = MutableStateFlow(UserInitUiState())

    val uiInitState: StateFlow<UserInitUiState> = _uiInitState.asStateFlow()

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished: StateFlow<Boolean> = _isQuizFinished.asStateFlow()

    fun restartQuiz(){
        _isQuizFinished.value = false
    }

    fun onQuizFinished(){
        updateRepo()
        _isQuizFinished.value = true
        quizHandler.resetQuiz()
    }

    val quizHandler = QuizHandler()

    fun updateRepo(){
        val state = _uiInitState.value
        val goals = state.goals
        UserSettingsRepository.updateGoals(goals?:listOf())
        UserSettingsRepository.changeName(state.userName)
        UserSettingsRepository.putAge(state.userAge?:0)
        UserSettingsRepository.putHeight(state.userHeight?:0.0f)
        UserSettingsRepository.putWeight(state.userWeight?:0.0f)
        UserSettingsRepository.saveGender(state.gender)
        UserSettingsRepository.changeIfAskedForNotifs(state.askedUserForNotifRights?:false)
        //mifflin st. jeor formula
        if((state.userWeight!=null)
            &&(state.userHeight!=null)
            &&(state.userAge!=null)
            ){
            var cals = (10* state.userWeight!!)+(6.25*state.userHeight!!) -(5*state.userAge!!)//currently only bmr, add question for activity level!
            when(state.gender){
                Gender.Female -> {
                    cals-=161
                }
                Gender.Male -> {
                    cals +=5
                }
            }

            //assuming that weight is kg
            cals*=state.activityLevel.calFactor
            if(goals!=null){
                if(goals.contains(FitnessGoal.LosingWeight)&&!(goals.contains(FitnessGoal.GainingWeight)||goals.contains(
                        FitnessGoal.KeepWeight))){
                    // if goals has losing weight and no user mistakes (keeping or gaining also pressed)
                    cals-=500
                }else if(goals.contains(FitnessGoal.GainingWeight)&&!(goals.contains(FitnessGoal.LosingWeight)||goals.contains(
                        FitnessGoal.KeepWeight))){
                    //sources say 350-500
                    cals+=400
                }
            }
            UserSettingsRepository.putDailyCalories(cals.roundToInt())
            UserSettingsRepository.putProtein((state.userWeight!!*2.2f).roundToInt())
            UserSettingsRepository.putDailySugar(30)
            UserSettingsRepository.putDailyCarbs(((0.325*cals)/4).roundToInt())
            var fatToPut = 0.3*cals
            if(state.userAge!!<3){
                fatToPut=0.35*cals
            }
            UserSettingsRepository.putDailyFat(fatToPut.roundToInt())
        }
    }

    fun updateUserActivityLevel(newLevel: ActivityLevel){
        _uiInitState.update { currentState->
            currentState.copy(
                activityLevel = newLevel
            )
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

    fun updateUserNotifPermissionsAsked(askedOrNot: Boolean){

        _uiInitState.update { currentState->
            currentState.copy(
                askedUserForNotifRights = askedOrNot

            )
        }
    }
}