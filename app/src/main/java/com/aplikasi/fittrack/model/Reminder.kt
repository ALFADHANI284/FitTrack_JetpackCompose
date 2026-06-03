package com.aplikasi.fittrack.model

import com.google.gson.annotations.SerializedName

// 1. Model untuk nangkep data Reminder dari Laravel
data class Reminder(
    val id: Int,
    val title: String,
    val message: String?, // Bisa null kalau user ga ngisi pesan
    @SerializedName("remind_at") val remindAt: String, // Format tanggal/waktu dari Laravel (misal: "2026-06-03 07:00:00")
    @SerializedName("is_sent") val isSent: Boolean = false // Penanda apakah notifnya udah dikirim atau belum
)

// 2. Model untuk ngirim data (Create / Update dari Android ke Laravel)
data class ReminderRequest(
    val title: String,
    val message: String,
    @SerializedName("remind_at") val remindAt: String,
    @SerializedName("is_sent") val isSent: Boolean = false
)

// 3. Model untuk respon List
data class ReminderListResponse(
    val status: Boolean,
    val data: List<Reminder>
)

// 4. Model untuk respon Single
data class ReminderSingleResponse(
    val status: Boolean,
    val message: String,
    val data: Reminder?
)
