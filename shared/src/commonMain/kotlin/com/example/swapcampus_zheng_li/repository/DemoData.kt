package com.example.swapcampus_zheng_li.repository

import com.example.swapcampus_zheng_li.*
import kotlinx.datetime.LocalDate

object DemoData {
    val accounts: List<CompteUtilisateur> = listOf(
        CompteUtilisateur(1, "Etudiant 1", "student1@isep.fr", "student123", LocalDate(2026, 1, 1), RoleUtilisateur.ETUDIANT, true),
        CompteUtilisateur(2, "Etudiant 2", "student2@isep.fr", "student123", LocalDate(2026, 1, 1), RoleUtilisateur.ETUDIANT, true),
        CompteUtilisateur(3, "Administrateur", "admin@isep.fr", "admin123", LocalDate(2026, 1, 1), RoleUtilisateur.ADMINISTRATEUR, true),
        CompteUtilisateur(4, "Ada", "ada@isep.fr", "ada123", LocalDate(2026, 1, 1), RoleUtilisateur.ETUDIANT, true)
    )

    fun listings(): List<Annonce> = listOf(
        Annonce(1, "Kotlin textbook", "A textbook for the mobile development course.", 15.0, Categorie.LIVRES, null, accounts[3], Disponible, LocalDate(2026, 10, 1)),
        Annonce(2, "Desk lamp", "A working LED lamp for a student room.", 12.0, Categorie.MOBILIER, null, accounts[0], Disponible, LocalDate(2026, 10, 2)),
        Annonce(3, "Badminton racket", "A beginner racket with its cover.", 20.0, Categorie.SPORT_LOISIRS, null, accounts[1], Disponible, LocalDate(2026, 10, 3))
    )
}
