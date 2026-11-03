package com.example.swapcampus_zheng_li

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swapcampus_zheng_li.ui.AppContainer
import com.example.swapcampus_zheng_li.ui.screens.LoginScreen
import com.example.swapcampus_zheng_li.ui.screens.WelcomeScreen
import com.example.swapcampus_zheng_li.ui.theme.SwapCampusTheme
import com.example.swapcampus_zheng_li.ui.viewmodel.AuthViewModel

@Composable
fun App() {
    SwapCampusTheme {
        Surface(
            modifier = Modifier.safeDrawingPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            val authViewModel = viewModel {
                AuthViewModel(AppContainer().authRepository)
            }
            val user by authViewModel.currentUser.collectAsStateWithLifecycle()

            val account = user
            if (account == null) {
                LoginScreen(viewModel = authViewModel, onLoggedIn = {})
            } else {
                WelcomeScreen(
                    name = account.nom,
                    onLogout = authViewModel::logout
                )
            }
        }
    }
}