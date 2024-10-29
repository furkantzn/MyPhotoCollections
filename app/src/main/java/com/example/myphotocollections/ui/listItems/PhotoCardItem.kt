package com.example.myphotocollections.ui.listItems

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.myphotocollections.R
import com.example.myphotocollections.data.models.Photo

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
                .placeholder(R.drawable.image_place_holder)
                .error(R.drawable.image_load_error)
                .build()
        )

        Image(
            painter = painter,
            contentDescription = "Example Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }
}