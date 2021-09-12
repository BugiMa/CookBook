package com.example.cookbook.repo

import androidx.lifecycle.LiveData
import com.example.cookbook.data.local.ProductEntity
import com.example.cookbook.data.local.dao.ShoppingListDao
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val shoppingListDao: ShoppingListDao
    ){

    suspend fun insertProduct(product: ProductEntity) {
        return shoppingListDao.insert(product)
    }
    suspend fun updateProduct(product: ProductEntity) {
        shoppingListDao.update(product)
    }
    suspend fun deleteProduct(product: ProductEntity) {
        shoppingListDao.delete(product)
    }

    fun getProductByNameAndUnit(name: String, unit: String): ProductEntity {
        return shoppingListDao.getProductByNameAndUnit(name, unit)
    }

    fun all(): LiveData<List<ProductEntity>> {
        return shoppingListDao.all()
    }
    suspend fun deleteAll() {
        shoppingListDao.deleteAll()
    }




}