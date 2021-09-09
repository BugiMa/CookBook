package com.example.cookbook.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.cookbook.R
import com.example.cookbook.data.local.RecipeEntity
import com.example.cookbook.databinding.FragmentRecipeDetailsBinding
import com.example.cookbook.databinding.FragmentRecipeListBinding
import com.example.cookbook.ui.adapter.RecipeListAdapter
import com.example.cookbook.ui.viewModel.SharedViewModel
import com.example.cookbook.util.Constants
import com.example.cookbook.util.GlideApp
import com.example.cookbook.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text

@AndroidEntryPoint
class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SharedViewModel

    private val args: RecipeDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.loadRecipeDetails(args.id)

        viewModel.recipeDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    Log.d("RECIPE DETAILS OBSERVER", "Resource SUCCESS")
                    binding.progressBarContainerRecipeDetails.visibility = View.GONE
                    response.data?.let { details ->

                        val ingredients = ArrayList<String>()
                        val steps = ArrayList<String>()
                        for (ingredient in details.extendedIngredients) {
                            ingredients.add(ingredient.original)
                        }
                        for (instruction in details.analyzedInstructions) {
                            for (step in instruction.steps) {
                                steps.add(step.step)
                            }
                        }

                        binding.ingredientList.createList(ingredients)
                        binding.directionsList.createList(steps)
                        binding.recipeTitle.text = details.title
                        viewModel.isFavorite(details.id).observe(viewLifecycleOwner, { isFavorite ->
                            binding.favoriteCheckBox.isChecked = isFavorite
                        })
                        binding.favoriteCheckBox.setOnClickListener {
                            val recipe = RecipeEntity(details.id, details.image, details.title, ingredients, steps, details.summary, true)
                            viewModel.favorite(recipe)
                        }

                        val progressBar = CircularProgressDrawable(requireContext()).apply {
                            this.strokeWidth = 16f
                            this.centerRadius = 48f
                            this.start()
                        }
                        GlideApp.with(this)
                            .load(details.image)
                            .placeholder(progressBar)
                            .centerInside()
                            .into(binding.recipeImageView)
                    }
                }
                is Resource.Error -> {
                    Log.d("RECIPE DETAILS OBSERVER", "Resource ERROR")
                    binding.progressBarContainerRecipeDetails.visibility = View.GONE
                    response.message?.let { message ->
                        if (message == Constants.NO_INTERNET)
                            noInternetDialog() else Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    Log.d("RECIPE DETAILS OBSERVER", "Resource LOADING")
                    binding.progressBarContainerRecipeDetails.visibility = View.VISIBLE
                }
            }
        }



        return binding.root
    }

    private fun noInternetDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Oops!")
            .setIcon(R.drawable.ic_round_error_outline_24)
            .setMessage("It looks like You don't have internet connection. Would You like to turn internet on now?")
            .setPositiveButton("Mobile Data") { _, _ ->
                startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
            }
            .setNegativeButton("Wi-Fi") { _, _ ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setNeutralButton("No") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun switchVisibility(view: View) {
        if (view.visibility == View.VISIBLE)
            view.visibility = View.GONE else view.visibility = View.VISIBLE
    }

    private fun LinearLayout.createList(array: ArrayList<String>) {
        for (item in array) {

            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(12)

            val textView = TextView(requireContext())
            textView.apply {
                this.text = item
                this.textSize = 16f
                this.layoutParams = params
            }
            this.addView(textView)
        }
    }
}