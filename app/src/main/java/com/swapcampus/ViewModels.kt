package com.swapcampus

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import shared.*

sealed class UiState {
    object Loading : UiState()
    data class Success(val listings: List<Annonce>) : UiState()
    data class Error(val message: String) : UiState()
}

class ListingsViewModel(private val repository: ListingRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init { loadListings() }

    fun loadListings() {
        _uiState.value = UiState.Loading
        try {
            _uiState.value = UiState.Success(repository.allListings())
        } catch (e: Exception) {
            _uiState.value = UiState.Error("Failed to load")
        }
    }

    fun search(query: String) {
        _uiState.value = UiState.Loading
        val results = repository.searchListings(query, null)
        if (results.isEmpty()) _uiState.value = UiState.Error("No results found.")
        else _uiState.value = UiState.Success(results)
    }

    fun postListing(annonce: Annonce) { repository.postListing(annonce); loadListings() }
    fun changeStatus(id: String, status: EtatAnnonce) { repository.changeStatus(id, status); loadListings() }
    fun removeListing(id: String) { repository.removeListing(id); loadListings() }
    fun getListingById(id: String) = repository.allListings().find { it.id == id }
}
