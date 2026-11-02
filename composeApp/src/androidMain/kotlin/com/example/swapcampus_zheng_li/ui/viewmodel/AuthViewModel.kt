package com.example.swapcampus_zheng_li.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.swapcampus_zheng_li.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class LoginError { EMPTY_FIELDS, INVALID_CREDENTIALS }

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    val currentUser = repository.currentUser
    private val _error = MutableStateFlow<LoginError?>(null)
    val error = _error.asStateFlow()

    fun login(email: String, password: String) {
        _error.value = when {
            email.isBlank() || password.isBlank() -> LoginError.EMPTY_FIELDS
            repository.authenticate(email.trim(), password) == null -> LoginError.INVALID_CREDENTIALS
            else -> null
        }
    }

    fun logout() {
        repository.logout()
        _error.value = null
    }
}
