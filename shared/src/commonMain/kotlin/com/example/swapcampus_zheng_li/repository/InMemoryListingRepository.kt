package com.example.swapcampus_zheng_li.repository

import com.example.swapcampus_zheng_li.Annonce
import com.example.swapcampus_zheng_li.CatalogueAnnonces
import com.example.swapcampus_zheng_li.Categorie
import com.example.swapcampus_zheng_li.EtatAnnonce

class InMemoryListingRepository(
    private val catalogue: CatalogueAnnonces = CatalogueAnnonces()
) : ListingRepository {

    override fun allListings(): List<Annonce> {
        return catalogue.toutesLesAnnonces()
    }

    override fun searchListings(
        keyword: String?,
        category: Categorie?
    ): List<Annonce> {
        return catalogue.rechercherAnnonces(keyword, category)
    }

    override fun getListing(id: Int): Annonce? {
        return catalogue.trouverAnnonce(id)
    }

    override fun postListing(listing: Annonce): Result<Annonce> {
        return runCatching {
            require(listing.titre.isNotBlank()) {
                "Le titre est obligatoire."
            }
            require(listing.prix > 0) {
                "Le prix doit être supérieur à zéro."
            }

            catalogue.publierAnnonce(listing)
        }
    }

    override fun updateListing(listing: Annonce): Result<Annonce> {
        return runCatching {
            require(listing.titre.isNotBlank()) {
                "Le titre est obligatoire."
            }
            require(listing.prix > 0) {
                "Le prix doit être supérieur à zéro."
            }

            catalogue.modifierAnnonce(listing.id, listing)
                ?: error("Annonce introuvable.")
        }
    }

    override fun changeStatus(
        id: Int,
        status: EtatAnnonce
    ): Result<Annonce> {
        return runCatching {
            catalogue.changerEtat(id, status)
                ?: error("Annonce introuvable.")
        }
    }

    override fun removeListing(id: Int): Boolean {
        return catalogue.retirerAnnonce(id)
    }
}