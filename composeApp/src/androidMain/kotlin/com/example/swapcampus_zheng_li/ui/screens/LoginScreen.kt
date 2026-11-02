package com.example.swapcampus_zheng_li.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.swapcampus_zheng_li.composeapp.R
import com.example.swapcampus_zheng_li.ui.i18n.text
import com.example.swapcampus_zheng_li.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel, onLoggedIn: () -> Unit) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(user?.id) { if (user != null) onLoggedIn() }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(R.string.login_title), style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = email, onValueChange = { email = it }, label = { Text(stringResource(R.string.login_email)) },
            singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password, onValueChange = { password = it }, label = { Text(stringResource(R.string.login_password)) },
            singleLine = true, visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        error?.let { Text(it.text(), color = MaterialTheme.colorScheme.error) }
        Button(onClick = { viewModel.login(email, password) }) { Text(stringResource(R.string.action_login)) }
        Text(stringResource(R.string.login_demo_accounts))
    }
}
