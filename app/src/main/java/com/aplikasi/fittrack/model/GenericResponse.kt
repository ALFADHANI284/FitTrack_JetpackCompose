package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class GenericResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String
)
