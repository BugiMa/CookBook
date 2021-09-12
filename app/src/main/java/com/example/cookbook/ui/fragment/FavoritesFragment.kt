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
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.data.local.RecipeEntity
import com.example.cookbook.databinding.FragmentRecipeListBinding
import com.example.cookbook.ui.adapter.FavoritesAdapter
import com.example.cookbook.ui.adapter.RecipeListAdapter
import com.example.cookbook.ui.dialog.SearchDialogFragment
import com.example.cookbook.ui.viewModel.SharedViewModel
import com.example.cookbook.util.Constants
import com.example.cookbook.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        binding.searchFab.visibility = View.GONE
        val progressBar = binding.progressBarContainerRecipeList
        val recyclerView = binding.recipeRecycleView
        val recipeListAdapter = FavoritesAdapter(requireContext())

        recyclerView.apply {
            this.adapter = recipeListAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.getFavoriteRecipes().observe(viewLifecycleOwner, {
            recipeListAdapter.setData(it)
        })

        return binding.root
    }
}