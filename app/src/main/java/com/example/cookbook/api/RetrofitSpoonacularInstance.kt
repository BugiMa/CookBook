package com.example.cookbook.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.cookbook.utils.Constants.Companion.SPOONACULAR_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Body

object RetrofitSpoonacularInstance {

    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val httpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor)

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SPOONACULAR_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    val spoonacularApi: SpoonacularApi by lazy {
        retrofit.create(SpoonacularApi::class.java)
    }
}