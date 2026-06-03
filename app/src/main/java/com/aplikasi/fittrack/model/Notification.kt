package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

// 1. Model detail notifikasi
data class NotificationData(
    val id: Int,
    val title: String,
    val message: String,
    @SerializedName("is_read") var isRead: Boolean = false // var supaya bisa diubah lokal di UI
)

// 2. Model buat nangkep list notifikasi dari API
data class NotificationListResponse(
    val status: Boolean,
    val data: List<NotificationData>
)
