package com.aplikasi.fittrack.model

data class ScheduleListResponse(
    val status: Boolean,
    val message: String,
    val data: List<ScheduleItem>
)
