package com.example.swapcampus_zheng_li.ui.navigation

object Routes {
    const val LISTINGS = "listings"
    const val MY_LISTINGS = "myListings"
    const val PROFILE = "profile"
    const val LOGIN = "login"
    const val POST = "post"
    const val EDIT = "edit/{id}"
    const val DETAIL = "detail/{id}"
    fun edit(id: Int) = "edit/$id"
    fun detail(id: Int) = "detail/$id"
}
