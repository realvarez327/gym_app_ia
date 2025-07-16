package com.example.gymappia.model

enum class Gender{
    Female, Male
}

class User (
    var usersName:String,
    var weight: Int,
    var goals: List<String>,
    var gender: Gender
    ){

}