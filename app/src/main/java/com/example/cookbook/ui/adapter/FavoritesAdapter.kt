package com.example.cookbook.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.cookbook.data.local.ProductEntity
import com.example.cookbook.data.local.RecipeEntity
import com.example.cookbook.databinding.ListItemRecipeBinding
import com.example.cookbook.ui.fragment.FavoritesFragmentDirections
import com.example.cookbook.ui.fragment.RecipeListFragmentDirections
import com.example.cookbook.util.GlideApp

class FavoritesAdapter(
    private val context: Context
): RecyclerView.Adapter<FavoritesAdapter.RecipeViewHolder>() {

    private var recipes = emptyList<RecipeEntity>()
    inner class RecipeViewHolder (private val itemBinding: ListItemRecipeBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(title: String, image: String, isFavorite: Boolean?) {

            itemBinding.favoriteCheckBox.visibility = View.GONE
            itemBinding.recipeTitle.text = title
            if (isFavorite != null) {
                itemBinding.favoriteCheckBox.isChecked = isFavorite
            }

            val progressBar = CircularProgressDrawable(context).apply {
                this.strokeWidth = 16f
                this.centerRadius = 48f
                this.start()
            }

            GlideApp.with(context)
                .load(image)
                .placeholder(progressBar)
                .centerInside()
                .into(itemBinding.recipeImageView)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesAdapter.RecipeViewHolder {
        val itemBinding = ListItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: FavoritesAdapter.RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe.title, recipe.image, recipe.isFavorite)
        holder.itemView.setOnClickListener {
            val directions = FavoritesFragmentDirections
                .actionNavigationFavoritesToRecipeFragment(
                    recipe.id,
                )
            it.findNavController().navigate(directions)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(newData: List<RecipeEntity>?) {
        if (newData != null) {
            this.recipes = newData
        }
        notifyDataSetChanged()
    }
}