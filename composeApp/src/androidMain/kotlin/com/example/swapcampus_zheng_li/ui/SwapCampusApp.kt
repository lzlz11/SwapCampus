package com.example.swapcampus_zheng_li.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.swapcampus_zheng_li.ui.navigation.Routes
import com.example.swapcampus_zheng_li.ui.screens.*
import com.example.swapcampus_zheng_li.ui.theme.SwapCampusTheme
import com.example.swapcampus_zheng_li.ui.viewmodel.*

@Composable
fun SwapCampusApp(container: AppContainer) {
    val auth = viewModel { AuthViewModel(container.authRepository) }
    val listings = viewModel { ListingsViewModel(container.listingRepository, container.authRepository) }
    val user by auth.currentUser.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route
    val tabs = listOf(Routes.LISTINGS to "Listings", Routes.MY_LISTINGS to "My Listings", Routes.PROFILE to "Profile")
    SwapCampusTheme {
        Scaffold(bottomBar = {
            NavigationBar {
                tabs.forEach { (route, label) ->
                    NavigationBarItem(
                        selected = currentRoute == route,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(Routes.LISTINGS) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text(label.take(1)) }, label = { Text(label) }
                    )
                }
            }
        }) { padding ->
            NavHost(navController, startDestination = Routes.LISTINGS, modifier = Modifier.padding(padding)) {
                composable(Routes.LISTINGS) {
                    ListingsScreen(listings,
                        onListingClick = { navController.navigate(Routes.detail(it)) },
                        onPostClick = { navController.navigate(Routes.POST) })
                }
                composable(Routes.MY_LISTINGS) {
                    MyListingsScreen(listings, user,
                        onLogin = { navController.navigate(Routes.LOGIN) },
                        onListingClick = { navController.navigate(Routes.detail(it)) })
                }
                composable(Routes.PROFILE) {
                    ProfileScreen(user, onLogin = { navController.navigate(Routes.LOGIN) }, onLogout = auth::logout)
                }
                composable(Routes.LOGIN) {
                    LoginScreen(auth, onLoggedIn = { navController.popBackStack() })
                }
                composable(Routes.POST) {
                    ListingFormScreen(null, listings, user,
                        onLogin = { navController.navigate(Routes.LOGIN) },
                        onSaved = { navController.popBackStack() })
                }
                composable(Routes.EDIT, arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                    ListingFormScreen(it.arguments!!.getInt("id"), listings, user,
                        onLogin = { navController.navigate(Routes.LOGIN) },
                        onSaved = { navController.popBackStack() })
                }
                composable(Routes.DETAIL, arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                    ListingDetailScreen(it.arguments!!.getInt("id"), listings, user,
                        onLogin = { navController.navigate(Routes.LOGIN) },
                        onEdit = { id -> navController.navigate(Routes.edit(id)) },
                        onDeleted = { navController.popBackStack() })
                }
            }
        }
    }
}
