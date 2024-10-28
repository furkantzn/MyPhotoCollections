package com.example.myphotocollections.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myphotocollections.ui.pages.NavigationPage

@Composable
fun SetUpNavHost(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Destinations.Home
    ) {
        composable(route = Destinations.Home){
            NavigationPage(navController = navController)
        }
    }
}