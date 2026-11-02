package com.example.swapcampus_zheng_li.ui.screens

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
    if (id != null && existing == null) { Text("Listing not found.", Modifier.padding(16.dp)); return }
    if (existing != null && existing.auteur.id != user.id) { Text("Access denied.", Modifier.padding(16.dp)); return }
    var title by rememberSaveable(id) { mutableStateOf(existing?.titre.orEmpty()) }
    var description by rememberSaveable(id) { mutableStateOf(existing?.description.orEmpty()) }
    var price by rememberSaveable(id) { mutableStateOf(existing?.prix?.toString().orEmpty()) }
    var categoryName by rememberSaveable(id) { mutableStateOf((existing?.categorie ?: Categorie.LIVRES).name) }
    var photoUrl by rememberSaveable(id) { mutableStateOf(existing?.photoUrl.orEmpty()) }
    var menuOpen by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val category = Categorie.valueOf(categoryName)
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(if (id == null) "Post listing" else "Edit listing", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(title, { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(description, { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(price, { price = it }, label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
        Box {
            OutlinedButton(onClick = { menuOpen = true }) { Text("Category: ${category.nomAffiche()}") }
            DropdownMenu(menuOpen, onDismissRequest = { menuOpen = false }) {
                Categorie.entries.forEach { item ->
                    DropdownMenuItem(text = { Text(item.nomAffiche()) }, onClick = { categoryName = item.name; menuOpen = false })
                }
            }
        }
        OutlinedTextField(photoUrl, { photoUrl = it }, label = { Text("Photo URL (optional)") }, modifier = Modifier.fillMaxWidth())
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = {
            val number = price.toDoubleOrNull()
            if (number == null) {
                error = "Enter a valid price."
            } else {
                viewModel.saveListing(id, title, description, number, category, photoUrl)
                    .onSuccess { onSaved() }
                    .onFailure { error = it.message ?: "Unable to save listing." }
            }
        }, modifier = Modifier.fillMaxWidth()) { Text(if (id == null) "Publish" else "Save") }
    }
}
