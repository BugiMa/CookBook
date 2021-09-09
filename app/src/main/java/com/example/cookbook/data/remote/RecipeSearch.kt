package com.example.cookbook.data.remote

data class RecipeSearch(
    val number: Int,
    val offset: Int,
    val results: ArrayList<Recipe>,
    val totalResults: Int
)