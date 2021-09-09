package com.example.cookbook.repo

import com.example.cookbook.api.SpoonacularApi
import com.example.cookbook.data.remote.RecipeDetails
import com.example.cookbook.data.remote.RecipeSearch
import com.example.cookbook.util.Constants.Companion.RECIPE_LIMIT
import retrofit2.Response

class SpoonacularRepository(
    private val api: SpoonacularApi
) {

    suspend fun getRecipeDetails(id: Int): Response<RecipeDetails> {
        return api.getRecipe(id = id)
    }

    suspend fun searchRecipes(
        offset: Int? = 0,
        number: Int? = RECIPE_LIMIT,
        query: List<String>?,
        cuisine: ArrayList<String>?,
        //excludeCuisine: ArrayList<String> ?= null,
        type: ArrayList<String>?,
        diet: String?,
        intolerances: ArrayList<String>?,
        //includeIngredient: ArrayList<String> ?= null,
        //excludeIngredient: ArrayList<String> ?= null,
        sort: String? = "popularity"

    ): Response<RecipeSearch> {
        return api.searchRecipes(
            offset,
            number,
            sort,
            query?.joinToString("-"),
            cuisine?.joinToString(","),
            diet,
            intolerances?.joinToString(","),
            type?.joinToString(","))
    }

}