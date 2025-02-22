package com.tuguego.app.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import com.tuguego.app.core.data.local.preferences.AppPreferences
import com.tuguego.app.core.domain.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(
    private val preferences: AppPreferences
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> get() = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password

    private val _rememberMe = MutableStateFlow(preferences.isRememberMeChecked())
    val rememberMe: StateFlow<Boolean> get() = _rememberMe

    private val _showForgotPasswordDialog = MutableStateFlow(false)
    val showForgotPasswordDialog: StateFlow<Boolean> get() = _showForgotPasswordDialog

    private val _response = MutableStateFlow<Result?>(null)
    val response: StateFlow<Result?> get() = _response

    init {
        if (preferences.isRememberMeChecked()) {
            val savedUsername = preferences.getRememberedUsername()
            val savedPassword = preferences.getRememberedPassword()

            _username.value = when (savedUsername != "no_user") {
                true -> savedUsername
                false -> ""
            }
            _password.value = when (savedPassword != "no_user") {
                true -> savedPassword
                false -> ""
            }
        }
    }


    fun onUsernameChange(value: String) {
        _username.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun toggleRememberMe() {
        _rememberMe.value = !_rememberMe.value
        preferences.toggleRememberMe()
    }

    fun toggleForgotPasswordDialog() {
        _showForgotPasswordDialog.value = !_showForgotPasswordDialog.value
    }

    fun login() {
        if (_username.value.trim().isBlank() || _password.value.trim().isBlank()) {
            _response.value = Result(
                success = false,
                message = "Username or password cannot be empty!"
            )
        }

        if (_rememberMe.value) {
            preferences.setUsernameToRemember(_username.value.trim())
            preferences.setPasswordToRemember(_password.value.trim())
        }
        _response.value = Result(
            success = true,
            message = "Login successful."
        )
    }
}