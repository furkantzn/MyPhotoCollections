package com.example.myphotocollections.ui.view.customcomponents
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun GradientText(text: String,modifier: Modifier,textAlign: TextAlign) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        style = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            brush = Brush.linearGradient(
                colors = listOf(MaterialTheme.colorScheme.onPrimary, MaterialTheme.colorScheme.background) // Gradient colors
            )
        ),
    )
}

@Composable
fun BodyText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        textAlign = textAlign,
        style = MaterialTheme.typography.bodyLarge,
        color = textColor,
        modifier = modifier
    )
}