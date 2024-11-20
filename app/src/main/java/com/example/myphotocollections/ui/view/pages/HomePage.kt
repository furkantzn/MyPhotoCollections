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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myphotocollections.ui.view.customcomponents.GradientText
import com.example.myphotocollections.ui.view.listItems.PhotoCardItem
import com.example.myphotocollections.ui.viewmodel.NavigationViewModel

@Composable
fun HomePage(navController: NavController, viewModel: NavigationViewModel) {
    val trendingPhotos by viewModel.trendingPhotos.collectAsState()
    val currentPage = remember { mutableStateOf(1) }
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        if (!viewModel.isTrendingLoaded) {
            loadPageData(viewModel, 1)
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
            text = "Trending Photos",
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
                items(trendingPhotos.size) { index ->
                    PhotoCardItem(
                        navController,
                        photo = trendingPhotos[index]
                    )
                }
            }
        )

        LaunchedEffect(gridState) {
            snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { lastVisibleItemIndex ->
                    if (lastVisibleItemIndex == trendingPhotos.size - 1) {
                        currentPage.value++
                        loadPageData(viewModel, currentPage.value)
                    }
                }
        }
    }
}

private fun loadPageData(
    viewModel: NavigationViewModel,
    page: Int
) {
    viewModel.initTrendingPhotos(page) { success ->
        if (success) {
            println("Page $page loaded successfully!")
        } else {
            println("Failed to load page $page")
        }
    }
}

@Preview
@Composable
fun HomePagePreview() {
    val navController = rememberNavController()
    HomePage(navController, viewModel())
}