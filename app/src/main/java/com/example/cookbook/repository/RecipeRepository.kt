package com.example.cookbook.repository

import com.example.cookbook.api.RetrofitSpoonacularInstance
import com.example.cookbook.model.RecipeDetailsModel
import com.example.cookbook.model.RecipeSearchModel
import retrofit2.Response


class RecipeRepository {

    suspend fun getRecipeDetails(id: Int): Response<RecipeDetailsModel> {
        return RetrofitSpoonacularInstance.spoonacularApi.getRecipe(id = id)
    }

    suspend fun searchRecipes(
        offset: Int? = 0,
        query: List<String>?,
        cuisine: ArrayList<String>?,
        //excludeCuisine: ArrayList<String> ?= null,
        type: ArrayList<String>?,
        diet: String?,
        intolerances: ArrayList<String>?,
        //includeIngredient: ArrayList<String> ?= null,
        //excludeIngredient: ArrayList<String> ?= null,
        sort: String? = "popularity"

        ): Response<RecipeSearchModel> {
        return RetrofitSpoonacularInstance.spoonacularApi.searchRecipes(offset,
                                                                        sort,
                                                                        query?.joinToString("-"),
                                                                        cuisine?.joinToString(","),
                                                                        diet,
                                                                        intolerances?.joinToString(","),
                                                                        type?.joinToString(","))
    }
}