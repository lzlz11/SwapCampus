package shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// TD2 In-Memory Catalog[cite: 1]
class CatalogueAnnonces {
    private val listings = mutableListOf<Annonce>()

    fun allListings(): List<Annonce> = listings.toList()

    fun searchListings(query: String?, category: Categorie?): List<Annonce> {
        return listings.filter { 
            (query == null || it.title.contains(query, true)) && 
            (category == null || it.category == category) 
        }
    }

    fun postListing(listing: Annonce) {
        require(listing.title.isNotBlank()) { "Title cannot be empty" }
        require(listing.price > 0) { "Price must be > 0" }
        listings.add(listing)
    }

    fun updateListing(updated: Annonce) {
        val index = listings.indexOfFirst { it.id == updated.id }
        if (index != -1) listings[index] = updated
    }

    fun changeStatus(id: String, status: EtatAnnonce) {
        val index = listings.indexOfFirst { it.id == id }
        if (index != -1) listings[index] = listings[index].copy(status = status)
    }

    fun removeListing(id: String) { 
        listings.removeIf { it.id == id } 
    }
}

// TP1 Listing Repository Interface[cite: 3]
interface ListingRepository {
    fun allListings(): List<Annonce>
    fun searchListings(query: String?, category: Categorie?): List<Annonce>
    fun postListing(listing: Annonce)
    fun updateListing(updated: Annonce)
    fun changeStatus(id: String, status: EtatAnnonce)
    fun removeListing(id: String)
}

// TP1 Listing Repository Implementation delegating to Catalog[cite: 3]
class ListingRepositoryImpl(private val catalog: CatalogueAnnonces) : ListingRepository {
    override fun allListings() = catalog.allListings()
    override fun searchListings(query: String?, category: Categorie?) = catalog.searchListings(query, category)
    override fun postListing(listing: Annonce) = catalog.postListing(listing)
    override fun updateListing(updated: Annonce) = catalog.updateListing(updated)
    override fun changeStatus(id: String, status: EtatAnnonce) = catalog.changeStatus(id, status)
    override fun removeListing(id: String) = catalog.remove
