package com.example.myphotocollections.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.example.myphotocollections.ui.customcomponents.DialogProgress
import com.example.myphotocollections.ui.customcomponents.GradientText
import com.example.myphotocollections.ui.listItems.PhotoCardItem
import com.example.myphotocollections.ui.viewmodel.CategoryDetailPageViewModel

@Composable
fun CategoryDetailPage(navController: NavController,categoryName:String,viewModel: CategoryDetailPageViewModel = viewModel()){
    val photos by viewModel.photos.collectAsState()
    val currentPage = remember { mutableStateOf(1) }
    val isLoading = remember { mutableStateOf(false) }
    val gridState = rememberLazyGridState()
    LaunchedEffect(Unit) {
        loadPageData(viewModel,currentPage.value,categoryName,isLoading)
    }
    if (isLoading.value) {
        DialogProgress(isLoading.value)
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
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
                            photo = photos[index]
                        )
                    }
                }
            )

            LaunchedEffect(gridState) {
                snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collect { lastVisibleItemIndex ->
                        if (lastVisibleItemIndex == photos.size - 1 && !isLoading.value) {
                            currentPage.value++
                            loadPageData(viewModel,currentPage.value,categoryName)
                        }
                    }
            }
        }
    }
}

private fun loadPageData(viewModel: CategoryDetailPageViewModel, page: Int,categoryName:String, isLoading: MutableState<Boolean> = mutableStateOf(false)) {
    isLoading.value = true
    viewModel.initPhotos(categoryName,page) { success ->
        if (success) {
            println("Page $page loaded successfully!")
        } else {
            println("Failed to load page $page")
        }
        isLoading.value = false
    }
}

@Preview
@Composable
fun CategoryDetailPagePreview() {
    val navController = rememberNavController()
    CategoryDetailPage(navController,"")
}