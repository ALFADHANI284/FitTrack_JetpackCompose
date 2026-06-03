package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

// Model untuk list history
data class WorkoutHistory(
    val id: Int,
    @SerializedName("workout_id") val workoutId: Int?,
    @SerializedName("workout_name") val workoutName: String?, // Asumsi backend lu ngirim nama workout-nya
    val duration: Int?, // durasi dalam menit/detik
    @SerializedName("calories_burned") val caloriesBurned: Int?,
    @SerializedName("completed_at") val completedAt: String? // Tanggal selesai
)

// Model untuk nangkep respon GET list
data class HistoryListResponse(
    val status: Boolean,
    val data: List<WorkoutHistory>
)
