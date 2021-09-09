package com.example.cookbook.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cookbook.data.local.RecipeEntity

@Dao
interface RecipeDao {

    @Insert
    suspend fun insert(recipe: RecipeEntity)
    @Delete
    suspend fun delete(recipe: RecipeEntity)

    @Query ("SELECT * FROM recipes")
    fun all(): LiveData<List<RecipeEntity>>
    @Query("SELECT EXISTS (SELECT 1 FROM recipes WHERE id = :id)")
    suspend fun exists(id: Int): Boolean
}