package com.example.cookbook.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val name: String,
    var quantity: Float,
    val unit: String,
    var isChecked: Boolean
) {

    override fun toString(): String {

        return if (quantity % 1 == 0f)
            "$name (${quantity.toInt()} $unit)"
        else
            "$name - $quantity $unit"
    }
}