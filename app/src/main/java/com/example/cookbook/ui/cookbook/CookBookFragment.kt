package com.example.cookbook.ui.cookbook

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.repository.RecipeRepository
import com.example.cookbook.ui.search.SearchDialogFragment
import com.example.cookbook.ui.shopping.ShoppingListAdapter
import com.google.android.material.chip.ChipGroup

class CookBookFragment : Fragment() {

    private lateinit var cookBookViewModel: CookBookViewModel
    private val cookBookAdapter by lazy { CookBookAdapter() }
    var offset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {

                val dialog = SearchDialogFragment()
                dialog.show(childFragmentManager, SearchDialogFragment.TAG)

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_cookbook, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.cookbook_recycle_view)
        val linearLayoutManager = LinearLayoutManager(this.context)

        recyclerView.apply {
            this.layoutManager = linearLayoutManager
            this.adapter = cookBookAdapter
        }

        val repository = RecipeRepository()
        val viewModelFactory = CookBookViewModelFactory(repository)
        cookBookViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(CookBookViewModel::class.java)


        cookBookViewModel.getResponse(0)
        cookBookViewModel.getRecipes().observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                response.body()!!.results.let { cookBookAdapter.setRecipeData(it)}
            } else {
                Toast.makeText(this.context ,"Response Error", Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }
}