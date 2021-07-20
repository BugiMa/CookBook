package com.example.cookbook.ui.search

import android.os.Bundle
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
import com.example.cookbook.repository.RecipeRepository
import com.example.cookbook.ui.cookbook.CookBookViewModel
import com.example.cookbook.ui.cookbook.CookBookViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout


class SearchDialogFragment : BottomSheetDialogFragment() {

    private val rotateOpenAnimation: Animation by lazy {AnimationUtils.loadAnimation(this.context, R.anim.rotate_open_anim)}
    private val rotateCloseAnimation: Animation by lazy {AnimationUtils.loadAnimation(this.context, R.anim.rotate_close_anim)}

    private  var query: List<String>? = null
    private var cuisines: ArrayList<ArrayList<Any>>? = null
    private var mealTypes: ArrayList<ArrayList<Any>>? = null
    private var isCuisineExpanded  = false
    private var isMealTypeExpanded = false

    private lateinit var cookBookViewModel: CookBookViewModel

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
    ): View? {
        val repository = RecipeRepository()
        val viewModelFactory = CookBookViewModelFactory(repository)
        cookBookViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(CookBookViewModel::class.java)
        return inflater.inflate(R.layout.dialog_fragment_search, container, false) //root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setting Cuisine Expandable Chip Group Card
        val cuisineNames = resources.getStringArray(R.array.array_cuisine)
        val chipGroupCuisine = view.findViewById<ChipGroup>(R.id.chip_group_cuisine)
        val cuisineCard = view.findViewById<CardView>(R.id.expandable_card_cuisine)
        val cuisineArrow = view.findViewById<ImageView>(R.id.image_view_arrow_cuisine)
        val cuisineCardExpandablePart = view.findViewById<LinearLayout>(R.id.expandable_chip_group_cuisine)
        // Adding Cuisines as Chips to Chip Group
        chipGroupCuisine.createChipGroup(cuisineNames, 0)

        // Setting Meal Type Expandable Chip Group Card
        val mealTypeNames = resources.getStringArray(R.array.array_meal_type)
        val chipGroupMealType = view.findViewById<ChipGroup>(R.id.chip_group_meal_type)
        val mealTypeCard = view.findViewById<CardView>(R.id.expandable_card_meal_type)
        val mealTypeArrow = view.findViewById<ImageView>(R.id.image_view_arrow_meal_type)
        val mealTypeCardExpandablePart = view.findViewById<LinearLayout>(R.id.expandable_chip_group_meal_type)
        // Adding Meal Types as Chips to Chip Group
        chipGroupMealType.createChipGroup(mealTypeNames, 1)

        // Setting Cards OnClickListeners
        cuisineCard.setOnClickListener {
            if (isMealTypeExpanded) { // Collapsing other card if expanded
                expandedCardBehavior(mealTypeCard, mealTypeCardExpandablePart, mealTypeArrow, isMealTypeExpanded)
                isMealTypeExpanded = !isMealTypeExpanded
            }
            expandedCardBehavior(cuisineCard, cuisineCardExpandablePart, cuisineArrow, isCuisineExpanded)
            isCuisineExpanded = !isCuisineExpanded
        }

        mealTypeCard.setOnClickListener {
            if (isCuisineExpanded) { // Collapsing other card if expanded
                expandedCardBehavior(cuisineCard, cuisineCardExpandablePart, cuisineArrow, isCuisineExpanded)
                isCuisineExpanded = !isCuisineExpanded
            }
            expandedCardBehavior(mealTypeCard, mealTypeCardExpandablePart, mealTypeArrow, isMealTypeExpanded)
            isMealTypeExpanded = !isMealTypeExpanded
        }

        val textFieldQuery = view.findViewById<TextInputLayout>(R.id.text_input_query)
        query = textFieldQuery.editText!!.text.trim().split("\\s+".toRegex())
        // TODO: Include/Exclude Ingredients View & Functionality

        // Setting Search Button Functionality
        val searchButton = view.findViewById<Button>(R.id.button_search)
        searchButton.setOnClickListener {

            cookBookViewModel.setQuery(query)
            cookBookViewModel.setCuisines(cuisines?.toStringArrayList())
            cookBookViewModel.setMealTypes(mealTypes?.toStringArrayList())
            cookBookViewModel.getResponse(0)
            dialog?.dismiss()
        }
    }

    private fun expandedCardBehavior(card: CardView, expandablePart: View, expandIndicator: ImageView, isExpanded: Boolean) {
        expandIndicator.rotate(isExpanded)
        card.expand(isExpanded, expandablePart)
    }

    private fun CardView.expand(isExpanded: Boolean, expandablePart: View) {
        if (isExpanded) {
            //TransitionManager.beginDelayedTransition(this, AutoTransition())
            expandablePart.visibility = View.GONE
        } else {
            //TransitionManager.beginDelayedTransition(this, AutoTransition())
            expandablePart.visibility = View.VISIBLE
        }
    }

    private fun ImageView.rotate(isExpanded: Boolean) {
        if(!isExpanded)  this.startAnimation(rotateOpenAnimation)
        else            this.startAnimation(rotateCloseAnimation)
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