package com.example.cookbook.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.cookbook.R
import com.example.cookbook.data.local.RecipeEntity
import com.example.cookbook.databinding.FragmentRecipeListBinding
import com.example.cookbook.ui.adapter.RecipeListAdapter
import com.example.cookbook.ui.dialog.SearchDialogFragment
import com.example.cookbook.ui.viewModel.SharedViewModel
import com.example.cookbook.util.Constants
import com.example.cookbook.util.Constants.Companion.NO_INTERNET
import com.example.cookbook.util.GlideApp
import com.example.cookbook.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment: Fragment() {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


        val fab = binding.searchFab
        val progressBar = binding.progressBarContainerRecipeList
        val recyclerView = binding.recipeRecycleView
        val recipeListAdapter = RecipeListAdapter (requireContext(), viewModel.getLoadedRecipes(), favoriteCallback =  { id ->

            viewModel.loadRecipeDetails(id)
            viewModel.recipeDetails.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {

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

                            val recipe = RecipeEntity(details.id, details.image, details.title, ingredients, steps, details.summary, true)
                            viewModel.favorite(recipe)
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            if (message == NO_INTERNET)
                                noInternetDialog() else Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                        }
                    }
                    is Resource.Loading -> {
                    }
                }
            }
        })

        recyclerView.apply {
            this.adapter = recipeListAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() >= recyclerView.adapter!!.itemCount - 1) {
                        viewModel.loadRecipes()
                    }

                    if (newState == RecyclerView.SCROLL_STATE_IDLE ) {
                        Handler(Looper.myLooper()!!).postDelayed({
                            fab.show()
                        }, 200)
                    } else {
                        fab.hide()
                    }

                }
            })
        }

        if (viewModel.getDietAndIntoleranceUpdated()) {
            recipeListAdapter.clearData()
        }

        viewModel.recipes.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    Log.d("RECIPES OBSERVER", "Resource SUCCESS")
                    progressBar.visibility = View.GONE
                    response.data?.let { data ->

                        viewModel.getFavoriteRecipes().observe(viewLifecycleOwner, { entities ->
                            for (recipe in data) {
                                if (entities.find { it.id == recipe.id } != null) {
                                    recipe.apply {
                                        this.isFavorite = true
                                    }
                                }
                            }
                        })
                        recipeListAdapter.updateData(data)
                        Toast.makeText(requireContext(),"Recipes loaded successfully",Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Error -> {
                    Log.d("RECIPES OBSERVER", "Resource ERROR")
                    progressBar.visibility = View.GONE
                    response.message?.let { message ->
                        if (message == NO_INTERNET)
                            noInternetDialog() else Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    Log.d("RECIPES OBSERVER", "Resource LOADING")
                    progressBar.visibility = View.VISIBLE
                }
            }
        }

        fab.setOnClickListener {
            val dialog = SearchDialogFragment {
                recipeListAdapter.clearData()
                viewModel.loadRecipes()
            }
            dialog.show(childFragmentManager, SearchDialogFragment.TAG)
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
}