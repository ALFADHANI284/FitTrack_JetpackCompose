package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class WorkoutResponse(
    val id: Int,
    val category_id: Int,
    val name: String,
    val duration_minutes: Int? =null,
    val calories_burned: Int? =null,
    val description: String? =null,
    val link_yt: String? = null,
    @SerializedName("is_favorite")
    val isFavorite: Boolean? = false
)
