package com.example.cookbook.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cookbook.data.local.ProductEntity
import com.example.cookbook.data.local.RecipeEntity
import com.example.cookbook.data.local.dao.RecipeDao
import com.example.cookbook.data.local.dao.ShoppingListDao
import com.example.cookbook.util.CookBookTypeConverters

@Database(
    entities = [
        RecipeEntity::class,
        ProductEntity::class],
    version = 1,
    exportSchema = false)
@TypeConverters(CookBookTypeConverters::class)
abstract class CookBookDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun recipeDao(): RecipeDao
}