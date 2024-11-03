package com.example.myphotocollections.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.myphotocollections.data.services.SharedPrefManager
import com.example.myphotocollections.ui.customcomponents.InfoDialog
import com.example.myphotocollections.ui.customcomponents.observeConnectivityState
import com.example.myphotocollections.ui.navigation.SetUpNavHost
import com.example.myphotocollections.ui.theme.MyPhotoCollectionsTheme
import kotlinx.coroutines.delay
import android.provider.Settings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        SharedPrefManager.init(this)
        setContent {
            MyApp(this)
        }
    }
}

@Composable
fun MyApp(context:Context) {
    MyPhotoCollectionsTheme {
        val isConnected by observeConnectivityState()
        if(!isConnected){
                InfoDialog(
                    "No Internet",
                    "There is no internet connection. Please check your internet connection!"
                ) {
                    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    context.startActivity(intent)
                }
        }
        val navController = rememberNavController()
        SetUpNavHost(navController = navController)
    }
}