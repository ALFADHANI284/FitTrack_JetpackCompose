package com.aplikasi.fittrack.model

data class WorkoutRequest(
    val category_id: Int,
    val name: String,
    val duration_minutes: Int,
    val calories_burned: Int,
    val description: String
)