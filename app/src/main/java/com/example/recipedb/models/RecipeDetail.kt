package com.example.recipedb.models

import com.google.gson.annotations.SerializedName

data class RecipeDetailResponse(
    @SerializedName("meals") val meals: List<RecipeDetail>?
)

data class RecipeDetail(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strMealThumb") val imageUrl: String,
    @SerializedName("strInstructions") val instructions: String,
    @SerializedName("strIngredient1") val ingredient1: String?,
    @SerializedName("strIngredient2") val ingredient2: String?,
    @SerializedName("strIngredient3") val ingredient3: String?,
    @SerializedName("strIngredient4") val ingredient4: String?,
    @SerializedName("strYoutube") val youtubeUrl: String?
)