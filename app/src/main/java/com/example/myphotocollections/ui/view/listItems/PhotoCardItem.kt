package com.example.myphotocollections.ui.view.listItems

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myphotocollections.R
import com.example.myphotocollections.data.model.Photo

@Composable
fun PhotoCardItem(
    navController: NavController,
    photo: Photo
    ){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { navController.navigate("photo_detail_page/${photo.id}") },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo.src.portrait)
                .crossfade(true)
                .placeholder(R.drawable.ic_image_item_download)
                .error(R.drawable.image_load_error)
                .build()
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "Example Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}