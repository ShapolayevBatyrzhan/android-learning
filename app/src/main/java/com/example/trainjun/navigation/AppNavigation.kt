package com.example.trainjun.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trainjun.movie.DetailScreen
import com.example.trainjun.movie.FavoritesScreen
import com.example.trainjun.movie.MovieListScreen
import com.example.trainjun.navigation.Routes.ARG_MOVIE_ID

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(Routes.MOVIE_LIST, Routes.FAVORITES)) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Routes.MOVIE_LIST,
                        onClick = {
                            navController.navigate(Routes.MOVIE_LIST) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Movies") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.FAVORITES,
                        onClick = {
                            navController.navigate(Routes.FAVORITES) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                        label = { Text("Favorites") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.MOVIE_LIST,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.MOVIE_LIST) {
                MovieListScreen(onMovieClick = { id ->
                    navController.navigate(Routes.movieDetail(id))
                })
            }
            composable(
                route = Routes.MOVIE_DETAIL_PATTERN,
                arguments = listOf(navArgument(ARG_MOVIE_ID) { type = NavType.IntType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt(ARG_MOVIE_ID) ?: 0
                DetailScreen(
                    movieId = movieId,
                    onBack = { navController.popBackStack() })
            }
            composable(Routes.FAVORITES) {
                FavoritesScreen()
            }
        }

    }
}