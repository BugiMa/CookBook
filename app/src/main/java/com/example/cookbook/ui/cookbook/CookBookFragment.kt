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
    var offset = 10

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

        val scrollListener = object: EndlessRecyclerViewScrollListener(linearLayoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {

                cookBookViewModel.getResponse(offset)

                cookBookViewModel.getRecipes().observe(viewLifecycleOwner, Observer { response ->
                    if (response.isSuccessful) {
                        response.body()!!.results.let { (recyclerView.adapter as CookBookAdapter).addRecipeData(it, offset, 10) }
                    } else {
                        Toast.makeText(requireContext() ,response.code(), Toast.LENGTH_SHORT).show()
                    }
                })
                offset += 10
            }
        }

        recyclerView.apply {
            this.layoutManager = linearLayoutManager
            this.adapter = cookBookAdapter
            this.addOnScrollListener(scrollListener)
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


/*
    val repository = RecipeRepository()
        val viewModelFactory = CookBookViewModelFactory(repository)
        cookBookViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(CookBookViewModel::class.java)
        cookBookViewModel.loadRecipes(0)

        mAdapter = CookBookAdapter(requireActivity(), cookBookViewModel.getRecipes(), cookBookViewModel)
        val recyclerView: RecyclerView = root.findViewById(R.id.cookbook_recycle_view)
        recyclerView.apply {
            this.layoutManager = LinearLayoutManager(this.context)
            this.adapter = mAdapter

        }

        cookBookViewModel.response.observe(viewLifecycleOwner, Observer { response ->
            mAdapter.notifyDataSetChanged()
            if (response.isSuccessful) {} else {}
        })
*/