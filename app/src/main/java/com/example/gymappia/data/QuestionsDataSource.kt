package com.example.gymappia.data

import androidx.annotation.StringRes
import com.example.gymappia.R
import com.example.gymappia.model.FitnessGoal
import com.example.gymappia.model.Gender
import com.example.gymappia.model.NumberQuestionSubject
import com.example.gymappia.model.Question
import com.example.gymappia.model.SingleChoiceQuestionSubject

object QuestionsDataSource {
    val userStartQuestions = listOf(
        Question.StringResponseQuestion(
            questionText = "Hello! What is your name?"
            ),
        Question.MultiChooseQuestion(
            questionText = "Nice to meet you! What are your fitness goals?",
            possibleGoalChoices = listOf(
                FitnessGoal.LosingWeight,
                FitnessGoal.GainingWeight,
                FitnessGoal.GainingMuscle,
                FitnessGoal.KeepWeight,
                FitnessGoal.LoseFat
            )
        ),
        Question.SingleChooseQuestion(
            questionText = "Alrighty, this next part will impact the calculations. What is your gender?",
            possibleAnswerChoices = listOf(
                Gender.Female,
                Gender.Male
            ),
            singleChooseSubject = SingleChoiceQuestionSubject.Gender
        ),
        Question.NumberResponseQuestion(
            questionText = "Okay then! Now, how much do you weigh (in kilograms) ?",
            numberQuestionSubject = NumberQuestionSubject.Weight
        ),
        Question.NumberResponseQuestion(
            questionText = "How tall are you (in cm) ?",
            numberQuestionSubject = NumberQuestionSubject.Height
        ),
        Question.NumberResponseQuestion(
            questionText = "How old are you?",
            numberQuestionSubject = NumberQuestionSubject.Age
        ),
        Question.SingleChooseQuestion(
            questionText = "What is your activity level?",
            singleChooseSubject = SingleChoiceQuestionSubject.ActivityLevel,
            possibleAnswerChoices = ActivityLevel.entries.toList()

        ),
        Question.SingleChooseQuestion(
            questionText = "And finally, would you like notifications reminding you to log your data?",
            possibleAnswerChoices = listOf(YesOrNoResponse.Yes, YesOrNoResponse.No),
            singleChooseSubject = SingleChoiceQuestionSubject.Notifications
        )
    )

}

enum class ActivityLevel(val calFactor:Float, val mainName:String, val desc:String){
    Sedentary(calFactor = 1.2f, mainName = "Sedentary", desc = "Little to no exercise, desk job"),
    LightlyActive(calFactor = 1.375f, mainName = "Lightly Active", desc = "Exercise 1-3 times per week"),
    ModeratelyActive(calFactor = 1.55f, mainName = "Moderately Active", desc = "Exercise 4-5 times per week"),
    VeryActive(calFactor = 1.725f, mainName = "Very Active", desc = "Intense exercise 6-7 times per week"),
    ExtremelyActive(calFactor = 1.9f, mainName = "Extremely Active", desc = "Very intense exercise daily, or physical job")


}

enum class YesOrNoResponse (@StringRes val stringID:Int){
    Yes(stringID = R.string.yesAnswer),
    No(stringID = R.string.noAnswer)
}