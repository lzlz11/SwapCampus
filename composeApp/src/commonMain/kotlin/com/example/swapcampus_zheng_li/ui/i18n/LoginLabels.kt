package com.example.swapcampus_zheng_li.ui.i18n

import androidx.compose.runtime.Composable
import com.example.swapcampus_zheng_li.composeapp.resources.Res
import com.example.swapcampus_zheng_li.composeapp.resources.login_error_empty_fields
import com.example.swapcampus_zheng_li.composeapp.resources.login_error_invalid_credentials
import com.example.swapcampus_zheng_li.ui.viewmodel.LoginError
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginError.text(): String = stringResource(
    when (this) {
        LoginError.EMPTY_FIELDS -> Res.string.login_error_empty_fields
        LoginError.INVALID_CREDENTIALS -> Res.string.login_error_invalid_credentials
    }
)
