package com.example.myphotocollections.ui.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myphotocollections.ui.view.customcomponents.GradientText
import com.example.myphotocollections.ui.view.listItems.PhotoCardItem
import com.example.myphotocollections.ui.viewmodel.CategoryDetailPageViewModel

@Composable
fun CategoryDetailPage(
    navController: NavController,
    categoryName: String,
    viewModel: CategoryDetailPageViewModel = hiltViewModel()
) {
    val photos by viewModel.photos.collectAsState()
    var currentPage by remember { mutableStateOf(1) }
    val gridState = rememberLazyGridState()
    LaunchedEffect(Unit) {
        if (!viewModel.isCategoriesDetailLoaded) {
            loadPageData(viewModel, currentPage, categoryName)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GradientText(
            text = categoryName,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(16.dp, 16.dp, 32.dp, 8.dp)
                .fillMaxWidth()
        )
        LazyVerticalGrid(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            columns = GridCells.Fixed(2),
            state = gridState,
            content = {
                items(photos.size) { index ->
                    PhotoCardItem(
                        navController,
                        photo = photos[index]
                    )
                }
            }
        )

        LaunchedEffect(gridState) {
            snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { lastVisibleItemIndex ->
                    if (lastVisibleItemIndex == photos.size - 1) {
                        currentPage++
                        loadPageData(viewModel, currentPage, categoryName)
                    }
                }
        }
    }
}

private fun loadPageData(
    viewModel: CategoryDetailPageViewModel,
    page: Int,
    categoryName: String
) {
    viewModel.initPhotos(categoryName, page) { success ->
        if (success) {
            println("Page $page loaded successfully!")
        } else {
            println("Failed to load page $page")
        }
    }
}

@Preview
@Composable
fun CategoryDetailPagePreview() {
    val navController = rememberNavController()
    CategoryDetailPage(navController, "")
}