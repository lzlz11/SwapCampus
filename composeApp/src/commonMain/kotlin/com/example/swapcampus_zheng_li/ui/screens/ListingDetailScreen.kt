package com.example.swapcampus_zheng_li.ui.screens

import com.example.swapcampus_zheng_li.composeapp.resources.*
import org.jetbrains.compose.resources.stringResource
import com.example.swapcampus_zheng_li.ui.i18n.*

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
    if (listing == null) { Text(stringResource(Res.string.not_found), Modifier.padding(16.dp)); return }
    val isOwner = listing.auteur.id == user?.id
    var showDelete by rememberSaveable(id) { mutableStateOf(false) }
    var error by remember { mutableStateOf<org.jetbrains.compose.resources.StringResource?>(null) }
    fun changeStatus(status: EtatAnnonce) {
        viewModel.changeStatus(id, status).onFailure { error = listingErrorResource(it.message) }
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(listing.titre, style = MaterialTheme.typography.headlineMedium)
        ListingPhoto(listing, Modifier.fillMaxWidth().height(140.dp))
        Text(listing.description)
        Text(formatPrice(listing.prix, currentLanguageTag()), style = MaterialTheme.typography.titleLarge)
        Text(listing.categorie.label())
        Text(stringResource(Res.string.owner, listing.auteur.nom))
        Text(stringResource(Res.string.created, formatDate(listing.dateCreation, currentLanguageTag())))
        Text(stringResource(Res.string.status, listing.etat.label()))
        when (listing.etat) {
            Disponible -> if (!isOwner) {
                if (user == null) Button(onClick = onLogin) { Text(stringResource(Res.string.login_reserve)) }
                else Button(onClick = { changeStatus(Reservee(user.nom)) }) { Text(stringResource(Res.string.reserve)) }
            }
            is Reservee -> if (isOwner) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { changeStatus(Vendue(todayDate())) }) { Text(stringResource(Res.string.mark_sold)) }
                    OutlinedButton(onClick = { changeStatus(Disponible) }) { Text(stringResource(Res.string.cancel)) }
                }
            }
            is Vendue -> if (isOwner) {
                Button(onClick = { changeStatus(Disponible) }) { Text(stringResource(Res.string.make_available)) }
            }
        }
        if (isOwner) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onEdit(id) }) { Text(stringResource(Res.string.edit)) }
                Button(onClick = { showDelete = true }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text(stringResource(Res.string.delete)) }
            }
        }
        error?.let { Text(stringResource(it), color = MaterialTheme.colorScheme.error) }
    }
    if (showDelete) AlertDialog(
        onDismissRequest = { showDelete = false }, title = { Text(stringResource(Res.string.delete_title)) },
        text = { Text(stringResource(Res.string.delete_hint)) },
        confirmButton = { TextButton(onClick = {
            viewModel.removeListing(id).onSuccess { showDelete = false; onDeleted() }
                .onFailure { showDelete = false; error = listingErrorResource(it.message) }
        }) { Text(stringResource(Res.string.delete)) } },
        dismissButton = { TextButton(onClick = { showDelete = false }) { Text(stringResource(Res.string.cancel)) } }
    )
}
