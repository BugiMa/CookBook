package com.example.cookbook.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.data.local.ProductEntity
import com.example.cookbook.databinding.FragmentShoppingListBinding
import com.example.cookbook.ui.adapter.ShoppingListAdapter
import com.example.cookbook.ui.viewModel.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingListFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val adapter = ShoppingListAdapter(
            itemClickCallback = {
                viewModel.checkProduct(it)
            },
            itemLongClickCallback = {
                deleteProductDialog(it)
            }
        )

        val recyclerView: RecyclerView = binding.shoppingRecycleView
        recyclerView.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(requireContext())
            this.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }

        viewModel.getProducts().observe(viewLifecycleOwner, {
            adapter.setData(it)
        })

        binding.addProductFab.setOnClickListener {
            addProductDialog()
        }

        return binding.root
    }

    private fun addProductDialog() {

        val nameInput = EditText(context)
        nameInput.inputType = InputType.TYPE_CLASS_TEXT
        nameInput.hint = ("Product")
        nameInput.filters += InputFilter.LengthFilter(24)

        val amountInput = EditText(context)
        amountInput.inputType = InputType.TYPE_CLASS_NUMBER
        amountInput.hint = ("Amount")
        amountInput.filters += InputFilter.LengthFilter(4)

        val unitInput = EditText(context)
        amountInput.inputType = InputType.TYPE_CLASS_TEXT
        amountInput.hint = ("Unit")
        amountInput.filters += InputFilter.LengthFilter(6)

        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(nameInput)
        linearLayout.addView(amountInput)
        linearLayout.addView(unitInput)

        val builder = AlertDialog.Builder(context)
            .setTitle("Add New Product")
            .setView(linearLayout)
            .setPositiveButton("Add") { _, _ ->
                insertNewProductToDatabase(
                    nameInput.text.toString(),
                    amountOneIfEmpty(amountInput.text.toString()),
                    unitBlankIfEmpty(unitInput.text.toString()))
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()

        builder.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
        nameInput.addTextChangedListener {
            builder.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = nameInput.text.toString().isNotEmpty()
        }
    }

    private fun insertNewProductToDatabase(name: String, amount: String, unit: String) {
        if (name.isNotEmpty() && unit.isNotEmpty()) {
            val newProduct = ProductEntity(  name, amount.toFloat(), unit, false)
            viewModel.addProduct(newProduct)
            Toast.makeText(requireContext(), "Product added successfully.", Toast.LENGTH_LONG).show()
        }
    }

    private fun amountOneIfEmpty (amount: String): String {
        return if (amount.isNotEmpty())
            amount else "1"
    }
    private fun unitBlankIfEmpty (unit: String): String {
        return if (unit.isNotEmpty())
            unit else ""
    }

    private fun deleteProductDialog(product: ProductEntity) {

        AlertDialog.Builder(context)
            .setTitle("Do You really want to delete this product?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteProduct(product)
                Toast.makeText(requireContext(), "Product deleted.", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel()}
            .show()
    }
}