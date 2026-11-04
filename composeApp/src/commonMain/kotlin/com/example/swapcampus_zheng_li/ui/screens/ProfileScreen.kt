package com.example.swapcampus_zheng_li.ui.screens

import com.example.swapcampus_zheng_li.composeapp.resources.*
import org.jetbrains.compose.resources.stringResource
import com.example.swapcampus_zheng_li.ui.i18n.*

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.swapcampus_zheng_li.CompteUtilisateur

@Composable
fun ProfileScreen(user: CompteUtilisateur?, onLogin: () -> Unit, onLogout: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        LanguageSelector()
        if (user == null) {
            Text(stringResource(Res.string.profile_logged_out))
            Button(onClick = onLogin) { Text(stringResource(Res.string.action_login)) }
        } else {
            Text(stringResource(Res.string.welcome_user, user.nom), style = MaterialTheme.typography.titleLarge)
            Text(user.email)
            Text(stringResource(if (user.role == com.example.swapcampus_zheng_li.RoleUtilisateur.ETUDIANT) Res.string.role_student else Res.string.role_admin))
            Text(stringResource(Res.string.member_since, formatDate(user.dateCreation, currentLanguageTag())))
            Button(onClick = onLogout) { Text(stringResource(Res.string.action_logout)) }
        }
    }
}

@Composable
fun AuthenticationRequired(onLogin: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(Res.string.login_required))
        Button(onClick = onLogin) { Text(stringResource(Res.string.action_login)) }
    }
}
