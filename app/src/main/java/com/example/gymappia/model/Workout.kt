package com.example.gymappia.model

import java.time.LocalDate
import java.time.LocalDateTime


data class Workout (

    var workoutName:String = "",
    var repetitions:Int = 1,
    var parentDay: LocalDateTime,
    var setNumber:Int,
    var weightUsed:Float

)