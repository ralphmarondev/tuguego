package com.tuguego.app.core.data.local.preferences

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(
    context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    companion object {
        private const val PREFERENCES_NAME = "app_preferences"
        private const val FIRST_LAUNCH = "first_launch"
        private const val DARK_THEME = "dark_theme"
        private const val REMEMBER_ME = "remember_me"
        private const val CURRENT_USER_USERNAME = "current_user_username"
        private const val CURRENT_USER_PASSWORD = "current_user_password"
    }

    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(FIRST_LAUNCH, true)
    }

    fun setFirstLaunchAsDone() {
        sharedPreferences.edit().putBoolean(FIRST_LAUNCH, false).apply()
    }

    fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME, false)
    }

    fun toggleDarkTheme() {
        sharedPreferences.edit().putBoolean(DARK_THEME, !isDarkTheme()).apply()
    }

    fun toggleRememberMe() {
        sharedPreferences.edit().putBoolean(REMEMBER_ME, !isRememberMeChecked()).apply()
    }

    fun isRememberMeChecked(): Boolean {
        return sharedPreferences.getBoolean(REMEMBER_ME, true)
    }

    fun setUsernameToRemember(value: String) {
        sharedPreferences.edit().putString(CURRENT_USER_USERNAME, value).apply()
    }

    fun getRememberedUsername(): String {
        return sharedPreferences.getString(CURRENT_USER_USERNAME, "no_user")!!
    }

    fun setPasswordToRemember(value: String) {
        sharedPreferences.edit().putString(CURRENT_USER_PASSWORD, value).apply()
    }

    fun getRememberedPassword(): String {
        return sharedPreferences.getString(CURRENT_USER_PASSWORD, "no_user")!!
    }
}