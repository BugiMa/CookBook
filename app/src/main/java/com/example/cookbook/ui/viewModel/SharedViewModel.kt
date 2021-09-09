package com.example.cookbook.ui.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cookbook.CookBookApplication
import com.example.cookbook.data.local.RecipeEntity
import com.example.cookbook.data.remote.Recipe
import com.example.cookbook.data.remote.RecipeDetails
import com.example.cookbook.data.remote.RecipeSearch
import com.example.cookbook.repo.RecipeRepository
import com.example.cookbook.repo.ShoppingListRepository
import com.example.cookbook.repo.SpoonacularRepository
import com.example.cookbook.util.Constants.Companion.CONVERSION_ERROR
import com.example.cookbook.util.Constants.Companion.NETWORK_FAILURE
import com.example.cookbook.util.Constants.Companion.NO_INTERNET
import com.example.cookbook.util.Constants.Companion.RECIPE_LIMIT
import com.example.cookbook.util.Resource
import com.example.cookbook.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    app: Application,
    private val spoonacularRepo: SpoonacularRepository,
    private val recipeRepo: RecipeRepository,
    private val shoppingListRepo: ShoppingListRepository
): AndroidViewModel(app) {

    private val loadedRecipes = ArrayList<Recipe>()

    // Search Variables
    private var page: Int = 0
    private var query: List<String>? = null
    private var diet: String? = null
    private var intolerances: ArrayList<String>? = null
    private var cuisines: ArrayList<String>? = null
    private var mealTypes : ArrayList<String>? = null

    // LiveData
    private val _recipes = SingleLiveEvent<Resource<ArrayList<Recipe>>>()
    val recipes: LiveData<Resource<ArrayList<Recipe>>> get() = _recipes

    private val _recipeDetails = SingleLiveEvent<Resource<RecipeDetails>>()
    val recipeDetails: LiveData<Resource<RecipeDetails>> get() = _recipeDetails

    private val favoriteRecipes: LiveData<List<RecipeEntity>> = recipeRepo.all()

    init {
        loadRecipes()
    }

    // Api

    fun loadRecipes() = viewModelScope.launch(Dispatchers.IO) {
        _recipes.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable()) {
                val response = spoonacularRepo.searchRecipes(
                    page * RECIPE_LIMIT,
                    RECIPE_LIMIT,
                    query,
                    cuisines,
                    mealTypes,
                    diet,
                    intolerances
                )
                _recipes.postValue(handleSearchResponse(response))
            } else {
                _recipes.postValue(Resource.Error(NO_INTERNET))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _recipes.postValue(Resource.Error(NETWORK_FAILURE))
                else -> _recipes.postValue(Resource.Error(CONVERSION_ERROR))
            }
        }
    }
    fun loadRecipeDetails(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        _recipeDetails.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable()) {
                val response = spoonacularRepo.getRecipeDetails(id)
                _recipeDetails.postValue(handleDetailsResponse(response))
            } else {
                _recipeDetails.postValue(Resource.Error(NO_INTERNET))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _recipeDetails.postValue(Resource.Error(NETWORK_FAILURE))
                else -> _recipeDetails.postValue(Resource.Error(CONVERSION_ERROR))
            }
        }
    }
    private fun handleSearchResponse(response: Response<RecipeSearch>): Resource<ArrayList<Recipe>> {
        if (response.isSuccessful) {
            response.body().let { resultResponse ->
                page++
                return Resource.Success(resultResponse!!.results)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleDetailsResponse(response: Response<RecipeDetails>): Resource<RecipeDetails> {
        if (response.isSuccessful) {
            response.body().let { resultResponse ->
                page++
                return Resource.Success(resultResponse!!)
            }
        }
        return Resource.Error(response.message())
    }
    private fun isNetworkAvailable(): Boolean {
        var result = false
        val connectivityManager =
            getApplication<CookBookApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)     -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }

        return result
    }

    // Room

    fun favorite(recipe: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        recipeRepo.favorite(recipe)
    }
    fun isFavorite(id: Int): LiveData<Boolean> {
        val isFav = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO){
            isFav.postValue(recipeRepo.exist(id))
        }
        return isFav
    }

    // Util

    fun setPage(page: Int) {
        this.page = page
    }

    fun getLoadedRecipes(): ArrayList<Recipe> = loadedRecipes

    fun getFavoriteRecipes(): LiveData<List<RecipeEntity>> = favoriteRecipes

    fun setQuery(query: List<String>?) {
        this.query = query
    }
    fun setDiet(diet: String?) {
        this.diet = diet
    }
    fun setIntolerances(intolerances: ArrayList<String>?) {
        this.intolerances = intolerances
    }
    fun setCuisines(intolerances: ArrayList<String>?) {
        this.cuisines = intolerances
    }
    fun setMealTypes(intolerances: ArrayList<String>?) {
        this.mealTypes = intolerances
    }


}