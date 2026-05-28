package com.aplikasi.fittrack.model

data class WorkoutRequest(
    val category_id: Int,
    val name: String,
    val duration_minutes: Int? =null,
    val calories_burned: Int? =null,
    val description: String? =null,
    val link_yt: String? = null
)