package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class DefaultResponse(
    @SerializedName("status")
    val status: Boolean,
    val message: String
)