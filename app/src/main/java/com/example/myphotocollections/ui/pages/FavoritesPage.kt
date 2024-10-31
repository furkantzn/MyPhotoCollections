package com.example.myphotocollections.ui.pages

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myphotocollections.ui.customcomponents.GradientText
import com.example.myphotocollections.ui.listItems.PhotoCardItem
import com.example.myphotocollections.ui.viewmodel.NavigationViewModel

@Composable
fun FavoritesPage(navController: NavController, viewModel: NavigationViewModel = viewModel()) {
    val favoritePhotos by viewModel.favoritePhotos.collectAsState()
    val gridState = rememberLazyGridState()
    LaunchedEffect(Unit) {
        loadPhotoIds(viewModel)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GradientText(
            text = "My Favorite Photos",
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
                items(favoritePhotos.size) { index ->
                    PhotoCardItem(
                        navController,
                        photo = favoritePhotos[index]
                    )
                }
            }
        )
    }
}

private fun loadFavoritePhotos(viewModel: NavigationViewModel) {
    viewModel.initFavoritePhotos { isSuccess ->
        if (isSuccess) {
            println("Photo loaded successfully!")
        } else {
            println("Failed to load the photo!")
        }
    }
}

private fun loadPhotoIds(viewModel: NavigationViewModel) {
    viewModel.initFavoritePhotoIds { success ->
        if (success) {
            if (!viewModel.allItemsIdentical()) {
                loadFavoritePhotos(viewModel)
            }
        }
    }
}

@Preview
@Composable
fun FavoritesPagePreview() {
    val navController = rememberNavController()
    FavoritesPage(navController)
}