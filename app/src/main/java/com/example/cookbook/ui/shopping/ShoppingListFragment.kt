package com.example.cookbook.ui.shopping

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import androidx.lifecycle.Observer


class ShoppingListFragment : Fragment() {

    private lateinit var shoppingListViewModel: ShoppingListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: ShoppingListAdapter

    private lateinit var listView: ListView
    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var fabRemove: FloatingActionButton
    private val fabOpenAnimation: Animation by lazy {AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val fabCloseAnimation: Animation by lazy {AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }
    private var isFabClicked: Boolean = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shopping_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = view.findViewById(R.id.shopping_list)
        shoppingListViewModel = ViewModelProvider(this).get(ShoppingListViewModel::class.java)
        shoppingListViewModel.productLiveData.observe(requireActivity(), { productsArrayList ->
            recyclerViewAdapter = ShoppingListAdapter(requireActivity(), productsArrayList, shoppingListViewModel)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = recyclerViewAdapter
        })

        fabMain   = view.findViewById(R.id.fab_main)
        fabAdd    = view.findViewById(R.id.fab_add)
        fabRemove = view.findViewById(R.id.fab_remove)

        fabMain.setOnClickListener   {onMainFabClick()}
        fabAdd.setOnClickListener    {addItemDialog()}
        fabRemove.setOnClickListener {removeItemDialog()}

        super.onViewCreated(view, savedInstanceState)
    }

    private fun onMainFabClick() {
        setVisibility(isFabClicked)
        setAnimation(isFabClicked)
        isFabClicked = !isFabClicked
    }
    private fun setVisibility(isClicked: Boolean) {
        if (isClicked) {
            fabAdd.visibility = View.INVISIBLE
            fabRemove.visibility = View.INVISIBLE
        } else {
            fabAdd.visibility = View.VISIBLE
            fabRemove.visibility = View.VISIBLE
        }
    }
    private fun setAnimation(isClicked: Boolean) {
        if (isClicked) {
            fabAdd.startAnimation(fabCloseAnimation)
            fabRemove.startAnimation(fabCloseAnimation)
        } else {
            fabAdd.startAnimation(fabOpenAnimation)
            fabRemove.startAnimation(fabOpenAnimation)
        }
    }

    private fun addItemDialog() {

        val builder = AlertDialog.Builder(requireContext())
        val linearLayout = LinearLayout(requireContext())
        val quantityInput = textInputLayout("Quantity", requireContext(), true)
        val unitInput = textInputLayout("Unit", requireContext(), false)
        val productInput = textInputLayout("Product", requireContext(), false)

        builder.setTitle("Add New Product")
        linearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.gravity = Gravity.CENTER

        linearLayout.addView(productInput)
        linearLayout.addView(quantityInput)
        linearLayout.addView(unitInput)

        builder.setView(linearLayout)
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.setPositiveButton("Add") { _, _ ->

            val name = productInput.editText?.text.toString().toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)
            val quantity = quantityInput.editText?.text.toString().toFloat()
            val unit = unitInput.editText?.text.toString().toLowerCase(Locale.ROOT)

            shoppingListViewModel.addItem(name, quantity, unit)

            Toast.makeText(requireContext(), "Added $name", Toast.LENGTH_SHORT).show()
        }
        builder.show()

    }
    private fun removeItemDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setNeutralButton("Cancel") { _, _ -> }
        builder.setNegativeButton("Remove All") { _, _ ->
            shoppingListViewModel.removeAll()
        }
        builder.setPositiveButton("Remove Checked") { _, _ ->
            shoppingListViewModel.removeChecked()
        }
        builder.show()
    }

    private fun textInputLayout(hint: String, context: Context, isNumber: Boolean): TextInputLayout {

        val layoutParams = LinearLayout.LayoutParams(800, 160)
        layoutParams.setMargins(16, 24, 16, 16)

        val textInputLayout = TextInputLayout(context, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox)
        textInputLayout.hint = hint
        textInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
        textInputLayout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
        textInputLayout.setBoxCornerRadii(5F, 5F, 5F, 5F)

        val textInputEditText = TextInputEditText(textInputLayout.context)
        if (isNumber) textInputEditText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
        textInputEditText.layoutParams = layoutParams
        textInputLayout.addView(textInputEditText, layoutParams)

        return textInputLayout
    }
}


