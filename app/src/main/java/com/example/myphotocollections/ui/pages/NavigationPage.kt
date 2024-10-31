package com.example.myphotocollections.ui.pages

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myphotocollections.ui.viewmodel.NavigationViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationPage(navController: NavController, viewModel: NavigationViewModel = viewModel()) {
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(64.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                listOf(
                    Icons.Default.Home to "Home",
                    Icons.Default.Category to "Categories",
                    Icons.Default.Person to "Profile"
                ).forEachIndexed { index, pair ->
                    val (icon, description) = pair
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = description) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = Color.White,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(bottom = 64.dp)
                    .fillMaxWidth()
            ) {
                when (selectedIndex) {
                    0 -> HomePage(navController = navController, viewModel = viewModel)
                    1 -> CategoriesPage(navController = navController, viewModel = viewModel)
                    2 -> FavoritesPage(navController = navController, viewModel = viewModel)
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