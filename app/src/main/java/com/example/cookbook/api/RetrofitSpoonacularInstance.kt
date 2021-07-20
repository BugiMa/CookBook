package com.example.cookbook.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.cookbook.utils.Constants.Companion.SPOONACULAR_BASE_URL

object RetrofitSpoonacularInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SPOONACULAR_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val spoonacularApi: SpoonacularApi by lazy {
        retrofit.create(SpoonacularApi::class.java)
    }
}