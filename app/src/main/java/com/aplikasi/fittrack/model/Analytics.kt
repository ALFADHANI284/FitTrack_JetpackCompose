package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

// Model penampung utama
data class AnalyticsSummaryResponse(
    val status: Boolean,
    val message: String?,
    val data: AnalyticsData?
)

// Isi statistik Admin
data class AnalyticsData(
    @SerializedName("total_users") val totalUsers: Int = 0,
    @SerializedName("total_workouts") val totalWorkouts: Int = 0,
    @SerializedName("total_favorites") val totalFavorites: Int = 0,
    @SerializedName("total_reviews") val totalReviews: Int = 0,
    @SerializedName("average_rating") val averageRating: Double = 0.0
)