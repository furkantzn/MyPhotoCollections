package com.example.myphotocollections.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myphotocollections.ui.theme.MyPhotoCollectionsTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationPage(navController: NavController) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(64.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },
                    selected = selectedIndex == 0,
                    onClick = { selectedIndex = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = Color.White,
                        indicatorColor = Color.Transparent
                    )

                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Category,
                            contentDescription = "Categories"
                        )
                    },
                    selected = selectedIndex == 1,
                    onClick = { selectedIndex = 1 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = Color.White,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    },
                    selected = selectedIndex == 2,
                    onClick = { selectedIndex = 2 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = Color.White,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(bottom = 64.dp)
                    .fillMaxWidth()
            ) {
                when (selectedIndex) {
                    0 -> {
                        HomePage(navController = navController)
                    }

                    1 -> {
                        CategoriesPage(navController = navController)
                    }

                    2 -> {
                        FavoritesPage(navController = navController)
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun NavigationPagePreview() {
    val navController = rememberNavController()
    NavigationPage(navController = navController)
}