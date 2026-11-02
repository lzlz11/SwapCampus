package com.example.swapcampus_zheng_li.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.swapcampus_zheng_li.Categorie
import com.example.swapcampus_zheng_li.nomAffiche
import com.example.swapcampus_zheng_li.ui.components.ListingCard
import com.example.swapcampus_zheng_li.ui.viewmodel.ListingsViewModel
import com.example.swapcampus_zheng_li.ui.viewmodel.UiState

@Composable
fun ListingsScreen(viewModel: ListingsViewModel, onListingClick: (Int) -> Unit, onPostClick: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val keyword by viewModel.keyword.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.category.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Listings", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onPostClick) { Text("Post listing") }
        }
        OutlinedTextField(value = keyword,
            onValueChange = { viewModel.search(it, selectedCategory) },
            label = { Text("Search") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(selected = selectedCategory == null,
                    onClick = { viewModel.search(keyword, null) }, label = { Text("All") })
            }
            items(Categorie.entries) { category ->
                FilterChip(selected = selectedCategory == category,
                    onClick = { viewModel.search(keyword, category) }, label = { Text(category.nomAffiche()) })
            }
        }
        when (val result = state) {
            UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is UiState.Error -> Column {
                Text(result.message, color = MaterialTheme.colorScheme.error)
                Button(onClick = viewModel::loadListings) { Text("Retry") }
            }
            is UiState.Success -> if (result.listings.isEmpty()) {
                Text("No listings found.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(result.listings, key = { it.id }) { listing ->
                        ListingCard(listing, onClick = { onListingClick(listing.id) })
                    }
                }
            }
        }
    }
}
