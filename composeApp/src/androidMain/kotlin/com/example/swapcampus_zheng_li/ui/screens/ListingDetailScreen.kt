package com.example.swapcampus_zheng_li.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.swapcampus_zheng_li.*
import com.example.swapcampus_zheng_li.ui.components.ListingPhoto
import com.example.swapcampus_zheng_li.ui.viewmodel.ListingsViewModel

@Composable
fun ListingDetailScreen(id: Int, viewModel: ListingsViewModel, user: CompteUtilisateur?, onLogin: () -> Unit, onEdit: (Int) -> Unit, onDeleted: () -> Unit) {
    val allListings by viewModel.allListings.collectAsStateWithLifecycle()
    val listing = allListings.find { it.id == id } ?: viewModel.getListing(id)
    if (listing == null) { Text("Listing not found.", Modifier.padding(16.dp)); return }
    val isOwner = listing.auteur.id == user?.id
    var showDelete by rememberSaveable(id) { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    fun changeStatus(status: EtatAnnonce) {
        viewModel.changeStatus(id, status).onFailure { error = it.message }
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(listing.titre, style = MaterialTheme.typography.headlineMedium)
        ListingPhoto(listing, Modifier.fillMaxWidth().height(140.dp))
        Text(listing.description)
        Text("€${listing.prix}", style = MaterialTheme.typography.titleLarge)
        Text(listing.categorie.nomAffiche())
        Text("Owner: ${listing.auteur.nom}")
        Text("Created: ${listing.dateCreation}")
        Text("Status: " + when (val status = listing.etat) {
            Disponible -> "Available"
            is Reservee -> "Reserved by ${status.acheteur}"
            is Vendue -> "Sold on ${status.dateVente}"
        })
        when (listing.etat) {
            Disponible -> if (!isOwner) {
                if (user == null) Button(onClick = onLogin) { Text("Log in to reserve") }
                else Button(onClick = { changeStatus(Reservee(user.nom)) }) { Text("Reserve") }
            }
            is Reservee -> if (isOwner) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { changeStatus(Vendue(todayDate())) }) { Text("Mark as sold") }
                    OutlinedButton(onClick = { changeStatus(Disponible) }) { Text("Cancel") }
                }
            }
            is Vendue -> if (isOwner) {
                Button(onClick = { changeStatus(Disponible) }) { Text("Make available") }
            }
        }
        if (isOwner) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onEdit(id) }) { Text("Edit") }
                Button(onClick = { showDelete = true }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text("Delete") }
            }
        }
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
    if (showDelete) AlertDialog(
        onDismissRequest = { showDelete = false }, title = { Text("Delete listing?") },
        text = { Text("This action cannot be undone.") },
        confirmButton = { TextButton(onClick = {
            viewModel.removeListing(id).onSuccess { showDelete = false; onDeleted() }
                .onFailure { showDelete = false; error = it.message }
        }) { Text("Delete") } },
        dismissButton = { TextButton(onClick = { showDelete = false }) { Text("Cancel") } }
    )
}
