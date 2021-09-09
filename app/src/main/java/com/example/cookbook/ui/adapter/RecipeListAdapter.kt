package com.example.cookbook.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.cookbook.data.remote.Recipe
import com.example.cookbook.databinding.ListItemRecipeBinding
import com.example.cookbook.ui.fragment.RecipeListFragmentDirections
import com.example.cookbook.util.GlideApp

class RecipeListAdapter(
    private val context: Context,
    private val recipes: ArrayList<Recipe>,
    private val favoriteCallback: ((recipeId: Int) -> Unit)?
    ): RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder (private val itemBinding: ListItemRecipeBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.favoriteCheckBox.setOnClickListener {
                favoriteCallback?.invoke(recipes[adapterPosition].id)
            }
        }
        fun bind(title: String, image: String, isFavorite: Boolean?) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemBinding = ListItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe.title, recipe.image, recipe.isFavorite)
        holder.itemView.setOnClickListener {
            val directions = RecipeListFragmentDirections
                .actionNavigationRecipeListToRecipeFragment(
                    recipe.id,
                )
            it.findNavController().navigate(directions)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun updateData(data: ArrayList<Recipe>) {
        this.recipes.addAll(data)
        notifyItemRangeInserted(itemCount, data.size)
    }

    fun clearData() {
        val count = itemCount
        this.recipes.clear()
        notifyItemRangeRemoved(0, count)
    }


}