package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class TdeeRequest(
    val weight: Double, // kg
    val height: Double, // cm
    val age: Int,
    val gender: String, // "male" atau "female"
    @SerializedName("activity_level")
    val activityLevel: String // "sedentary", "light", "moderate", "active", dll
)

// Model buat nangkep hasil hitungannya
data class TdeeResponse(
    val status: Boolean,
    val message: String,
    @SerializedName("tdee") val tdee: Int?, // Total kalori harian
    @SerializedName("bmi") val bmi: Double? // Opsional kalau Laravel lu ngirim BMI juga
)
