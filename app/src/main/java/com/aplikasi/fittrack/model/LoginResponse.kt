package com.aplikasi.fittrack.model

data class LoginResponse(
    val token: String,
    val role: String,
    val user: UserData
)
