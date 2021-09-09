package com.example.cookbook.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.cookbook.R
import com.example.cookbook.databinding.DialogFragmentSearchBinding
import com.example.cookbook.databinding.FragmentRecipeListBinding
import com.example.cookbook.ui.viewModel.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout


class SearchDialogFragment(
    private val searchCallback: (() -> Unit)?
) : BottomSheetDialogFragment() {

    private val rotateOpenAnimation: Animation by lazy {AnimationUtils.loadAnimation(this.context, R.anim.rotate_open_anim)}
    private val rotateCloseAnimation: Animation by lazy {AnimationUtils.loadAnimation(this.context, R.anim.rotate_close_anim)}

    private var _binding: DialogFragmentSearchBinding? = null
    private val binding get() = _binding!!

    private  var query: List<String>? = null
    private var cuisines: ArrayList<ArrayList<Any>>? = null
    private var mealTypes: ArrayList<ArrayList<Any>>? = null
    private var isCuisineExpanded  = false
    private var isMealTypeExpanded = false

    private lateinit var viewModel: SharedViewModel

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Setting Cuisine Expandable Chip Group Card
        val cuisineNames = resources.getStringArray(R.array.array_cuisine)
        binding.chipGroupCuisine.createChipGroup(cuisineNames, 0)

        // Setting Meal Type Expandable Chip Group Card
        val mealTypeNames = resources.getStringArray(R.array.array_meal_type)
        binding.chipGroupMealType.createChipGroup(mealTypeNames, 1)

        // Setting Cards OnClickListeners
        binding.expandableCardCuisine.setOnClickListener {
            if (isMealTypeExpanded) { // Collapsing other card if expanded
                expandedCardBehavior(
                    binding.expandableCardMealType,
                    binding.expandableChipGroupMealType,
                    binding.imageViewArrowMealType,
                    isMealTypeExpanded)
                isMealTypeExpanded = !isMealTypeExpanded
            }
            expandedCardBehavior(
                binding.expandableCardCuisine,
                binding.expandableChipGroupCuisine,
                binding.imageViewArrowCuisine,
                isCuisineExpanded)
            isCuisineExpanded = !isCuisineExpanded
        }
        binding.expandableCardMealType.setOnClickListener {
            if (isCuisineExpanded) { // Collapsing other card if expanded
                expandedCardBehavior(
                    binding.expandableCardCuisine,
                    binding.expandableChipGroupCuisine,
                    binding.imageViewArrowCuisine,
                    isCuisineExpanded)
                isCuisineExpanded = !isCuisineExpanded
            }
            expandedCardBehavior(
                binding.expandableCardMealType,
                binding.expandableChipGroupMealType,
                binding.imageViewArrowMealType,
                isMealTypeExpanded)
            isMealTypeExpanded = !isMealTypeExpanded
        }

        query = binding.textInputQuery.editText!!.text.trim().split("\\s+".toRegex())

        // Setting Search Button Functionality
        binding.buttonSearch.setOnClickListener {

            viewModel.setQuery(query)
            viewModel.setCuisines(cuisines?.toStringArrayList())
            viewModel.setMealTypes(mealTypes?.toStringArrayList())
            viewModel.setPage(0)
            searchCallback?.invoke()
            this.dismiss()
        }

        return binding.root
    }

    private fun expandedCardBehavior(card: CardView, expandablePart: View, expandIndicator: ImageView, isExpanded: Boolean) {
        expandIndicator.rotate(isExpanded)
        card.expand(isExpanded, expandablePart)
    }

    private fun CardView.expand(isExpanded: Boolean, expandablePart: View) {
        if (isExpanded) {
            TransitionManager.beginDelayedTransition(this, AutoTransition())
            expandablePart.visibility = View.GONE
        } else {
            TransitionManager.beginDelayedTransition(this, AutoTransition())
            expandablePart.visibility = View.VISIBLE
        }
    }

    private fun ImageView.rotate(isExpanded: Boolean) {
        if (!isExpanded)  this.startAnimation(rotateOpenAnimation)
        else              this.startAnimation(rotateCloseAnimation)
    }

    private fun ChipGroup.createChipGroup(resourceArray: Array<String>, resourceId: Int) {

        val outputArrayList = ArrayList<ArrayList<Any>>()
        for (item in resourceArray) {

            outputArrayList.add(arrayListOf(item, false))

            val chip = Chip(this.context)
            chip.setChipDrawable(ChipDrawable.createFromAttributes(chip.context, null, 0, R.style.Widget_MaterialComponents_Chip_Choice))
            chip.text = item
            chip.isClickable = true
            chip.isCheckable = true
            chip.isCheckedIconVisible = false

            chip.setOnCheckedChangeListener { _, isChecked ->
                if      (resourceId == 0) cuisines?.set(resourceArray.indexOf(item), arrayListOf(item, isChecked))
                else if (resourceId == 1) mealTypes?.set(resourceArray.indexOf(item), arrayListOf(item, isChecked))
            }
            this.addView(chip)
        }
        if (resourceId == 0) cuisines = outputArrayList
        else if (resourceId == 1) mealTypes = outputArrayList
    }

    private fun ArrayList<ArrayList<Any>>.toStringArrayList(): ArrayList<String> {
        val output = ArrayList<String>()
        for (item in this) {
            if (item[1] == true)
                output.add(item[0].toString())
        }
        return output
    }

    companion object {
        const val TAG = "RecipeSearchDialog"
    }
}