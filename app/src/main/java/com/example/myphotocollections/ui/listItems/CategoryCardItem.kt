package com.example.myphotocollections.ui.listItems

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myphotocollections.R
import com.example.myphotocollections.ui.customcomponents.BodyText

@Composable
fun CategoryCardItem(
    navController: NavController,
    categoryName: String,
    photoUrl: String
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable{navController.navigate("category_detail_page/${categoryName}")},
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
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
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
            BodyText(modifier = Modifier.background(MaterialTheme.colorScheme.onBackground).padding(16.dp).fillMaxWidth(),categoryName, textColor = Color.White, TextAlign.Center)
        }
    }
}