package com.example.swapcampus_zheng_li.ui.i18n

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.swapcampus_zheng_li.composeapp.R
import com.example.swapcampus_zheng_li.ui.viewmodel.LoginError

@Composable
fun LoginError.text(): String = stringResource(when (this) {
    LoginError.EMPTY_FIELDS -> R.string.login_error_empty_fields
    LoginError.INVALID_CREDENTIALS -> R.string.login_error_invalid_credentials
})
