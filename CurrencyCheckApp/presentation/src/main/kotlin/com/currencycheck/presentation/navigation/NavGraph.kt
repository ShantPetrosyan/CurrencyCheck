package com.currencycheck.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.currencycheck.presentation.ui.filter.compose.FilterScreen
import com.currencycheck.presentation.ui.main.compose.MainScreen

@Composable
fun NavGraph(innerPadding: Dp) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MainScreen.route, Modifier.padding(innerPadding)) {
        addMainScreenRoute(navController)
        addFilterOptionsScreenRoute(navController)
    }
}

fun NavGraphBuilder.addMainScreenRoute(navController: NavHostController) {
    composable(route = MainScreen.route) { entry ->
        MainScreen(navController, entry.savedStateHandle)
    }
}

fun NavGraphBuilder.addFilterOptionsScreenRoute(navController: NavHostController) {
    composable(FilterOptions.route) {
        FilterScreen(navController)
    }
}