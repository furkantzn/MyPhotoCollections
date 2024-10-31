package com.example.myphotocollections.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.myphotocollections.data.services.SharedPrefManager
import com.example.myphotocollections.ui.navigation.SetUpNavHost
import com.example.myphotocollections.ui.theme.MyPhotoCollectionsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        SharedPrefManager.init(this)
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