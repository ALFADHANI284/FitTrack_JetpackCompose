package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class UserData(
    val id: Int, // Biasanya Laravel otomatis menyertakan ID
    val name: String,
    val email: String,
    val role: String?,
    @SerializedName("avatar_path") val avatarPath: String?,
    val points: Int,
    val tier: String?,
    val goal: String?,
    @SerializedName("referral_code") val referralCode: String?,
    @SerializedName("daily_calories_target") val dailyCaloriesTarget: Int?,
    @SerializedName("daily_protein_target") val dailyProteinTarget: Int?,
    @SerializedName("daily_carbs_target") val dailyCarbsTarget: Int?,
    @SerializedName("daily_fat_target") val dailyFatTarget: Int?
)
