package com.example.swapcampus_zheng_li.ui.i18n
import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
@Composable expect fun currentLanguageTag(): String
expect fun formatPrice(price: Double, languageTag: String): String
expect fun formatDate(date: LocalDate, languageTag: String): String
