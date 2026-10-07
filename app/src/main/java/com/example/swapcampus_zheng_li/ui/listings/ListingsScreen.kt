package com.example.swapcampus_zheng_li.ui.listings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.swapcampus_zheng_li.Annonce
import com.example.swapcampus_zheng_li.Categorie

@Composable
fun ListingsScreen(
    viewModel: ListingsViewModel,
    onListingClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var keyword by remember {
        mutableStateOf("")
    }

    var selectedCategory by remember {
        mutableStateOf<Categorie?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = keyword,
            onValueChange = { newKeyword ->
                keyword = newKeyword

                viewModel.search(
                    keyword = newKeyword.ifBlank { null },
                    category = selectedCategory
                )
            },
            label = {
                Text("Rechercher")
            },
            modifier = Modifier.fillMaxWidth()
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = {
                        selectedCategory = null

                        viewModel.search(
                            keyword = keyword.ifBlank { null },
                            category = null
                        )
                    },
                    label = {
                        Text("Toutes")
                    }
                )
            }

            items(Categorie.entries) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        selectedCategory = category

                        viewModel.search(
                            keyword = keyword.ifBlank { null },
                            category = category
                        )
                    },
                    label = {
                        Text(category.name)
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                ListingsUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is ListingsUiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.message)

                        Button(
                            onClick = viewModel::loadListings,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Réessayer")
                        }
                    }
                }

                is ListingsUiState.Success -> {
                    if (state.listings.isEmpty()) {
                        Text("Aucune annonce trouvée.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = state.listings,
                                key = { listing ->
                                    listing.id
                                }
                            ) { listing ->
                                ListingCard(
                                    listing = listing,
                                    onClick = {
                                        onListingClick(listing.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ListingCard(
    listing: Annonce,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = listing.titre,
                fontWeight = FontWeight.Bold
            )

            Text(text = "${listing.prix} €")
            Text(text = listing.categorie.name)
        }
    }
}