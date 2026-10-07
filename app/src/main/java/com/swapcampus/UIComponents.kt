package com.swapcampus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import shared.*
import java.util.UUID

@Composable
fun SwapCampusApp(viewModel: ListingsViewModel, authRepo: AuthRepository, navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(icon = { Text("List") }, label = { Text("Browse") }, selected = true, onClick = { navController.navigate("browse") })
                NavigationBarItem(icon = { Text("+") }, label = { Text("Post") }, selected = false, onClick = { navController.navigate("post") })
            }
        }
    ) { padding ->
        NavHost(navController, "browse", Modifier.padding(padding)) {
            composable("browse") { BrowseScreen(viewModel, navController) }
            composable("post") { PostScreen(viewModel, authRepo, navController) }
            composable("detail/{id}") { backStackEntry ->
                DetailScreen(backStackEntry.arguments?.getString("id") ?: "", viewModel, authRepo, navController)
            }
        }
    }
}

@Composable
fun BrowseScreen(viewModel: ListingsViewModel, navController: NavHostController) {
    val state by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(searchQuery, { searchQuery = it }, label = { Text("Search...") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { viewModel.search(searchQuery) }) { Text("Search") }

        when (val currentState = state) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Text(currentState.message, color = MaterialTheme.colorScheme.error)
            is UiState.Success -> {
                LazyColumn {
                    items(currentState.listings) { listing ->
                        Card(onClick = { navController.navigate("detail/${listing.id}") }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Column(Modifier.padding(16.dp)) { Text(listing.title); Text("€${listing.price}") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostScreen(viewModel: ListingsViewModel, authRepo: AuthRepository, navController: NavHostController) {
    val currentUser by authRepo.currentUser.collectAsState()
    if (currentUser == null) { Text("Please log in to post."); return }

    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(title, { title = it }, label = { Text("Title") })
        OutlinedTextField(price, { price = it }, label = { Text("Price") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Button(onClick = {
            viewModel.postListing(Annonce(UUID.randomUUID().toString(), title, "", price.toDoubleOrNull() ?: 0.0, Categorie.OTHER, ownerId = currentUser!!.id))
            navController.popBackStack()
        }) { Text("Publish") }
    }
}

@Composable
fun DetailScreen(id: String, viewModel: ListingsViewModel, authRepo: AuthRepository, navController: NavHostController) {
    val currentUser by authRepo.currentUser.collectAsState()
    val listing = viewModel.getListingById(id) ?: return
    var showDialog by remember { mutableStateOf(false) }

    Column(Modifier.padding(16.dp)) {
        Text(listing.title, style = MaterialTheme.typography.headlineMedium)
        if (currentUser?.id == listing.ownerId) {
            Button(onClick = { viewModel.changeStatus(id, EtatAnnonce.RESERVED) }) { Text("Reserve") }
            Button(onClick = { showDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text("Delete") }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = { TextButton(onClick = { viewModel.removeListing(id); navController.popBackStack() }) { Text("Confirm Delete") } },
            title = { Text("Delete Listing?") },
            text = { Text("This cannot be undone.") }
        )
    }
}
