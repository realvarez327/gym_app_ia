package com.example.gymappia.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import kotlin.getValue

object FoodApiClient {
    val apiService: FoodApi by lazy {
        RetrofitClient.retrofit.create(FoodApi::class.java)
    }
}

object RetrofitClient{
    private const val BASE_URL = "https://world.openfoodfacts.org/api/v2/"
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .header("User-Agent", "Gym IA App 1.0")
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()


    }
}