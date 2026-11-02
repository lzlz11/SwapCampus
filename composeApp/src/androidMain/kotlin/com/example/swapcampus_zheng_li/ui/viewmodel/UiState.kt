package com.example.swapcampus_zheng_li.ui.viewmodel

import com.example.swapcampus_zheng_li.Annonce

sealed interface UiState {
    data object Loading : UiState
    data class Success(val listings: List<Annonce>) : UiState
    data class Error(val message: String) : UiState
}
