package com.example.gymappia.data


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object FoodApiClient {
    val apiService: FoodApi by lazy {
        FoodRetrofitClient.retrofit.create(FoodApi::class.java)
    }
}

object FoodRetrofitClient {
    private const val BASE_URL = "https://world.openfoodfacts.org/"
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(SharedHttpClientProvider.client)
            .build()


    }
}