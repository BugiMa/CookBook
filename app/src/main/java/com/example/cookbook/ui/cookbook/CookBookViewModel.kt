package com.example.cookbook.ui.cookbook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.RecipeModel
import com.example.cookbook.model.RecipeSearchModel
import com.example.cookbook.repository.RecipeRepository
import com.example.cookbook.ui.search.SearchDialogFragment
import kotlinx.coroutines.launch
import retrofit2.Response

class CookBookViewModel( private val recipeRepository: RecipeRepository) : ViewModel() {

    private var recipesNumber: Int = 0

    private val recipesLiveData: MutableLiveData<Response<RecipeSearchModel>> = MutableLiveData()



    private val favoritesLiveData: MutableLiveData<ArrayList<RecipeModel>> = MutableLiveData()
    private val favoritesArrayList: ArrayList<RecipeModel> =ArrayList()

    //Search Variables
    private var query: List<String>? = null
    private var diet: String? = null
    private var intolerances: ArrayList<String>? = null
    private var cuisines: ArrayList<String>? = null
    private var mealTypes : ArrayList<String>? = null

    fun getResponse(offset: Int) {
        viewModelScope.launch {
            val resp = recipeRepository.searchRecipes(offset, query, cuisines, mealTypes, diet, intolerances)
            //recipesNumber = response.body()!!.number
            recipesLiveData.value = resp
        }
    }

    fun getRecipes(): MutableLiveData<Response<RecipeSearchModel>> {
        return recipesLiveData
    }

    fun getRecipesNumber(): Int {
        return recipesNumber
    }

    fun getQuery(): List<String>? {
        return query
    }
    fun setQuery(query: List<String>?) {
        this.query = query
    }

    fun getDiet(): String? {
        return diet
    }
    fun setDiet(diet: String?) {
        this.diet = diet
    }

    fun getIntolerances(): ArrayList<String>? {
        return intolerances
    }
    fun setIntolerances(intolerances: ArrayList<String>?) {
        this.intolerances = intolerances
    }

    fun getCuisines(): ArrayList<String>? {
        return cuisines
    }
    fun setCuisines(intolerances: ArrayList<String>?) {
        this.cuisines = intolerances
    }

    fun getMealTypes(): ArrayList<String>? {
        return mealTypes
    }
    fun setMealTypes(intolerances: ArrayList<String>?) {
        this.mealTypes = intolerances
    }

    fun clearSearch() {
        query = null
        cuisines = null
        mealTypes = null
    }

    fun addToFavorites(recipe: RecipeModel) {
        favoritesArrayList.add(recipe)
        favoritesLiveData.value = favoritesArrayList
    }

    fun removeFromFavorites(recipe: RecipeModel) {
        favoritesArrayList.remove(recipe)
        favoritesLiveData.value = favoritesArrayList
    }


}