package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class StreakResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("streak_days")
    val streakDays: Int,

    @SerializedName("has_workout_today")
    val hasWorkoutToday: Boolean
)
