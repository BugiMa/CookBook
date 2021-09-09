package com.example.cookbook.repo

import com.example.cookbook.data.local.dao.ShoppingListDao
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val shoppingListDao: ShoppingListDao
    ){



}