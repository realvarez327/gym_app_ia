package com.example.gymappia.data

import com.example.gymappia.model.FitnessGoal
import com.example.gymappia.model.NumberQuestionSubject
import com.example.gymappia.model.Question
import com.example.gymappia.model.QuestionType

object QuestionsDataSource {
    val userStartQuestions = listOf<Question>(
        Question.StringResponseQuestion(
            questionText = "Hello! What is your name?"
            ),
        Question.MultiChooseQuestion(
            questionText = "Nice to meet you! What are your fitness goals?",
            possibleAnswerChoices = listOf(
                FitnessGoal.LosingWeight.name,
                FitnessGoal.GainingWeight.name,
                FitnessGoal.GainingMuscle.name,
                FitnessGoal.KeepWeight.name,
                FitnessGoal.LoseFat.name
            )
        ),//remember to make it so that if you choose lose weight, you cant also choose gain weight.. somehow
        //make a choice class?
        Question.SingleChooseQuestion(
            questionText = "Alrighty, this next part will impact the calculations. What is your gender?",
            possibleAnswerChoices = listOf(
                "Female",
                "Male"
            )
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