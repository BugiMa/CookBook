package com.example.cookbook.ui.recipe

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.RecipeDetailsModel
import com.example.cookbook.repository.RecipeRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    private var recipeDetails: MutableLiveData<Response<RecipeDetailsModel>> = MutableLiveData()

    private var recipeTitle: String? = null
    private var recipeAdditionalInfo: String? = null
    private var image: String? = null
    private var ingredients: List<RecipeDetailsModel.ExtendedIngredient>? = null
    private var directions: List<RecipeDetailsModel.AnalyzedInstruction>? = null
    private var isFavorite: Boolean = false

    fun getResponse(id: Int) {
        viewModelScope.launch {
            val resp = recipeRepository.getRecipeDetails(id)
            recipeDetails.value = resp
        }
    }

    fun getRecipeDetails(): MutableLiveData<Response<RecipeDetailsModel>> { return recipeDetails }
    fun getTitle(): String? { return recipeTitle }
    fun getAdditionalInfo(): String? {
        val split  = recipeAdditionalInfo?.split(".")
        var joined = split?.get(0) + split?.get(1)
        joined = joined.replace("<b>", "").replace("</b>", "")
        return joined }
    fun getImage(): String? { return image }
    fun getIngredients(): List<RecipeDetailsModel.ExtendedIngredient>? { return ingredients }
    fun getDirections(): RecipeDetailsModel.AnalyzedInstruction? { return directions?.get(0) }

    fun setRecipeData(recipeDetails: RecipeDetailsModel) {

        recipeTitle =           recipeDetails.title
        recipeAdditionalInfo =  recipeDetails.summary
        image =                 recipeDetails.image
        ingredients =           recipeDetails.extendedIngredients
        directions =            recipeDetails.analyzedInstructions

    }

}