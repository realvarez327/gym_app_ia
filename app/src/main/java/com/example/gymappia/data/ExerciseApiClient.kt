package com.example.gymappia.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ExerciseApiClient{
    val apiService: ExerciseApi by lazy {
        ExerciseRetrofitClient.retrofit.create(ExerciseApi::class.java)
    }
}

object ExerciseRetrofitClient{
    private const val BASE_URL = "https://exercisedb-api1.p.rapidapi.com/"
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(SharedHttpClientProvider.client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}