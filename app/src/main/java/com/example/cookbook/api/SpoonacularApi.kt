package com.example.cookbook.api

import com.example.cookbook.data.remote.RecipeDetails
import com.example.cookbook.data.remote.RecipeSearch
import com.example.cookbook.util.Constants.Companion.RECIPE_LIMIT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi {

    @GET("/recipes/{id}/information")
    suspend fun getRecipe(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean? = false,
        @Query("apiKey") apiKey: String? = Keys.apiKey()
    ): Response<RecipeDetails>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("offset")             offset: Int? = null,
        @Query("number")             number: Int? = null,
        @Query("sort")                 sort: String? = null,
        @Query("query")               query: String? = null,
        @Query("cuisine")           cuisine: String? = null,
        @Query("diet")                 diet: String? = null,
        @Query("intolerances") intolerances: String? = null,
        @Query("type")                 type: String? = null,
        @Query("apiKey")             apiKey: String? = Keys.apiKey()
    ): Response<RecipeSearch>
}