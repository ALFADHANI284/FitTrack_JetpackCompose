package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class Progress(
    val id: Int,
    @SerializedName("measured_at") val measuredAt: String, // Tanggal ngukur
    @SerializedName("weight_kg") val weightKg: Double,
    @SerializedName("body_fat_percentage") val bodyFatPercentage: Double?,
    @SerializedName("muscle_mass_kg") val muscleMassKg: Double?,
    val notes: String? // Catatan tambahan
)

// 2. Model Request untuk POST/PUT
data class ProgressRequest(
    @SerializedName("measured_at") val measuredAt: String,
    @SerializedName("weight_kg") val weightKg: Double,
    @SerializedName("body_fat_percentage") val bodyFatPercentage: Double?,
    @SerializedName("muscle_mass_kg") val muscleMassKg: Double?,
    val notes: String?
)

// 3. Model Response List
data class ProgressListResponse(
    val status: Boolean,
    val data: List<Progress>
)

// 4. Model Response Single
data class ProgressSingleResponse(
    val status: Boolean,
    val message: String,
    val data: Progress?
)