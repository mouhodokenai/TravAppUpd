package com.example.travappupd.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travappupd.presentation.view.MainScreen
import com.example.travappupd.presentation.view.NewTripScreen

import com.example.travappupd.presentation.view.TripsScreen

@Composable
fun TripNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            MainScreen(
                onNavigateToNewTrip = { navController.navigate("new") },
                onNavigateToTrips = { type -> navController.navigate("plan/$type") }
            )
        }

        composable("newTrip") {
            NewTripScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("plan/{type}") {backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: ""
            TripsScreen(
                type = type,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetails = { id, tripName -> navController.navigate("details/$id/$tripName") },
            )
        }
    }

}
