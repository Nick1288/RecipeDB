package com.example.recipedb.models

import com.google.gson.annotations.SerializedName

data class RecipeListResponse(
    @SerializedName("meals") val meals: List<Recipe>?
)

data class Recipe(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strMealThumb") val imageUrl: String
)