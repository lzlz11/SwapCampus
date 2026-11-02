package com.example.swapcampus_zheng_li.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapcampus_zheng_li.*
import com.example.swapcampus_zheng_li.repository.AuthRepository
import com.example.swapcampus_zheng_li.repository.ListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListingsViewModel(
    private val repository: ListingRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _allListings = MutableStateFlow<List<Annonce>>(emptyList())
    val allListings = _allListings.asStateFlow()
    private val _keyword = MutableStateFlow("")
    val keyword = _keyword.asStateFlow()
    private val _category = MutableStateFlow<Categorie?>(null)
    val category = _category.asStateFlow()

    init { loadListings() }

    fun loadListings() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                _allListings.value = repository.allListings()
                applyFilters()
            } catch (exception: Exception) {
                _uiState.value = UiState.Error(exception.message ?: "Unable to load listings.")
            }
        }
    }

    fun search(keyword: String, category: Categorie?) {
        _keyword.value = keyword
        _category.value = category
        applyFilters()
    }

    private fun applyFilters() {
        _uiState.value = try {
            UiState.Success(repository.searchListings(_keyword.value.trim().ifBlank { null }, _category.value))
        } catch (exception: Exception) {
            UiState.Error(exception.message ?: "Unable to search listings.")
        }
    }

    fun getListing(id: Int): Annonce? = repository.getListing(id)

    fun saveListing(
        id: Int?, title: String, description: String, price: Double,
        category: Categorie, photoUrl: String
    ): Result<Annonce> = runCatching {
        val user = authRepository.currentUser.value ?: error("Please log in first.")
        val existing = id?.let { repository.getListing(it) ?: error("Listing not found.") }
        require(existing == null || existing.auteur.id == user.id) { "Only the owner can edit this listing." }
        val listing = if (existing == null) {
            Annonce(
                id = (repository.allListings().maxOfOrNull { it.id } ?: 0) + 1,
                titre = title.trim(), description = description.trim(), prix = price,
                categorie = category, photoUrl = photoUrl.trim().ifBlank { null },
                auteur = user, etat = Disponible, dateCreation = todayDate()
            )
        } else {
            existing.copy(titre = title.trim(), description = description.trim(), prix = price,
                categorie = category, photoUrl = photoUrl.trim().ifBlank { null })
        }
        if (existing == null) repository.postListing(listing).getOrThrow()
        else repository.updateListing(listing).getOrThrow()
    }.onSuccess { loadListings() }

    fun changeStatus(id: Int, status: EtatAnnonce): Result<Annonce> = runCatching {
        val user = authRepository.currentUser.value ?: error("Please log in first.")
        val listing = repository.getListing(id) ?: error("Listing not found.")
        when (status) {
            is Reservee -> {
                require(listing.etat == Disponible && listing.auteur.id != user.id) { "This listing cannot be reserved." }
                require(status.acheteur == user.nom) { "Invalid buyer." }
            }
            is Vendue -> require(listing.auteur.id == user.id && listing.etat is Reservee) { "Only the owner can complete a reservation." }
            Disponible -> require(listing.auteur.id == user.id && listing.etat != Disponible) { "Only the owner can cancel or reopen a listing." }
        }
        repository.changeStatus(id, status).getOrThrow()
    }.onSuccess { loadListings() }

    fun removeListing(id: Int): Result<Unit> = runCatching {
        val user = authRepository.currentUser.value ?: error("Please log in first.")
        val listing = repository.getListing(id) ?: error("Listing not found.")
        require(listing.auteur.id == user.id) { "Only the owner can delete this listing." }
        check(repository.removeListing(id)) { "Listing not found." }
    }.onSuccess { loadListings() }
}
