package com.example.gymappia.data

import okhttp3.OkHttpClient

object SharedHttpClientProvider {
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Gym IA App 1.0")
                    .build()
                chain.proceed(request)
            }
            .retryOnConnectionFailure(true)
            .build()
    }

}