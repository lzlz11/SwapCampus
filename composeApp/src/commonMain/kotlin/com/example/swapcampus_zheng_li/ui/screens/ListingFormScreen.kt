package com.example.swapcampus_zheng_li.ui.screens

import com.example.swapcampus_zheng_li.composeapp.resources.*
import org.jetbrains.compose.resources.stringResource
import com.example.swapcampus_zheng_li.ui.i18n.*

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.swapcampus_zheng_li.*
import com.example.swapcampus_zheng_li.ui.viewmodel.ListingsViewModel

@Composable
fun ListingFormScreen(id: Int?, viewModel: ListingsViewModel, user: CompteUtilisateur?, onLogin: () -> Unit, onSaved: () -> Unit) {
    if (user == null) { AuthenticationRequired(onLogin); return }
    val existing = id?.let(viewModel::getListing)
    if (id != null && existing == null) { Text(stringResource(Res.string.not_found), Modifier.padding(16.dp)); return }
    if (existing != null && existing.auteur.id != user.id) { Text(stringResource(Res.string.access_denied), Modifier.padding(16.dp)); return }
    var title by rememberSaveable(id) { mutableStateOf(existing?.titre.orEmpty()) }
    var description by rememberSaveable(id) { mutableStateOf(existing?.description.orEmpty()) }
    var price by rememberSaveable(id) { mutableStateOf(existing?.prix?.toString().orEmpty()) }
    var categoryName by rememberSaveable(id) { mutableStateOf((existing?.categorie ?: Categorie.LIVRES).name) }
    var photoUrl by rememberSaveable(id) { mutableStateOf(existing?.photoUrl.orEmpty()) }
    var menuOpen by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<org.jetbrains.compose.resources.StringResource?>(null) }
    val category = Categorie.valueOf(categoryName)
    Column(Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(title, { title = it }, label = { Text(stringResource(Res.string.field_title)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(description, { description = it }, label = { Text(stringResource(Res.string.description)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(price, { price = it }, label = { Text(stringResource(Res.string.price)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
        Box {
            OutlinedButton(onClick = { menuOpen = true }) { Text(stringResource(Res.string.category, category.label())) }
            DropdownMenu(menuOpen, onDismissRequest = { menuOpen = false }) {
                Categorie.entries.forEach { item ->
                    DropdownMenuItem(text = { Text(item.label()) }, onClick = { categoryName = item.name; menuOpen = false })
                }
            }
        }
        OutlinedTextField(photoUrl, { photoUrl = it }, label = { Text(stringResource(Res.string.photo_url)) }, modifier = Modifier.fillMaxWidth())
        error?.let { Text(stringResource(it), color = MaterialTheme.colorScheme.error) }
        Button(onClick = {
            val number = price.trim().replace(',', '.').toDoubleOrNull()
            if (title.isBlank()) {
                error = Res.string.error_title
            } else if (number == null) {
                error = Res.string.invalid_price
            } else {
                viewModel.saveListing(id, title, description, number, category, photoUrl)
                    .onSuccess { onSaved() }
                    .onFailure { error = listingErrorResource(it.message) }
            }
        }, modifier = Modifier.fillMaxWidth()) { Text(if (id == null) stringResource(Res.string.publish) else stringResource(Res.string.save)) }
    }
}
