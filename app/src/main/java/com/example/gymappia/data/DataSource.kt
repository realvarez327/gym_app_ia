package com.example.gymappia.data

import com.example.gymappia.model.Question

object DataSource {
    val questions = listOf<Question>(
        Question.StringResponseQuestion(
            questionText = "First off, what is your name?"
        ),
        Question.MultiChooseQuestion(
            questionText = "Nice to meet you! What are your fitness goals?",
            possibleAnswerChoices = listOf(
                "Losing weight",
                "Gaining muscle",
                "Gaining muscle",
                "Maintaining weight",
                "Just a healthier lifestyle"
            )
        ),
        Question.SingleChooseQuestion(
            questionText = "Great! Now, what is your gender ?",
            possibleAnswerChoices = listOf(
                "Female",
                "Male",
                "Other/Prefer not to say (will use formula for males)"// todo check if i can actually support this, if not, oof
            )
        ),
        Question.NumberResponseQuestion(
            questionText = "Thanks for the info. Next, how much do you weigh?"
        )
    )

}