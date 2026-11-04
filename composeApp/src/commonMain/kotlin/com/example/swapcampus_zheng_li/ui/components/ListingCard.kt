package com.example.swapcampus_zheng_li.ui.components

import com.example.swapcampus_zheng_li.composeapp.resources.*
import org.jetbrains.compose.resources.stringResource
import com.example.swapcampus_zheng_li.ui.i18n.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.swapcampus_zheng_li.*

@Composable
fun ListingPhoto(listing: Annonce, modifier: Modifier = Modifier) {
    listing.photoUrl?.let { url ->
        AsyncImage(model = url, contentDescription = listing.titre,
            placeholder = painterResource(Res.drawable.listing_placeholder),
            error = painterResource(Res.drawable.listing_placeholder),
            contentScale = ContentScale.Crop, modifier = modifier)
    } ?: Box(modifier.background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
        Text(stringResource(Res.string.no_photo), style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun ListingCard(listing: Annonce, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ListingPhoto(listing, Modifier.size(80.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(listing.titre, style = MaterialTheme.typography.titleMedium)
                Text(formatPrice(listing.prix, currentLanguageTag()))
                Text(listing.categorie.label())
                Text(when (listing.etat) {
                    Disponible -> stringResource(Res.string.available)
                    is Reservee -> stringResource(Res.string.reserved)
                    is Vendue -> stringResource(Res.string.sold)
                }, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
