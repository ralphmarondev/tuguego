package com.tuguego.app.features.auth.di

import com.tuguego.app.features.auth.presentation.login.LoginViewModel
import com.tuguego.app.features.auth.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    // login
    viewModelOf(::LoginViewModel)

    // register
    viewModelOf(::RegisterViewModel)
}