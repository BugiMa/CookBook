package com.example.cookbook.ui.shopping

data class ProductModel(val name: String, var quantity: Float, val unit: String, var isChecked: Boolean) {

    override fun toString(): String {

        return if (quantity % 1 == 0f)
            "$name (${quantity.toInt()} $unit)"
        else
            "$name - $quantity $unit"
    }
}