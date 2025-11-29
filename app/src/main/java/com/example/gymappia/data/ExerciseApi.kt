package com.example.gymappia.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ExerciseApi {
    @GET("exercises/search")
    fun getExcercisesBySearch(
        @Query("search") query:String,
        @Header("x-rapidapi-key") apiKey:String ="b13f15f8f8msheb0bcd4ffb744d0p106137jsna0d364a3bcbb",
        @Header("x-rapidapi-host")hostName:String="exercisedb-api1.p.rapidapi.com"
    ): List<ExerciseApiResponse>

}