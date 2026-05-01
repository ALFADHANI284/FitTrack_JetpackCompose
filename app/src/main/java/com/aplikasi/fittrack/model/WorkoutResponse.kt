package com.aplikasi.fittrack.model

data class WorkoutResponse(
    val id: Int,
    val category_id: Int,
    val name: String,
    val duration_minutes: Int,
    val calories_burned: Int,
    val description: String
)
