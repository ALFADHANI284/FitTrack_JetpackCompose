package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class DefaultResponse(
    @SerializedName("message")
    val message: String
)