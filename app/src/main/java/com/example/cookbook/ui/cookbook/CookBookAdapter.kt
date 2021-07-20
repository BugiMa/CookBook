package com.example.cookbook.ui.cookbook

import android.app.Activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.model.RecipeModel
import com.example.cookbook.model.RecipeSearchModel
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class CookBookAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var recipes = ArrayList<RecipeModel>()
    private var favorites = ArrayList<RecipeModel>()

    inner class RecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recipeCard: CardView = itemView.findViewById(R.id.recipe_card)
        var name: TextView = itemView.findViewById(R.id.text_view_recipe_name)
        var isFavorite: Chip = itemView.findViewById(R.id.chip_favorite)
        var image: ImageView = itemView.findViewById(R.id.image_view_recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerViewViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_cookbook, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as RecyclerViewViewHolder

        viewHolder.name.text = recipes[position].title

        Picasso.get().load(recipes[position].image).into(holder.image)

        holder.isFavorite.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                favorites.add(recipes[position]) //TODO: Duplicate Check, Toast
            } else {
                favorites.remove(recipes[position])
            }
        }

        holder.recipeCard.setOnClickListener {
            val action = CookBookFragmentDirections.actionNavigationCookbookToRecipeFragment(recipes[position].id)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return recipes.count()
    }

    fun setRecipeData(newRecipes: ArrayList<RecipeModel>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    fun addRecipeData(newRecipes: ArrayList<RecipeModel>, position: Int, count: Int) {
        recipes.addAll(newRecipes)
        notifyItemRangeInserted(position, count)
    }

    fun getFavorites(item: ArrayList<RecipeModel>): ArrayList<RecipeModel> {
        return favorites
    }


}