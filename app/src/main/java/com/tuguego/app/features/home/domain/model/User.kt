package com.tuguego.app.features.home.domain.model

data class User(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val iconRes: Int
)