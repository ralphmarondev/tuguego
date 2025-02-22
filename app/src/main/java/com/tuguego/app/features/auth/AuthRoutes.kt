package com.tuguego.app.features.auth

import kotlinx.serialization.Serializable

object AuthRoutes {

    @Serializable
    data object Location

    @Serializable
    data object Welcome

    @Serializable
    data object Login

    @Serializable
    data object Register
}