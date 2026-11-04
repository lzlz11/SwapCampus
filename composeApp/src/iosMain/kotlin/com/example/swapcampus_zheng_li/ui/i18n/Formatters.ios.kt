@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.example.swapcampus_zheng_li.ui.i18n
import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import platform.Foundation.*
@Composable actual fun currentLanguageTag(): String = NSLocale.currentLocale.localeIdentifier
actual fun formatPrice(price: Double, languageTag: String): String = NSNumberFormatter().apply {
 numberStyle = NSNumberFormatterCurrencyStyle
 locale = NSLocale(languageTag)
 currencyCode = "EUR"
}.stringFromNumber(NSNumber(price)) ?: price.toString()
actual fun formatDate(date: LocalDate, languageTag: String): String {
 val components = NSDateComponents().apply { year = date.year.toLong(); month = (date.month.ordinal + 1).toLong(); day = date.day.toLong() }
 val value = NSCalendar.currentCalendar.dateFromComponents(components) ?: return date.toString()
 return NSDateFormatter().apply { locale = NSLocale(languageTag); dateStyle = NSDateFormatterMediumStyle }.stringFromDate(value)
}
