package com.example.swapcampus_zheng_li

import com.example.swapcampus_zheng_li.composeapp.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.getString
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.swapcampus_zheng_li.ui.i18n.*

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.swapcampus_zheng_li.ui.navigation.*
import com.example.swapcampus_zheng_li.ui.AppContainer
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.jetbrains.compose.resources.StringResource
import com.example.swapcampus_zheng_li.ui.screens.*
import com.example.swapcampus_zheng_li.ui.theme.SwapCampusTheme
import com.example.swapcampus_zheng_li.ui.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val container = remember { AppContainer() }
    val auth: AuthViewModel = viewModel(factory = viewModelFactory { initializer { AuthViewModel(container.authRepository) } })
    val listings: ListingsViewModel = viewModel(factory = viewModelFactory { initializer { ListingsViewModel(container.listingRepository, container.authRepository) } })
    val user by auth.currentUser.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val destination = entry?.destination
    val tabs: List<Pair<Any, StringResource>> = listOf(
        ListingsRoute to Res.string.listings_title,
        MyListingsRoute to Res.string.my_listings_title,
        ProfileRoute to Res.string.profile_title
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(listings, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            listings.messages.collect { message ->
                snackbarHostState.showSnackbar(getString(message.resource()))
            }
        }
    }
    val isTopLevel = tabs.any { destination?.hasRoute(it.first::class) == true }
    val title = when {
        destination?.hasRoute<MyListingsRoute>() == true -> Res.string.my_listings_title
        destination?.hasRoute<ProfileRoute>() == true -> Res.string.profile_title
        destination?.hasRoute<LoginRoute>() == true -> Res.string.login_title
        destination?.hasRoute<PostRoute>() == true -> Res.string.post_title
        destination?.hasRoute<EditRoute>() == true -> Res.string.edit_title
        destination?.hasRoute<DetailRoute>() == true -> Res.string.detail_title
        else -> Res.string.listings_title
    }
    SwapCampusTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(title)) },
                    navigationIcon = {
                        if (!isTopLevel && navController.previousBackStackEntry != null) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
                            }
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (isTopLevel) {
                    NavigationBar {
                        tabs.forEach { (route, label) ->
                            NavigationBarItem(
                                selected = destination?.hasRoute(route::class) == true,
                                onClick = {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(when (route) {
                                        MyListingsRoute -> Icons.AutoMirrored.Filled.List
                                        ProfileRoute -> Icons.Filled.Person
                                        else -> Icons.Filled.Home
                                    }, contentDescription = null)
                                },
                                label = { Text(stringResource(label)) }
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                if (destination?.hasRoute<ListingsRoute>() == true || destination?.hasRoute<MyListingsRoute>() == true) {
                    FloatingActionButton(onClick = { navController.navigate(PostRoute) }) {
                        Icon(Icons.Filled.Add, stringResource(Res.string.post_title))
                    }
                }
            }
        ) { padding ->
            NavHost(navController, startDestination = ListingsRoute, modifier = Modifier.padding(padding).consumeWindowInsets(padding)) {
                composable<ListingsRoute>() {
                    ListingsScreen(listings,
                        onListingClick = { navController.navigate(DetailRoute(it)) })
                }
                composable<MyListingsRoute>() {
                    MyListingsScreen(listings, user,
                        onLogin = { navController.navigate(LoginRoute) },
                        onListingClick = { navController.navigate(DetailRoute(it)) })
                }
                composable<ProfileRoute>() {
                    ProfileScreen(user, onLogin = { navController.navigate(LoginRoute) }, onLogout = auth::logout)
                }
                composable<LoginRoute>() {
                    LoginScreen(auth, onLoggedIn = { navController.popBackStack() }, showTitle = false)
                }
                composable<PostRoute>() {
                    ListingFormScreen(null, listings, user,
                        onLogin = { navController.navigate(LoginRoute) },
                        onSaved = { navController.popBackStack() })
                }
                composable<EditRoute> { entry ->
                    ListingFormScreen(entry.toRoute<EditRoute>().id, listings, user,
                        onLogin = { navController.navigate(LoginRoute) },
                        onSaved = { navController.popBackStack() })
                }
                composable<DetailRoute> { entry ->
                    ListingDetailScreen(entry.toRoute<DetailRoute>().id, listings, user,
                        onLogin = { navController.navigate(LoginRoute) },
                        onEdit = { id -> navController.navigate(EditRoute(id)) },
                        onDeleted = { navController.popBackStack() })
                }
            }
        }
    }
}
