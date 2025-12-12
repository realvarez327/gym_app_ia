package com.example.gymappia.model

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import com.example.gymappia.data.QuestionsDataSource
import androidx.compose.runtime.State
import androidx.navigation.NavHostController

class QuizHandler() {
    private val _currentIndex = mutableIntStateOf(0)
    val currentIndex: State<Int> get() = _currentIndex
    val quizLength: Int = QuestionsDataSource.userStartQuestions.size

    fun nextQuestion(): Boolean{
        Log.d("quiz", " curr index ${currentIndex.value}")
        if(_currentIndex.intValue+1!=quizLength){
            _currentIndex.value++
            return true
        }else{
            return false
        }
    }

    fun resetQuiz(){
        _currentIndex.intValue = 0
        Log.d("quiz", "reset called")
    }




}