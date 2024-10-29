package com.example.myphotocollections.ui.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.myphotocollections.ui.theme.MyPhotoCollectionsTheme
import com.example.myphotocollections.ui.navigation.SetUpNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    MyPhotoCollectionsTheme {
        val navController = rememberNavController()
        SetUpNavHost(navController = navController)
    }
}