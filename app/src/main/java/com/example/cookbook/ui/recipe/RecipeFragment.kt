package com.example.cookbook.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.cookbook.R
import com.example.cookbook.repository.RecipeRepository
import com.squareup.picasso.Picasso


class RecipeFragment : Fragment() {

    private val args: RecipeFragmentArgs by navArgs()
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_recipe, container, false)

        val imageViewRecipe = root.findViewById<ImageView>(R.id.image_view_recipe)
        val textViewTitle = root.findViewById<TextView>(R.id.text_view_recipe_title)
        val textViewAdditionalInfo = root.findViewById<TextView>(R.id.text_view_recipe_additional_info)
        val listIngredients = root.findViewById<LinearLayout>(R.id.linear_layout_ingredients)
        val cardViewDirections = root.findViewById<LinearLayout>(R.id.expandable_cards_directions)

        val repository = RecipeRepository()
        val viewModelFactory = RecipeViewModelFactory(repository)
        recipeViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(RecipeViewModel::class.java)

        recipeViewModel.getResponse(args.id)
        recipeViewModel.getRecipeDetails().observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                response.body()!!.let { recipeViewModel.setRecipeData(it) }

                Picasso.get().load(recipeViewModel.getImage()).into(imageViewRecipe)
                textViewTitle.text = recipeViewModel.getTitle()
                textViewAdditionalInfo.text = recipeViewModel.getAdditionalInfo()

                for (ingredient in recipeViewModel.getIngredients()!!) {
                    val textView = TextView(context)
                    textView.text = ingredient.original
                    textView.setPadding(0, 16, 0, 16)
                    listIngredients.addView(textView)
                }

                for (step in recipeViewModel.getDirections()!!.steps) {
                    val textView = TextView(context)
                    textView.text = "${step.number}. ${step.step}"
                    textView.setPadding(0, 16, 0, 16)
                    cardViewDirections.addView(textView)
                }

            } else {
                Toast.makeText(this.context, "Response Error", Toast.LENGTH_SHORT).show()
            }
        })


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





    }




}