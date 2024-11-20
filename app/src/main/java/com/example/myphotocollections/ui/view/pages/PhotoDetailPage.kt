package com.example.myphotocollections.ui.view.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myphotocollections.R
import com.example.myphotocollections.ui.view.customcomponents.BodyText
import com.example.myphotocollections.ui.view.customcomponents.DialogDownloadProgress
import com.example.myphotocollections.ui.view.customcomponents.DialogProgress
import com.example.myphotocollections.ui.viewmodel.PhotoDetailPageViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PhotoDetailPage(photoId: Int, viewModel: PhotoDetailPageViewModel = hiltViewModel()) {
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    val isLoading = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val fileName = "${photoId}.jpg"
    var photoUrl by remember {
        mutableStateOf("")
    }
    var photographer by remember {
        mutableStateOf("")
    }
    var isFavorited by remember {
        mutableStateOf(false)
    }
    var showingDetails by remember {
        mutableStateOf(true)
    }
    var isDownloadingImage by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(photoId) {
        isLoading.value = true
        isFavorited = viewModel.getListFromPref().contains(photoId)
        viewModel.getPhotoDetail(photoId) { success, photo ->
            if (success) {
                if (photo != null) {
                    photoUrl = photo.src.portrait
                    photographer = photo.photographer
                }
            } else {
                snackScope.launch {
                    snackState.showSnackbar(
                        "Photo could not load! Check your internet connection!",
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )
                }
            }
            isLoading.value = false
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackState)
        }
    ) {
        DialogDownloadProgress(isDownloadingImage)
        if (isLoading.value) {
            DialogProgress(isLoading.value)
        } else {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photoUrl)
                        .crossfade(true)
                        .placeholder(R.drawable.ic_image_item_download)
                        .error(R.drawable.image_load_error)
                        .build()
                )

                Image(
                    painter = painter,
                    contentDescription = "Example Image",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .clickable { showingDetails = !showingDetails }
                        .fillMaxWidth()
                        .fillMaxHeight()
                )

                if (showingDetails) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.onBackground)
                                .fillMaxWidth()
                        ) {
                            BodyText(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                "by $photographer",
                                textColor = Color.White,
                                TextAlign.Center
                            )
                        }

                        ActionButtons(
                            isFavorited,
                            onDownloadClick = {
                                val isFileExist =
                                    viewModel.fileExistsInMediaStore(context, fileName)
                                if (isFileExist) {
                                    snackScope.launch {
                                        snackState.showSnackbar(
                                            "File already exists check your gallery!",
                                            duration = SnackbarDuration.Short,
                                            withDismissAction = true
                                        )
                                    }
                                } else {
                                    isDownloadingImage = true
                                    viewModel.downloadImageWithCoil(
                                        context,
                                        photoUrl,
                                        fileName
                                    ) { _, message ->
                                        snackScope.launch {
                                            snackState.showSnackbar(
                                                message,
                                                duration = SnackbarDuration.Indefinite,
                                                withDismissAction = true
                                            )
                                        }
                                        isDownloadingImage = false
                                    }
                                }
                            },
                            onFavoriteClick = {
                                val favoritePhotos =
                                    viewModel
                                        .getListFromPref()
                                        .toMutableList()
                                val message = if (!isFavorited) {
                                    favoritePhotos.add(photoId)
                                    "The photo added to your favorites!"
                                } else {
                                    favoritePhotos.remove(photoId)
                                    "The photo removed from your favorites!"
                                }
                                viewModel.setFavoritePhotoIdsToPref(favoritePhotos) { success ->
                                    if (success) {
                                        isFavorited = !isFavorited
                                        snackScope.launch {
                                            snackState.showSnackbar(
                                                message,
                                                duration = SnackbarDuration.Short,
                                                withDismissAction = true
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButtons(
    isFavorited: Boolean,
    onDownloadClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    val favoriteImageVector = if (isFavorited) {
        ImageVector.vectorResource(id = R.drawable.ic_favorited)
    } else {
        ImageVector.vectorResource(id = R.drawable.ic_unfavoried)
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onBackground)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onDownloadClick),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_download_image),
            contentDescription = "Download Image"
        )
        Image(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onFavoriteClick),
            imageVector = favoriteImageVector,
            contentDescription = "Add to Favorite"
        )
    }
}