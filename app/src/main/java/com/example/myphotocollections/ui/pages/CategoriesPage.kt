package com.example.myphotocollections.ui.pages

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myphotocollections.ui.customcomponents.GradientText
import com.example.myphotocollections.ui.listItems.CategoryCardItem
import com.example.myphotocollections.ui.viewmodel.NavigationViewModel

@Composable
fun CategoriesPage(navController: NavController, viewModel: NavigationViewModel = viewModel()) {
    val categories by viewModel.categoryMap.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (!viewModel.isCategoriesLoaded) {
            loadCategories(viewModel, context)
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GradientText(
            text = "Categories",
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(16.dp, 16.dp, 32.dp, 8.dp)
                .fillMaxWidth()
        )
        LazyVerticalGrid(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            columns = GridCells.Fixed(2),
            content = {
                items(categories.size) { index ->
                    val categoryName = categories.keys.elementAt(index)
                    val photoUrl = categories[categoryName]
                    CategoryCardItem(
                        navController,
                        categoryName,
                        photoUrl!!
                    )
                }
            }
        )
    }
}

private fun loadCategories(
    viewModel: NavigationViewModel,
    context: Context
) {
    viewModel.initCategoriesFromJson(context) { success ->
        if (success) {
            viewModel.initCategoryPhotos { isSuccess ->
                if (isSuccess) {
                    println("Category loaded successfully!")
                } else {
                    println("Failed to load the category!")
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoriesPagePreview() {
    val navController = rememberNavController()
    HomePage(navController, viewModel())
}