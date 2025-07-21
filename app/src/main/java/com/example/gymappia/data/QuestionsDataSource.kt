package com.example.gymappia.data

import com.example.gymappia.model.FitnessGoal
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
        ),//remember to make it so that if you choose lose weight, you cant also choose gain weight.. somehow
        //make a choice class?
        Question.SingleChooseQuestion(
            questionText = "Alrighty, this next part will impact the calculations. What is your gender?",
            possibleAnswerChoices = listOf(
                "Female",
                "Male"
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
            questionText = "And finally, how old are you?",
            numberQuestionSubject = NumberQuestionSubject.Age
        )
    )

}