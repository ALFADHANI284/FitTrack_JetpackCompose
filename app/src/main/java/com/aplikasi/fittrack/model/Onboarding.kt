package com.aplikasi.fittrack.model

data class OnboardingRequest(
    val motivation: String,
    val goal: String,
    val gender: String,
    val age: Int,
    val weight: Float,
    val height: Float,
    val activity_level: String
)

data class OnboardingData(
    val name: String,
    val goal: String,
    val daily_calorie_target: String
)

data class OnboardingResponse(
    val status: Boolean,
    val message: String,
    val data: OnboardingData? = null
)
