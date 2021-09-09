package com.example.cookbook.di

import com.example.cookbook.api.SpoonacularApi
import com.example.cookbook.data.local.dao.RecipeDao
import com.example.cookbook.data.local.dao.ShoppingListDao
import com.example.cookbook.repo.RecipeRepository
import com.example.cookbook.repo.ShoppingListRepository
import com.example.cookbook.repo.SpoonacularRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideSpoonacularRepository(
        api: SpoonacularApi
    ) = SpoonacularRepository(api)

    @ViewModelScoped
    @Provides
    fun provideRecipeRepository(
        dao: RecipeDao
    ) = RecipeRepository(dao)

    @ViewModelScoped
    @Provides
    fun provideShoppingListRepository(
        dao: ShoppingListDao
    ) = ShoppingListRepository(dao)
}