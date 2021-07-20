package com.example.cookbook.api


import com.example.cookbook.model.RecipeDetailsModel
import com.example.cookbook.model.RecipeSearchModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface SpoonacularApi {
    @GET
    suspend fun getRecipe(
        @Url url: String
    ): Response<RecipeDetailsModel>

    @GET
    suspend fun searchRecipes(
        @Url url: String
    ): Response<RecipeSearchModel>
}