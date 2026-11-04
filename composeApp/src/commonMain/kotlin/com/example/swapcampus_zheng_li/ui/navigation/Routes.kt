package com.example.swapcampus_zheng_li.ui.navigation

import kotlinx.serialization.Serializable

@Serializable data object ListingsRoute
@Serializable data object MyListingsRoute
@Serializable data object ProfileRoute
@Serializable data object LoginRoute
@Serializable data object PostRoute
@Serializable data class DetailRoute(val id: Int)
@Serializable data class EditRoute(val id: Int)
