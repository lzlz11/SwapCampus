package com.example.swapcampus_zheng_li.ui.screens

import com.example.swapcampus_zheng_li.composeapp.resources.*
import org.jetbrains.compose.resources.stringResource
import com.example.swapcampus_zheng_li.ui.i18n.*

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.swapcampus_zheng_li.CompteUtilisateur
import com.example.swapcampus_zheng_li.ui.components.ListingCard
import com.example.swapcampus_zheng_li.ui.viewmodel.ListingsViewModel

@Composable
fun MyListingsScreen(viewModel: ListingsViewModel, user: CompteUtilisateur?, onLogin: () -> Unit, onListingClick: (Int) -> Unit) {
    if (user == null) { AuthenticationRequired(onLogin); return }
    val allListings by viewModel.allListings.collectAsStateWithLifecycle()
    val listings = allListings.filter { it.auteur.id == user.id }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (listings.isEmpty()) Text(stringResource(Res.string.empty_owned))
        LazyColumn(contentPadding = PaddingValues(bottom = 88.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listings, key = { it.id }) { listing -> ListingCard(listing) { onListingClick(listing.id) } }
        }
    }
}
