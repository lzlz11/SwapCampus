package com.example.swapcampus_zheng_li

class CatalogueAnnonces {

    private val annonces = mutableListOf<Annonce>()

    fun toutesLesAnnonces(): List<Annonce> {
        return annonces.toList()
    }

    fun trouverAnnonce(id: Int): Annonce? {
        return annonces.find { it.id == id }
    }

    fun publierAnnonce(annonce: Annonce): Annonce {
        require(annonces.none { it.id == annonce.id }) {
            "Une annonce avec l'identifiant ${annonce.id} existe déjà."
        }

        return annonce.apply {
            annonces.add(this)
        }
    }

    fun rechercherAnnonces(
        motCle: String? = null,
        categorie: Categorie? = null
    ): List<Annonce> {
        return annonces.filter { annonce ->
            val correspondAuMotCle =
                motCle.isNullOrBlank() ||
                        annonce.titre.contains(motCle, ignoreCase = true) ||
                        annonce.description.contains(motCle, ignoreCase = true)

            val correspondALaCategorie =
                categorie == null || annonce.categorie == categorie

            correspondAuMotCle && correspondALaCategorie
        }
    }

    fun modifierAnnonce(
        id: Int,
        nouvelleAnnonce: Annonce
    ): Annonce? {
        val index = annonces.indexOfFirst { it.id == id }

        if (index == -1) {
            return null
        }

        val annonceModifiee = nouvelleAnnonce.copy(id = id)
        annonces[index] = annonceModifiee

        return annonceModifiee
    }

    fun changerEtat(
        id: Int,
        nouvelEtat: EtatAnnonce
    ): Annonce? {
        val index = annonces.indexOfFirst { it.id == id }

        if (index == -1) {
            return null
        }

        val annonceModifiee = annonces[index].copy(etat = nouvelEtat)
        annonces[index] = annonceModifiee

        return annonceModifiee
    }

    fun retirerAnnonce(id: Int): Boolean {
        return annonces.removeAll { it.id == id }
    }
}

fun List<Annonce>.disponibles(): List<Annonce> {
    return filter { it.etat == Disponible }
}