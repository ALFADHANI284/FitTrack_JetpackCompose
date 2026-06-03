package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class UserPointsResponse(
    val status: Boolean,
    @SerializedName("total_points") val totalPoints: Int
)
