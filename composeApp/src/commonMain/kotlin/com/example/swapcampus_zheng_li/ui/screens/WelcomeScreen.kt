package com.example.swapcampus_zheng_li.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.swapcampus_zheng_li.platform
import com.example.swapcampus_zheng_li.composeapp.resources.Res
import com.example.swapcampus_zheng_li.composeapp.resources.welcome_logout
import com.example.swapcampus_zheng_li.composeapp.resources.welcome_platform
import com.example.swapcampus_zheng_li.composeapp.resources.welcome_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun WelcomeScreen(
    name: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(Res.string.welcome_title, name),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            stringResource(Res.string.welcome_platform, platform()),
            style = MaterialTheme.typography.bodyLarge
        )
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.welcome_logout))
        }
    }
}