package com.example.swapcampus_zheng_li

import kotlinx.datetime.LocalDateTime

data class ActionModeration(
    val id: Int,
    val cible: String,
    val type: TypeActionModeration,
    val motif: String,
    val administrateur: CompteUtilisateur,
    val date: LocalDateTime
)