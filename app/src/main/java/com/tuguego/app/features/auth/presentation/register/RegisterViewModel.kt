package com.tuguego.app.features.auth.presentation.register

import androidx.lifecycle.ViewModel
import com.tuguego.app.core.domain.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterViewModel : ViewModel() {
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> get() = _fullName

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> get() = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> get() = _confirmPassword

    private val _response = MutableStateFlow<Result?>(null)
    val response: StateFlow<Result?> get() = _response


    fun onFullNameChange(value: String) {
        _fullName.value = value
    }

    fun onUsernameChange(value: String) {
        _username.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onConfirmPasswordChange(value: String) {
        _confirmPassword.value = value
    }

    fun register() {
        if (_username.value.trim().isBlank() || _password.value.trim().isBlank()) {
            _response.value = Result(
                success = false,
                message = "Username or password cannot be empty!"
            )
        }

        _response.value = Result(
            success = true,
            message = "Registration successful."
        )
    }
}