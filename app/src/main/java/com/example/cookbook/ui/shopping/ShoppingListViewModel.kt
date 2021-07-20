package com.example.cookbook.ui.shopping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookbook.model.ProductModel

class ShoppingListViewModel : ViewModel() {

    private var productLiveData: MutableLiveData<ArrayList<ProductModel>> = MutableLiveData<ArrayList<ProductModel>>()
    private var productArrayList: ArrayList<ProductModel> = ArrayList()

    fun getProducts(): MutableLiveData<ArrayList<ProductModel>> {
        return productLiveData
    }

    fun addItem(product: String, quantity: Float, unit: String, ) {
        val item = ProductModel( product, quantity, unit, false)
        if (!productArrayList.containsProductWithUnit(item)) {
            productArrayList.add(item)
        }
        productLiveData.updateValue()
    }

    fun removeItemAt(index: Int) {
        productArrayList.removeAt(index)
        productLiveData.updateValue()
    }
    fun removeCheckedItems() {
        val products = ArrayList<ProductModel>()
        for (item in productArrayList) {
            if (item.isChecked)
                products.add(item)
        }
        productArrayList.removeAll(products)
        productLiveData.updateValue()
    }
    fun removeAllItems() {
        productArrayList.removeAll(productArrayList)
        productLiveData.updateValue()
    }

    fun switchItemCheckedAt(index: Int) {
        productArrayList[index].isChecked = !productArrayList[index].isChecked
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

    private fun MutableLiveData<ArrayList<ProductModel>>.updateValue() {
        this.value = productArrayList
    }
}
