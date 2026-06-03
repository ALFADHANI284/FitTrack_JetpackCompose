package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

data class ReferralRequest(
    @SerializedName("referral_code")
    val referralCode: String
)

data class ReferralResponse(
    val message: String,
    @SerializedName("current_points")
    val currentPoints: Int? = null
)