package com.example.swapcampus_zheng_li.ui

import com.example.swapcampus_zheng_li.repository.DemoData
import com.example.swapcampus_zheng_li.repository.AuthRepository
import com.example.swapcampus_zheng_li.repository.InMemoryAuthRepository
import com.example.swapcampus_zheng_li.repository.InMemoryListingRepository
import com.example.swapcampus_zheng_li.repository.ListingRepository

class AppContainer {
    val authRepository: AuthRepository = InMemoryAuthRepository()
    val listingRepository: ListingRepository = InMemoryListingRepository().apply {
        DemoData.listings().forEach { postListing(it).getOrThrow() }
    }
}
