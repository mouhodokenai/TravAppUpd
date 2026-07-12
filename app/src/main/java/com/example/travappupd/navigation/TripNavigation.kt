package com.example.travappupd.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.travappupd.presentation.view.BudgetScreen
import com.example.travappupd.presentation.view.HotelScreen
import com.example.travappupd.presentation.view.MainScreen
import com.example.travappupd.presentation.view.NewTripScreen
import com.example.travappupd.presentation.view.NoteScreen
import com.example.travappupd.presentation.view.PackingListScreen
import com.example.travappupd.presentation.view.RouteScreen
import com.example.travappupd.presentation.view.TicketScreen
import com.example.travappupd.presentation.view.TripsScreen
import com.example.travappupd.presentation.viewmodel.BudgetViewModel
import com.example.travappupd.presentation.viewmodel.DraftViewModelFactory
import com.example.travappupd.presentation.viewmodel.TripViewModel

@Composable
fun TripNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            MainScreen(
                onNavigateToNewTrip = { navController.navigate("createTrip") },
                onNavigateToTrips = { type -> navController.navigate("plans/$type") }
            )
        }

        navigation(startDestination = "newTrip", route = "createTrip") {

            composable("newTrip") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("createTrip")
                }
                val tripViewModel: TripViewModel = hiltViewModel(parentEntry)

                NewTripScreen(
                    viewModel = tripViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToTrips = { type -> navController.navigate("plans/$type") },
                    onNavigateToBudget = { navController.navigate("draftBudget") },
                    onNavigateToHotel = { navController.navigate("draftHotel") },
                    onNavigateToNote = { navController.navigate("draftNote") },
                    onNavigateToPackingList = { navController.navigate("draftPackingList") },
                    onNavigateToRoute = { navController.navigate("draftRoute") },
                    onNavigateToTicket = { navController.navigate("draftTicket") }
                )
            }

            composable("draftBudget") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("createTrip")
                }
                val tripViewModel: TripViewModel = hiltViewModel(parentEntry)

                val budgetViewModel: BudgetViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    key = "draftBudgetViewModel",
                    factory = DraftViewModelFactory { BudgetViewModel(tripViewModel.budgetRepository) }
                )

                BudgetScreen(
                    tripId = tripViewModel.tripId,
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = budgetViewModel
                )
            }

            /*
            composable("draftHotel") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("createTrip")
                }
                val tripViewModel: TripViewModel = hiltViewModel(parentEntry)

                val hotelViewModel: HotelViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    key = "draftHotelViewModel",
                    factory = DraftViewModelFactory { HotelViewModel(tripViewModel.hotelRepository) }
                )

                HotelScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("draftNote") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("createTrip")
                }
                val tripViewModel: TripViewModel = hiltViewModel(parentEntry)

                val noteViewModel: NoteViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    key = "draftNoteViewModel",
                    factory = DraftViewModelFactory { NoteViewModel(tripViewModel.noteRepository) }
                )

                NoteScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("draftPackingList") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("createTrip")
                }
                val tripViewModel: TripViewModel = hiltViewModel(parentEntry)

                val packingListViewModel: PackingListViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    key = "draftPackingListViewModel",
                    factory = DraftViewModelFactory { PackingListViewModel(tripViewModel.packingListRepository) }
                )

                PackingListScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("draftRoute") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("createTrip")
                }
                val tripViewModel: TripViewModel = hiltViewModel(parentEntry)

                val routeViewModel: RouteViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    key = "draftRouteViewModel",
                    factory = DraftViewModelFactory { RouteViewModel(tripViewModel.routeRepository) }
                )

                RouteScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("draftTicket") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("createTrip")
                }
                val tripViewModel: TripViewModel = hiltViewModel(parentEntry)

                val ticketViewModel: TicketViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    key = "draftTicketViewModel",
                    factory = DraftViewModelFactory { TicketViewModel(tripViewModel.ticketRepository) }
                )

                TicketScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
*/

            composable(
                "budget/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getLong("id") ?: return@composable
                BudgetScreen(
                    onNavigateBack = { navController.popBackStack() },
                    tripId = tripId
                    // viewModel = hiltViewModel()
                )
            }

            composable(
                "hotel/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) {
                HotelScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                "note/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) {
                NoteScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                "packingList/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) {
                PackingListScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                "route/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) {
                RouteScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                "ticket/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) {
                TicketScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("plans/{type}") { backStackEntry ->
                val type = backStackEntry.arguments?.getString("type") ?: ""
                TripsScreen(
                    type = type,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetails = { id, tripName -> navController.navigate("details/$id/$tripName") }
                )
            }
        }
    }
}











