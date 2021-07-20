package com.example.cookbook.ui.cookbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cookbook.repository.RecipeRepository

class CookBookViewModelFactory (
    private val gameRepository: RecipeRepository,
): ViewModelProvider.Factory {
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return CookBookViewModel(gameRepository) as T
    }

}