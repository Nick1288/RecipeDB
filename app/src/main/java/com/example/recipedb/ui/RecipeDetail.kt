package com.example.recipedb.ui

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipedb.models.RecipeDetail

@Composable
fun RecipeDetailScreen(
    recipeDetail: RecipeDetail,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            AsyncImage(
                model = recipeDetail.imageUrl,
                contentDescription = recipeDetail.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Ingredients:", style = MaterialTheme.typography.titleMedium)
        }

        items(
            listOfNotNull(
                recipeDetail.ingredient1,
                recipeDetail.ingredient2,
                recipeDetail.ingredient3,
                recipeDetail.ingredient4
            )
        ) { ingredient ->
            Text("- $ingredient")
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Instructions:", style = MaterialTheme.typography.titleMedium)
            Text(recipeDetail.instructions)

        }
    }
}
