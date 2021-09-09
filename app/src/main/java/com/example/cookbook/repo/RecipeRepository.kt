package com.example.cookbook.repo

import androidx.lifecycle.LiveData
import com.example.cookbook.data.local.RecipeEntity
import com.example.cookbook.data.local.dao.RecipeDao
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao
    ){

    fun all(): LiveData<List<RecipeEntity>> {
        return recipeDao.all()
    }

    suspend fun exist(id: Int): Boolean {
        return recipeDao.exists(id)
    }

    suspend fun favorite(recipeEntity: RecipeEntity) {
        if (recipeDao.exists(recipeEntity.id)) {
            recipeDao.delete(recipeEntity)
        } else {
            recipeDao.insert(recipeEntity)
        }
    }

}