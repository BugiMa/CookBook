package com.example.cookbook.ui.shopping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoppingListViewModel : ViewModel() {

    var productLiveData: MutableLiveData<ArrayList<ProductModel>> = MutableLiveData<ArrayList<ProductModel>>()
    var productArrayList: ArrayList<ProductModel> = ArrayList()

    init {
        productLiveData.value = productArrayList
    }

    fun addItem(product: String, quantity: Float, unit: String, ) {
        val item = ProductModel( product, quantity, unit, false)
        productArrayList.add(item)
    }

    fun removeChecked() {
        val products = ArrayList<ProductModel>()
        for (item in productArrayList) {
            if (item.isChecked)
                products.add(item)
        }
        productArrayList.removeAll(products)
    }

    fun remove(index: Int) {
        productArrayList.removeAt(index)
    }

    fun removeAll() {
        productArrayList.removeAll(productArrayList)
    }

    private fun changeQuantity(item: ProductModel, quantity: Float) {
        val index = productArrayList.indexOf(item)
        item.quantity = quantity
        productArrayList[index] = item
    }
}
