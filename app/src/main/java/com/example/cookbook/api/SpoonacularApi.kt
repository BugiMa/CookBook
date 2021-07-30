package com.example.cookbook.api

import com.example.cookbook.model.RecipeDetailsModel
import com.example.cookbook.model.RecipeSearchModel
import retrofit2.Response
import retrofit2.http.*

interface SpoonacularApi {

    @GET("recipes/{id}/information")
    suspend fun getRecipe(
        @Path ("id") id: Int,
        @Query ("includeNutrition") includeNutrition: Boolean? = false,
        @Query ("apiKey") apiKey: String? = Keys.apiKey()
    ): Response<RecipeDetailsModel>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query ("offset")             offset: Int? = 0,
        @Query ("sort")                 sort: String? = null,
        @Query ("query")               query: String? = null,
        @Query ("cuisine")           cuisine: String? = null,
        @Query ("diet")                 diet: String? = null,
        @Query ("intolerances") intolerances: String? = null,
        @Query ("type")                 type: String? = null,
        @Query ("apiKey")             apiKey: String? = Keys.apiKey(),
        @Query ("number")             number: Int? = 100
    ): Response<RecipeSearchModel>
}