package com.example.cookbook.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cookbook.data.remote.RecipeDetails

@Entity(tableName="recipes")
data class RecipeEntity(
    @PrimaryKey
    val id: Int,
    val image: String,
    val title: String,
    val ingredients: ArrayList<String>,
    val steps: ArrayList<String>,
    val summary: String,
    val isFavorite: Boolean? = false
){}