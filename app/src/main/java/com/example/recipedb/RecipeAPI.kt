package com.example.recipedb

import com.example.recipedb.models.Recipe
import com.example.recipedb.models.RecipeDetail
import com.example.recipedb.models.RecipeDetailResponse
import com.example.recipedb.models.RecipeListResponse
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// --- Retrofit Interface ---

interface RecipeApiService {
    @GET("search.php")
    suspend fun searchRecipes(@Query("s") query: String): RecipeListResponse

    @GET("lookup.php")
    suspend fun getRecipeDetails(@Query("i") recipeId: String): RecipeDetailResponse
}

// --- API Singleton ---

object RecipeApi {
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    suspend fun fetchRecipes(query: String): List<Recipe> {
        return withContext(Dispatchers.IO) {
            service.searchRecipes(query).meals ?: emptyList()
        }
    }

    suspend fun fetchRecipeDetail(id: String): RecipeDetail? {
        return withContext(Dispatchers.IO) {
            service.getRecipeDetails(id).meals?.firstOrNull()
        }
    }
}
