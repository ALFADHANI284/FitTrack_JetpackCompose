package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val message: String,
    @SerializedName("data")
    val user: UserData,
    @SerializedName("access_token")
    val token: String,
    val token_type: String
)
