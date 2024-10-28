package com.example.myphotocollections.ui.customcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogProgress(isLoading: Boolean,message:String = "Loading...") {
    if (isLoading) {
        Dialog(onDismissRequest = { /* Prevent dismiss */ }) {
            Box{
                AnimatedPreloader(modifier = Modifier.size(200.dp).align(Alignment.Center))
            }
        }
    }
}