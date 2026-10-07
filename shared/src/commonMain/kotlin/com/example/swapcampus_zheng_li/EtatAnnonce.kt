package com.example.swapcampus_zheng_li
import kotlinx.datetime.LocalDate
sealed class EtatAnnonce

data object Disponible : EtatAnnonce()

data class Reservee(
    val acheteur: String
) : EtatAnnonce()
data class Vendue(
    val dateVente: LocalDate
) : EtatAnnonce()

fun EtatAnnonce.description(): String {
    return when (this) {
        Disponible -> "Disponible"
        is Reservee -> "Réservée par $acheteur"
        is Vendue -> "Vendue le $dateVente"
    }
}