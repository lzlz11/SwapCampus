package com.example.swapcampus_zheng_li.ui.listings

import com.example.swapcampus_zheng_li.Annonce

sealed interface ListingsUiState {
    data object Loading : ListingsUiState

    data class Success(
        val listings: List<Annonce>
    ) : ListingsUiState

    data class Error(
        val message: String
    ) : ListingsUiState
}