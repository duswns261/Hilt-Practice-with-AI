package com.cret.hilt_practice.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cret.hilt_practice.presentation.ui.screen.DebugUserRoute
import com.cret.hilt_practice.presentation.ui.screen.HomeRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = HomeDestination.ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(HomeDestination.ROUTE) {
            HomeRoute(
                onUserClick = { userId ->
                    navController.navigate(UserDestination.createRoute(userId))
                }
            )
        }

        composable(
            route = UserDestination.ROUTE,
            arguments = listOf(
                navArgument(UserDestination.ARG_USER_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments
                ?.getString(UserDestination.ARG_USER_ID)
                ?: return@composable
            DebugUserRoute(userId = userId)
        }
    }
}