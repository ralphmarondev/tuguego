package com.tuguego.app.navigation

import kotlinx.serialization.Serializable

object Routes {

    @Serializable
    data object Onboarding

    @Serializable
    data object Auth

    @Serializable
    data object Home
}