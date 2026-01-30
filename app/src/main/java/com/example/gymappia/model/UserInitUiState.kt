package com.example.gymappia.model

import com.example.gymappia.data.ActivityLevel

//all these are var becuase they could be changed in creation process by back arrow
data class UserInitUiState (
    var userWeight:Float? = null,
    var userName:String = "Sample",
    var goals: List<FitnessGoal>? = null,
    var gender:Gender = Gender.Female,
    var userAge:Int? = null,
    var userHeight:Float? = null,
    var askedUserForNotifRights:Boolean?=true,
    var activityLevel: ActivityLevel = ActivityLevel.Sedentary
)