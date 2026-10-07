package com.example.swapcampus_zheng_li.repository

import com.example.swapcampus_zheng_li.CompteUtilisateur
import com.example.swapcampus_zheng_li.RoleUtilisateur
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate

class InMemoryAuthRepository : AuthRepository {

    private val utilisateurs = listOf(
        CompteUtilisateur(
            id = 1,
            nom = "Etudiant 1",
            email = "student1@isep.fr",
            motDePasseHash = "student123",
            dateCreation = LocalDate(2026, 1, 1),
            role = RoleUtilisateur.ETUDIANT,
            actif = true
        ),
        CompteUtilisateur(
            id = 2,
            nom = "Etudiant 2",
            email = "student2@isep.fr",
            motDePasseHash = "student123",
            dateCreation = LocalDate(2026, 1, 1),
            role = RoleUtilisateur.ETUDIANT,
            actif = true
        ),
        CompteUtilisateur(
            id = 3,
            nom = "Administrateur",
            email = "admin@isep.fr",
            motDePasseHash = "admin123",
            dateCreation = LocalDate(2026, 1, 1),
            role = RoleUtilisateur.ADMINISTRATEUR,
            actif = true
        )
    )

    private val _currentUser =
        MutableStateFlow<CompteUtilisateur?>(null)

    override val currentUser: StateFlow<CompteUtilisateur?> =
        _currentUser

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