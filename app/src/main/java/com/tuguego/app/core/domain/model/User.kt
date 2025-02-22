package com.tuguego.app.core.domain.model

data class Vechile(
    val id: Int = 0, // 1
    val name: String, // taxi, tricy
)

data class Status(
    val id: Int =0,
    val name: String, // student, adult, senior, pwd
)

data class User(
    val id: Int = 0,
    val username: String,
    val password: String,
    val fullName: String,
    val passwordHint: String,
    val isDriver: String,
    val vehicle: Int?, // vehicle id
    val longitude: Double?, // for real time loc
    val latitude: Double?, // for real time loc,
    val isDriving: Boolean?, // green [no], red [yes]
    val status: Int?, // status id
    val createDate: String,
    val isDeleted: Boolean,
)

data class Transaction(
    val id: Int = 0,
    val userId: Int,
    val driverId: Int,
    val dateOfTransaction: String,
    val startDestination: String,
    val finalDestination: String,
)
