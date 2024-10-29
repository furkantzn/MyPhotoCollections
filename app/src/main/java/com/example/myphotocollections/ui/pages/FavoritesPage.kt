package com.example.myphotocollections.ui.pages

import android.content.Context
import android.content.SharedPreferences
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myphotocollections.ui.customcomponents.DialogProgress
import com.example.myphotocollections.ui.customcomponents.GradientText
import com.example.myphotocollections.ui.listItems.PhotoCardItem
import com.example.myphotocollections.ui.viewmodel.FavoritesPageViewModel
import com.example.myphotocollections.ui.viewmodel.HomePageViewModel

@Composable
fun FavoritesPage(navController: NavController, viewModel: FavoritesPageViewModel = viewModel()) {
    val photos by viewModel.photos.collectAsState()
    val isLoading = remember { mutableStateOf(false) }
    val gridState = rememberLazyGridState()
    LaunchedEffect(Unit) {
        loadData(viewModel,isLoading)
    }
    if (isLoading.value) {
        DialogProgress(isLoading.value)
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
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
                    items(photos.size) { index ->
                        PhotoCardItem(
                            navController,
                            photo = photos[index]
                        )
                    }
                }
            )
        }
    }
}

private fun loadData(viewModel: FavoritesPageViewModel, isLoading: MutableState<Boolean> = mutableStateOf(false)) {
    isLoading.value = true
    viewModel.initFavoritePhotos{success->
        if(success){
            viewModel.initPhotos { success ->
                if (success) {
                    println("Photo loaded successfully!")
                } else {
                    println("Failed to load data")
                }
            }
        }
        isLoading.value = false
    }
}

@Preview
@Composable
fun FavoritesPagePreview() {
    val navController = rememberNavController()
    FavoritesPage(navController)
}