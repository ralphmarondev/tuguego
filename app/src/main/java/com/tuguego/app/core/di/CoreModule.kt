package com.tuguego.app.core.di

import com.tuguego.app.core.data.local.preferences.AppPreferences
import org.koin.dsl.module

val coreModule = module {
    single { AppPreferences(get()) }
}