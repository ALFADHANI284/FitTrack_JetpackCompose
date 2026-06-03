package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class Achievement(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("badge_icon") val badgeIcon: String,
    val points: Int,
    @SerializedName("is_claimed") val isClaimed: Boolean = false,
    @SerializedName("is_unlocked") val isUnlocked: Boolean = false
)
