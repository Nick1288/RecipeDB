package com.example.recipedb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipedb.models.Recipe
import com.example.recipedb.models.RecipeDetail
import com.example.recipedb.models.Review
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeFinderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeUIState())
    val uiState: StateFlow<RecipeUIState> = _uiState

    private val _recipeListUiState = MutableStateFlow<RecipeListUIState>(RecipeListUIState.Loading)
    val recipeListUiState: StateFlow<RecipeListUIState> = _recipeListUiState

    init {
        fetchPopularRecipesFromApi()
    }

    fun fetchPopularRecipesFromApi() {
        viewModelScope.launch {
            _recipeListUiState.value = RecipeListUIState.Loading
            try {
                val recipes = RecipeApi.fetchRecipes("chicken")
                _recipeListUiState.value = RecipeListUIState.Success(recipes)
            } catch (e: Exception) {
                _recipeListUiState.value = RecipeListUIState.Error
            }
        }
    }

    fun setSelectedRecipe(recipe: Recipe) {
        _uiState.value = _uiState.value.copy(
            selectedRecipe = recipe,
            selectedRecipeDetail = null
        )
    }

    fun fetchRecipeDetail(id: String) {
        viewModelScope.launch {
            try {
                val detail = RecipeApi.fetchRecipeDetail(id)
                detail?.let {
                    _uiState.value = _uiState.value.copy(selectedRecipeDetail = it)
                }
            } catch (_: Exception) { }
        }
    }
}

data class RecipeUIState(
    val selectedRecipe: Recipe? = null,
    val selectedRecipeDetail: RecipeDetail? = null
)

sealed class RecipeListUIState {
    object Loading : RecipeListUIState()
    data class Success(val recipes: List<Recipe>) : RecipeListUIState()
    object Error : RecipeListUIState()
}

