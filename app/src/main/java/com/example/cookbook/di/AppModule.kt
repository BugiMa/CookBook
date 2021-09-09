package com.example.cookbook.di

import android.content.Context
import androidx.room.Room
import com.example.cookbook.api.SpoonacularApi
import com.example.cookbook.db.CookBookDatabase
import com.example.cookbook.util.Constants.Companion.BASE_URL
import com.example.cookbook.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLoremPicsumApi(): SpoonacularApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(SpoonacularApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCookBookDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        CookBookDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideShoppingListDao(
        database: CookBookDatabase
    ) = database.shoppingListDao()

    @Singleton
    @Provides
    fun provideRecipeDao(
        database: CookBookDatabase
    ) = database.recipeDao()
}