package com.example.recipedb.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.recipedb.R
import com.example.recipedb.RecipeFinderViewModel
import com.example.recipedb.RecipeListUIState

enum class RecipeScreen(@StringRes val title: Int) {
    List(R.string.app_name),
    Description(R.string.recipe_description),
    Video(R.string.recipe_video)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeAppBar(
    currScreen: RecipeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    showVideoIcon: Boolean,
    onVideoClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currScreen.title)) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        modifier=modifier,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        actions = {
            if (showVideoIcon) {
                IconButton(onClick = onVideoClicked) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Video")
                }
            }
        },

    )
}

@Composable
fun RecipeFinderApp(
    viewModel: RecipeFinderViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = RecipeScreen.valueOf(backStackEntry?.destination?.route ?: RecipeScreen.List.name)

    val recipeListUiState by viewModel.recipeListUiState.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    val showVideoIcon = currentScreen == RecipeScreen.Description && uiState.selectedRecipe != null

    Scaffold(
        topBar = {
            RecipeAppBar(
                currScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                showVideoIcon = showVideoIcon,
                onVideoClicked = {
                    navController.navigate(RecipeScreen.Video.name)
                }
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RecipeScreen.List.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(RecipeScreen.List.name) {
                when (recipeListUiState) {
                    is RecipeListUIState.Loading -> CircularProgressIndicator()
                    is RecipeListUIState.Success -> {
                        RecipeGridScreen(
                            recipeList = (recipeListUiState as RecipeListUIState.Success).recipes,
                            onRecipeClicked = {
                                viewModel.setSelectedRecipe(it)
                                viewModel.fetchRecipeDetail(it.id)
                                navController.navigate(RecipeScreen.Description.name)
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp) )
                    }
                    is RecipeListUIState.Error -> Text("Error loading recipes.")
                }
            }

            composable(RecipeScreen.Description.name) {
                val recipeDetail = uiState.selectedRecipeDetail
                if (recipeDetail != null) {
                    RecipeDetailScreen(recipeDetail = recipeDetail)
                } else {
                    CircularProgressIndicator()
                }
            }

            composable(RecipeScreen.Video.name) {
                val recipeDetail = uiState.selectedRecipeDetail
                recipeDetail?.youtubeUrl?.let {
                    RecipeVideoScreen(youtubeUrl = it)
                } ?: Text("No video available")
            }
        }
    }
}
