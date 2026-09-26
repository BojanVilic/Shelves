package com.bojanvilic.shelves

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bojanvilic.shelves.bookdetails.BookDetailsScreen
import com.bojanvilic.shelves.discover.DiscoverScreen
import com.bojanvilic.shelves.navigation.BookDetailsRoute
import com.bojanvilic.shelves.navigation.DiscoverRoute
import com.bojanvilic.shelves.navigation.SearchRoute
import com.bojanvilic.shelves.search.SearchScreen

@Composable
fun ShelvesApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.hierarchy?.any {
        it.hasRoute<DiscoverRoute>() || it.hasRoute<SearchRoute>()
    } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination.hierarchy.any {
                            it.hasRoute<DiscoverRoute>()
                        },
                        onClick = {
                            navController.navigate(DiscoverRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Discover",
                            )
                        },
                        label = { Text("Discover") },
                    )
                    NavigationBarItem(
                        selected = currentDestination.hierarchy.any {
                            it.hasRoute<SearchRoute>()
                        },
                        onClick = {
                            navController.navigate(SearchRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                            )
                        },
                        label = { Text("Search") },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DiscoverRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<DiscoverRoute> {
                DiscoverScreen(
                    onBookClick = { workId ->
                        navController.navigate(BookDetailsRoute(workId))
                    },
                )
            }
            composable<SearchRoute> {
                SearchScreen(
                    onBookClick = { workId ->
                        navController.navigate(BookDetailsRoute(workId))
                    },
                )
            }
            composable<BookDetailsRoute> {
                BookDetailsScreen(
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
