package com.example.swapcampus_zheng_li.repository

import com.example.swapcampus_zheng_li.CompteUtilisateur
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryAuthRepository : AuthRepository {

    private val utilisateurs = DemoData.accounts

    private val _currentUser =
        MutableStateFlow<CompteUtilisateur?>(null)

    override val currentUser = _currentUser.asStateFlow()

    override fun authenticate(
        email: String,
        password: String
    ): CompteUtilisateur? {
        val utilisateur = utilisateurs.find {
            it.email.equals(email, ignoreCase = true) &&
                    it.motDePasseHash == password &&
                    it.actif
        }

        _currentUser.value = utilisateur
        return utilisateur
    }

    override fun logout() {
        _currentUser.value = null
    }
}