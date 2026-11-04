package com.example.swapcampus_zheng_li.ui.i18n
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.datetime.LocalDate
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Calendar
import java.util.Currency
import java.util.Locale
@Composable actual fun currentLanguageTag(): String = LocalConfiguration.current.locales[0].toLanguageTag()
actual fun formatPrice(price: Double, languageTag: String): String = NumberFormat.getCurrencyInstance(Locale.forLanguageTag(languageTag)).apply { currency = Currency.getInstance("EUR") }.format(price)
actual fun formatDate(date: LocalDate, languageTag: String): String {
 val calendar = Calendar.getInstance().apply { clear(); set(date.year, (date.month.ordinal + 1) - 1, date.day) }
 return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.forLanguageTag(languageTag)).format(calendar.time)
}
