package com.example.cookbook.data.remote

data class Recipe (
    val id: Int,
    val image: String,
    val imageType: String,
    val title: String,
    var isFavorite: Boolean? = false
)