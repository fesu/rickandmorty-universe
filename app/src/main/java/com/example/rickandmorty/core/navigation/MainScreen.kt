package com.example.rickandmorty.core.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rickandmorty.feature.characters.presentation.detail.CharacterDetailScreen
import com.example.rickandmorty.feature.episodes.presentation.EpisodesScreen
import com.example.rickandmorty.feature.characters.presentation.list.CharacterListScreen
import com.example.rickandmorty.feature.locations.presentation.LocationsScreen

data class TopLevelDestination(
    val route: Any,
    val icon: ImageVector,
    val label: String
)

val topLevelDestinations = listOf(
    TopLevelDestination(
        route = CharacterListRoute,
        icon = Icons.Default.Face,
        label = "Characters"
    ),
    TopLevelDestination(
        route = EpisodesRoute,
        icon = Icons.Default.PlayArrow,
        label = "Episodes"
    ),
    TopLevelDestination(
        route = LocationsRoute,
        icon = Icons.Default.LocationOn,
        label = "Locations"
    )
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // Optional: Hide bottom bar on specific screens like CharacterDetail
            val isTopLevelRoute = currentDestination?.hierarchy?.any { dest ->
                dest.route?.contains("CharacterListRoute") == true ||
                dest.route?.contains("EpisodesRoute") == true ||
                dest.route?.contains("LocationsRoute") == true
            } == true

            if (isTopLevelRoute) {
                NavigationBar {
                    topLevelDestinations.forEach { destination ->
                        val selected = currentDestination?.hierarchy?.any { 
                            it.route?.contains(destination.route::class.simpleName ?: "") == true 
                        } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            },
                            icon = { Icon(destination.icon, contentDescription = destination.label) },
                            label = { Text(destination.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        SharedTransitionLayout {
            NavHost(
                navController = navController,
                startDestination = CharacterListRoute,
                modifier = Modifier.padding(innerPadding),
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                }
            ) {
                composable<CharacterListRoute> {
                    CharacterListScreen(
                        onCharacterClick = { characterId ->
                            navController.navigate(CharacterDetailRoute(characterId = characterId))
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this
                    )
                }

                composable<CharacterDetailRoute> {
                    CharacterDetailScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this
                    )
                }

                composable<EpisodesRoute> {
                    EpisodesScreen()
                }

                composable<LocationsRoute> {
                    LocationsScreen()
                }
            }
        }
    }
}
