package com.example.cookbook.repository

import com.example.cookbook.api.Keys
import com.example.cookbook.api.RetrofitSpoonacularInstance
import com.example.cookbook.model.RecipeDetailsModel
import com.example.cookbook.model.RecipeSearchModel
import retrofit2.Response

class RecipeRepository {


    suspend fun getRecipeDetails(id: Int): Response<RecipeDetailsModel> {
        val url = "recipes/$id/information?includeNutrition=false&apiKey=${Keys.apiKey()}"
        return RetrofitSpoonacularInstance.spoonacularApi.getRecipe(url)
    }

    suspend fun searchRecipes(offset: Int,
                              query: List<String> ?= null,
                              cuisine: ArrayList<String> ?= null,
                              //excludeCuisine: ArrayList<String> ?= null,
                              type: ArrayList<String> ?= null,
                              diet: String ?= null,
                              intolerances: ArrayList<String> ?= null,
                              //includeIngredient: ArrayList<String> ?= null,
                              //excludeIngredient: ArrayList<String> ?= null
                              ): Response<RecipeSearchModel> {

        var url = "recipes/complexSearch?sort=popularity&offset=$offset"

        if              (diet != null) url += "&diet=$diet"
        if             (query != null) url +=             query.joinToString("-","&query=")
        if           (cuisine != null) url +=           cuisine.joinToString(",","&cuisine=")
        //if    (excludeCuisine != null) url +=    excludeCuisine.joinToString(",","&excludeCuisine=")
        if      (intolerances != null) url +=      intolerances.joinToString(",","&intolerances=")
        if              (type != null) url +=              type.joinToString(",","&type=")
        //if (includeIngredient != null) url += includeIngredient.joinToString(",","&includeIngredients=")
        //if (excludeIngredient != null) url += excludeIngredient.joinToString(",","&excludeIngredients=")

        url += "&apiKey=${Keys.apiKey()}"
        return RetrofitSpoonacularInstance.spoonacularApi.searchRecipes(url)
    }
}