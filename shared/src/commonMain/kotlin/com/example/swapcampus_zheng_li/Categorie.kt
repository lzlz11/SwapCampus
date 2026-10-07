package com.example.swapcampus_zheng_li

enum class Categorie {
    LIVRES,
    COURS,
    ELECTRONIQUE,
    SPORT_LOISIRS,
    MOBILIER,
    VETEMENTS,
    AUTRE
}

fun Categorie.nomAffiche(): String {
    return when (this) {
        Categorie.LIVRES -> "Livres"
        Categorie.COURS -> "Cours"
        Categorie.ELECTRONIQUE -> "Électronique"
        Categorie.SPORT_LOISIRS -> "Sport & loisirs"
        Categorie.MOBILIER -> "Mobilier"
        Categorie.VETEMENTS -> "Vêtements"
        Categorie.AUTRE -> "Autre"
    }
}