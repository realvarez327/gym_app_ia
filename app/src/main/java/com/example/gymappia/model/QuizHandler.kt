package com.example.gymappia.model

import androidx.compose.runtime.mutableIntStateOf
import com.example.gymappia.data.QuestionsDataSource
import androidx.compose.runtime.State

class QuizHandler{
    private val _currentIndex = mutableIntStateOf(0)
    val currentIndex: State<Int> get() = _currentIndex
    val quizLength: Int = QuestionsDataSource.userStartQuestions.size
    fun nextQuestion():Int{
        if(_currentIndex.intValue+1< quizLength){
            //next question is in bounds
            _currentIndex.intValue += 1
            return 1
        }
        return -1
        //out of bounds, quiz end reached

    }
    fun onLastQuestion(): Boolean{
        if(_currentIndex.intValue+1==quizLength){
            return true
        }
        return false
    }

}