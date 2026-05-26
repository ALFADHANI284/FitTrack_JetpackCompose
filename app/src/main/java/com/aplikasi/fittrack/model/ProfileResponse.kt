package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    val status: String,
    val message: String?,
    @SerializedName("data")
    val data: UserData,
)
