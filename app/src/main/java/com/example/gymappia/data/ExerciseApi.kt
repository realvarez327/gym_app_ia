package com.example.gymappia.data

import retrofit2.http.GET

interface ExerciseApi {
    @GET("exercises/search?search=")
    fun getExcercisesBySearch()
}