package com.example.swapcampus_zheng_li.ui.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapcampus_zheng_li.Categorie
import com.example.swapcampus_zheng_li.repository.ListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListingsViewModel(
    private val repository: ListingRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ListingsUiState>(ListingsUiState.Loading)

    val uiState: StateFlow<ListingsUiState> =
        _uiState.asStateFlow()

    init {
        loadListings()
    }

    fun loadListings() {
        viewModelScope.launch {
            try {
                val listings = repository.allListings()
                _uiState.value = ListingsUiState.Success(listings)
            } catch (exception: Exception) {
                _uiState.value = ListingsUiState.Error(
                    exception.message ?: "Erreur inconnue"
                )
            }
        }
    }

    fun search(
        keyword: String?,
        category: Categorie?
    ) {
        viewModelScope.launch {
            val listings = repository.searchListings(
                keyword = keyword,
                category = category
            )
            _uiState.value = ListingsUiState.Success(listings)
        }
    }
}