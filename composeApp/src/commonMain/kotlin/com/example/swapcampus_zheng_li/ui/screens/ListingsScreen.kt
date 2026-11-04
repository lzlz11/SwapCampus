package com.example.swapcampus_zheng_li.ui.screens

import com.example.swapcampus_zheng_li.composeapp.resources.*
import org.jetbrains.compose.resources.stringResource
import com.example.swapcampus_zheng_li.ui.i18n.*

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
import com.example.swapcampus_zheng_li.ui.components.ListingCard
import com.example.swapcampus_zheng_li.ui.viewmodel.ListingsViewModel
import com.example.swapcampus_zheng_li.ui.viewmodel.UiState

@Composable
fun ListingsScreen(viewModel: ListingsViewModel, onListingClick: (Int) -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val keyword by viewModel.keyword.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.category.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = keyword,
            onValueChange = { viewModel.search(it, selectedCategory) },
            label = { Text(stringResource(Res.string.search)) }, singleLine = true, modifier = Modifier.fillMaxWidth())
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(selected = selectedCategory == null,
                    onClick = { viewModel.search(keyword, null) }, label = { Text(stringResource(Res.string.all_categories)) })
            }
            items(Categorie.entries) { category ->
                FilterChip(selected = selectedCategory == category,
                    onClick = { viewModel.search(keyword, category) }, label = { Text(category.label()) })
            }
        }
        when (val result = state) {
            UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is UiState.Error -> Column {
                Text(stringResource(listingErrorResource(result.message)), color = MaterialTheme.colorScheme.error)
                Button(onClick = viewModel::loadListings) { Text(stringResource(Res.string.retry)) }
            }
            is UiState.Success -> if (result.listings.isEmpty()) {
                Text(stringResource(Res.string.empty_listings))
            } else {
                LazyColumn(contentPadding = PaddingValues(bottom = 88.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(result.listings, key = { it.id }) { listing ->
                        ListingCard(listing, onClick = { onListingClick(listing.id) })
                    }
                }
            }
        }
    }
}
