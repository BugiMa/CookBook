package com.example.cookbook.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.data.local.ProductEntity
import com.example.cookbook.databinding.ListItemProductBinding

class ShoppingListAdapter(
    private val itemClickCallback: ((ProductEntity) -> Unit)?,
    private val itemLongClickCallback: ((ProductEntity) -> Unit)?
): RecyclerView.Adapter<ShoppingListAdapter.ProductViewHolder>() {

    private var products = emptyList<ProductEntity>()

    inner class ProductViewHolder (private val itemBinding: ListItemProductBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.root.setOnClickListener {
                itemClickCallback?.invoke(products[adapterPosition])
            }
            itemBinding.root.setOnLongClickListener {
                itemLongClickCallback?.invoke(products[adapterPosition])
                return@setOnLongClickListener true
            }
        }
        fun bind(name: String, quantity: Float, unit: String, isChecked: Boolean?) {
            if (isChecked!!) {
                itemBinding.productName.paintFlags = (itemBinding.productName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                itemBinding.productAmount.paintFlags = (itemBinding.productName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
            } else {
                itemBinding.productName.paintFlags = (itemBinding.productName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                itemBinding.productAmount.paintFlags = (itemBinding.productName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            }

            itemBinding.productName.text = name
            itemBinding.productAmount.text = "$quantity $unit"
            itemBinding.checkBox.isChecked = isChecked
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val itemBinding = ListItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product.name,
                    product.quantity,
                    product.unit,
                    product.isChecked
        )
    }

    override fun getItemCount(): Int {
    return products.size
    }

    fun setData(newData: List<ProductEntity>?) {
        if (newData != null) {
            this.products = newData
        }
        notifyDataSetChanged()
    }


}