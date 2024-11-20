package com.example.myphotocollections.ui.view.customcomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogProgress(isLoading: Boolean) {
    if (isLoading) {
        Dialog(onDismissRequest = { /* Prevent dismiss */ }) {
            Box {
                AnimatedPreloader(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun DialogDownloadProgress(isLoading: Boolean) {
    if (isLoading) {
        Dialog(onDismissRequest = { /* Prevent dismiss */ }) {
            Box {
                AnimatedDownloadImage(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun InfoDialog(
    title: String,
    message: String,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            BodyText(text = title, textColor = MaterialTheme.colorScheme.onPrimary)
        },
        text = {
            BodyText(text = message, textColor = MaterialTheme.colorScheme.primary)
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                modifier = Modifier.fillMaxWidth()
            ) {
                BodyText(text = "Open Network Settings")
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}