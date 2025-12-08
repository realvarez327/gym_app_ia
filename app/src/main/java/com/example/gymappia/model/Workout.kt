package com.example.gymappia.model

import java.time.LocalDate
import java.time.LocalDateTime


class Workout (

    var workoutName:String = "",
    var repetitions:Int = 1,
    var orderInDay:Int =1,
    var parentDay: LocalDateTime


)