package com.example.cookbook.ui.shopping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoppingListViewModel : ViewModel() {

    var productLiveData: MutableLiveData<ArrayList<ProductModel>> = MutableLiveData<ArrayList<ProductModel>>()
    var productArrayList: ArrayList<ProductModel> = ArrayList()

    fun addItem(product: String, quantity: Float, unit: String, ) {
        val item = ProductModel( product, quantity, unit, false)
        if (!productArrayList.containsProductWithUnit(item)) {
            productArrayList.add(item)
        }
        productLiveData.value = productArrayList
    }

    fun removeItemAt(index: Int) {
        productArrayList.removeAt(index)
        productLiveData.value = productArrayList
    }
    fun removeCheckedItems() {
        val products = ArrayList<ProductModel>()
        for (item in productArrayList) {
            if (item.isChecked)
                products.add(item)
        }
        productArrayList.removeAll(products)
        productLiveData.value = productArrayList
    }
    fun removeAllItems() {
        productArrayList.removeAll(productArrayList)
        productLiveData.value = productArrayList
    }

    private fun ArrayList<ProductModel>.containsProductWithUnit(item: ProductModel): Boolean {
        for (product in this) {
            if (product.name == item.name && product.unit == item.unit) {
                changeQuantity(product, item.quantity)
                return true
            }
        }
        return false
    }
    private fun changeQuantity(product: ProductModel, quantity: Float) {
        val index = productArrayList.indexOf(product)
        product.quantity = product.quantity + quantity
        productArrayList[index] = product
    }
}
