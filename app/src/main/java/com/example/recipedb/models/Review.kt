package com.example.recipedb.models

data class Review(
    val recipeId: String,
    val username: String,
    val comment: String,
    val rating: Int
)
