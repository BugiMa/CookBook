package com.example.cookbook.model

data class RecipeSearchModel(
    val number: Int,
    val offset: Int,
    val results: ArrayList<RecipeModel>,
    val totalResults: Int
)