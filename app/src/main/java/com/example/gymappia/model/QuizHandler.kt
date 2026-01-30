package com.example.gymappia.model

import androidx.compose.runtime.mutableIntStateOf
import com.example.gymappia.data.QuestionsDataSource
import androidx.compose.runtime.State

class QuizHandler() {
    private val _currentIndex = mutableIntStateOf(0)
    val currentIndex: State<Int> get() = _currentIndex
    private val quizLength: Int = QuestionsDataSource.userStartQuestions.size

    fun nextQuestion(): Boolean{
        if(_currentIndex.intValue+1!=quizLength){
            _currentIndex.intValue++
            return true
        }else{
            return false
        }
    }

    fun resetQuiz(){
        _currentIndex.intValue = 0
    }
}