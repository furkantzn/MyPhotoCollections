package com.example.myphotocollections.ui.pages

import android.provider.ContactsContract.Contacts.Photo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myphotocollections.R
import com.example.myphotocollections.ui.customcomponents.BodyText
import com.example.myphotocollections.ui.customcomponents.DialogProgress
import com.example.myphotocollections.ui.viewmodel.PhotoDetailPageViewModel

@Composable
fun PhotoDetailPage(photoId: Int, viewModel: PhotoDetailPageViewModel = viewModel()) {
    val isLoading = remember { mutableStateOf(false) }
    val photoUrl = remember {
        mutableStateOf("")
    }
    val photographer = remember {
        mutableStateOf("")
    }
    val isFavorited = remember {
        mutableStateOf(false)
    }
    val showingDetails = remember {
        mutableStateOf(true)
    }
    val favoriteImageVector = if (isFavorited.value) {
        ImageVector.vectorResource(id = R.drawable.ic_favorited)
    } else {
        ImageVector.vectorResource(id = R.drawable.ic_unfavoried)
    }
    LaunchedEffect(Unit) {
        isLoading.value = true
        viewModel.getPhotoDetail(photoId) { success, photo ->
            if (success) {
                if (photo != null) {
                    photoUrl.value = photo.src.portrait
                    photographer.value = photo.photographer
                }
            } else {
                println("Photo could not load!")
            }
            isLoading.value = false
        }
    }

    if (isLoading.value) {
        DialogProgress(isLoading.value)
    } else {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUrl.value)
                    .crossfade(true)
                    .placeholder(R.drawable.image_place_holder)
                    .error(R.drawable.image_load_error)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = "Example Image",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .clickable { showingDetails.value = !showingDetails.value }
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
            if (showingDetails.value) {
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
                            "by ${photographer.value}",
                            textColor = Color.White,
                            TextAlign.Center
                        )
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
                                .clickable { },
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_download_image),
                            contentDescription = "Download Image"
                        )
                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    isFavorited.value = !isFavorited.value
                                },
                            imageVector = favoriteImageVector,
                            contentDescription = "Add to Favorite"
                        )
                    }
                }
            }
        }
    }
}