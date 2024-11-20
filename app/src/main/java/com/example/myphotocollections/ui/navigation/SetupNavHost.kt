package com.example.myphotocollections.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myphotocollections.ui.view.pages.CategoryDetailPage
import com.example.myphotocollections.ui.view.pages.NavigationPage
import com.example.myphotocollections.ui.view.pages.PhotoDetailPage

@Composable
fun SetUpNavHost(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Destinations.Home
    ) {
        composable(route = Destinations.Home){
            NavigationPage(navController = navController)
        }

        composable(route=Destinations.CategoryDetailPage){
                backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName")
            if (categoryName != null) {
                CategoryDetailPage(navController = navController, categoryName = categoryName )
            }
        }

        composable(route=Destinations.PhotoDetailPage){
                backStackEntry ->
            val photoIdString = backStackEntry.arguments?.getString("photoId")
            val photoId = photoIdString?.toInt()
            if (photoId != null) {
                PhotoDetailPage(photoId = photoId )
            }
        }
    }
}