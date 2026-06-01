package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class ScheduleItem(
    val id: Int,
    @SerializedName("workout_id") val workoutId: Int,
    @SerializedName("schedule_time") val scheduleTime: String, // Contoh: "2026-06-01 17:30:00"
    @SerializedName("is_notified") val isNotified: Int,
    val title: String,
    val description: String?,
    // Nangkep relasi data workout dari Laravel (biar bisa nampilin nama olahraganya)
    val workout: WorkoutResponse?
)
