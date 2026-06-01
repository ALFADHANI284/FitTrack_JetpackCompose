package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class ScheduleRequest(
    @SerializedName("workout_id")
    val workoutId: Int,
    @SerializedName("schedule_time")
    val scheduleTime: String, // Format ke server: "2026-06-01 17:30:00"
    @SerializedName("title")
    val title: String = "Rencana Latihan FitTrack"
)
