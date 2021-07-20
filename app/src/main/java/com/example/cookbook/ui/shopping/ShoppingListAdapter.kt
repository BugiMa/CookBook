package com.example.cookbook.ui.shopping

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.model.ProductModel
import java.util.*

class ShoppingListAdapter(
        var context: Activity,
        var products: ArrayList<ProductModel>,
        var viewModel: ShoppingListViewModel
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView: View = LayoutInflater.from(context).inflate(R.layout.list_item_shopping, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product: ProductModel = products[position]
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.textView.text = product.toString()
        viewHolder.checkBox.isChecked = product.isChecked

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                holder.textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.textView.paintFlags = holder.textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            viewModel.switchItemCheckedAt(position)
        }
        holder.textView.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Product?")
            builder.setMessage("Do you really want to delete this product?")

            builder.setPositiveButton("Yes") { _, _ ->
                viewModel.removeItemAt(position)
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.show()

            true
        }
    }

    override fun getItemCount(): Int {
        return products.count()
    }

    internal inner class RecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.shopping_item_textView)
        var checkBox: CheckBox = itemView.findViewById(R.id.shopping_item_checkBox)
    }
}