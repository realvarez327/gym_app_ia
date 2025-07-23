package com.example.gymappia.model

import com.example.gymappia.data.QuestionsDataSource

class QuizHandler(
    var currentIndex: Int = 0,
    val quizLength: Int = QuestionsDataSource.userStartQuestions.size
) {

    fun nextQuestion(): Int {
        if (currentIndex + 1 >= QuestionsDataSource.userStartQuestions.size) {
            currentIndex = -1
            return -1
        }
        currentIndex++
        return currentIndex
    }

}