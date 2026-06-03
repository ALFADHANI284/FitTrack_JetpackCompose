package com.aplikasi.fittrack.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasi.fittrack.model.NotificationData
import com.aplikasi.fittrack.network.ApiService
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    // Nampung list notifikasi
    var notifications = mutableStateOf<List<NotificationData>>(emptyList())
    var isLoading = mutableStateOf(false)

    private fun getToken(): String {
        val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
        val savedToken = sharedPref.getString("ACCESS_TOKEN", "") ?: ""
        return if (savedToken.startsWith("Bearer ")) savedToken else "Bearer $savedToken"
    }

    // Panggil ini pas layar Notification dibuka
    fun loadNotifications() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = apiService.getNotifications(getToken())
                if (response.isSuccessful) {
                    notifications.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    // Panggil ini pas satu item notifikasi DI-KLIK
    fun markAsRead(notificationId: Int) {
        // 1. Ubah langsung di UI (Optimistic Update biar instan)
        val currentList = notifications.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == notificationId }

        if (index != -1 && !currentList[index].isRead) {
            currentList[index] = currentList[index].copy(isRead = true)
            notifications.value = currentList

            // 2. Baru jalanin di background ke Laravel
            viewModelScope.launch {
                try {
                    apiService.markNotificationAsRead(getToken(), notificationId)
                    // Gak perlu ngapa-ngapain kalau sukses, karena UI udah diubah duluan
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}