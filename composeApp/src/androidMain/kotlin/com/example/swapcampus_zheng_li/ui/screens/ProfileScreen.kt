package com.example.swapcampus_zheng_li.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.swapcampus_zheng_li.CompteUtilisateur
import com.example.swapcampus_zheng_li.composeapp.R

@Composable
fun ProfileScreen(user: CompteUtilisateur?, onLogin: () -> Unit, onLogout: () -> Unit) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(R.string.profile_title), style = MaterialTheme.typography.headlineMedium)
        if (user == null) {
            Text(stringResource(R.string.profile_logged_out))
            Button(onClick = onLogin) { Text(stringResource(R.string.action_login)) }
        } else {
            Text(stringResource(R.string.welcome_user, user.nom), style = MaterialTheme.typography.titleLarge)
            Text(user.email)
            Text(user.role.name)
            Button(onClick = onLogout) { Text(stringResource(R.string.action_logout)) }
        }
    }
}

@Composable
fun AuthenticationRequired(onLogin: () -> Unit) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(R.string.login_required))
        Button(onClick = onLogin) { Text(stringResource(R.string.action_login)) }
    }
}
