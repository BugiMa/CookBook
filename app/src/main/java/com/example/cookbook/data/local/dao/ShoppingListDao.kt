package com.example.cookbook.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cookbook.data.local.ProductEntity

@Dao
interface ShoppingListDao {

    @Insert
    suspend fun insert(product: ProductEntity)
    @Update
    suspend fun update(product: ProductEntity)
    @Delete
    suspend fun delete(product: ProductEntity)

    @Query("SELECT * FROM products WHERE( name = :name AND unit = :unit)")
    fun getProductByNameAndUnit(name: String, unit: String): ProductEntity

    @Query("SELECT * FROM products")
    fun all(): LiveData<List<ProductEntity>>

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}