package com.example.swapcampus_zheng_li.repository

import com.example.swapcampus_zheng_li.Annonce
import com.example.swapcampus_zheng_li.Categorie
import com.example.swapcampus_zheng_li.EtatAnnonce

interface ListingRepository {

    fun allListings(): List<Annonce>

    fun searchListings(
        keyword: String? = null,
        category: Categorie? = null
    ): List<Annonce>

    fun getListing(id: Int): Annonce?

    fun postListing(listing: Annonce): Result<Annonce>

    fun updateListing(listing: Annonce): Result<Annonce>

    fun changeStatus(
        id: Int,
        status: EtatAnnonce
    ): Result<Annonce>

    fun removeListing(id: Int): Boolean
}