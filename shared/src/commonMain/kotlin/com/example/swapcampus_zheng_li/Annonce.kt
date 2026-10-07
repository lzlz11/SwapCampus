package com.example.swapcampus_zheng_li

import kotlinx.datetime.LocalDate

data class Annonce(
    val id: Int,
    val titre: String,
    val description: String,
    val prix: Double,
    val categorie: Categorie,
    val photoUrl: String?,
    val auteur: CompteUtilisateur,
    val etat: EtatAnnonce,
    val dateCreation: LocalDate
)

fun Annonce.photoAffichee(): String {
    return photoUrl ?: "Aucune photo"
}