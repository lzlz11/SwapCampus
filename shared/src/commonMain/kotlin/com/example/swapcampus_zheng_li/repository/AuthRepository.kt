package com.example.swapcampus_zheng_li.repository

import com.example.swapcampus_zheng_li.CompteUtilisateur
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    val currentUser: StateFlow<CompteUtilisateur?>

    fun authenticate(
        email: String,
        password: String
    ): CompteUtilisateur?

    fun logout()
}