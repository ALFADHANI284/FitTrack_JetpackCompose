package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class FinishWorkoutRequest(
    @SerializedName("workout_id") val workoutId: Int,
    val status: String = "completed",
    @SerializedName("duration_minutes") val durationMinutes: Int,
    @SerializedName("calories_burned") val caloriesBurned: Int,
    @SerializedName("completed_at") val completedAt: String,
    val notes: String? = null
)
