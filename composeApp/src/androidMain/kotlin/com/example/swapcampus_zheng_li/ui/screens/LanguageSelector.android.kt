package com.example.swapcampus_zheng_li.ui.screens
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import com.example.swapcampus_zheng_li.composeapp.resources.*
import com.example.swapcampus_zheng_li.ui.i18n.currentLanguageTag
import org.jetbrains.compose.resources.stringResource
@Composable
actual fun LanguageSelector() {
 currentLanguageTag() // Recompose when the configuration changes.
 val selected = AppCompatDelegate.getApplicationLocales().toLanguageTags()
 Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
  Text(stringResource(Res.string.language_title), style = MaterialTheme.typography.titleMedium)
  listOf("" to stringResource(Res.string.language_system), "en" to "English", "fr" to "Français").forEach { (tag, label) ->
   FilterChip(selected = if(tag.isEmpty()) selected.isEmpty() else selected.startsWith(tag),
    onClick = { AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag)) }, label = { Text(label) })
  }
 }
}
