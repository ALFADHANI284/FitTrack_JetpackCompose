package com.aplikasi.fittrack.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasi.fittrack.model.AnalyticsData
import com.aplikasi.fittrack.network.ApiService
import kotlinx.coroutines.launch

class HomeViewModel(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    // ======================================================
    // STATE (Data yang bakal ditampilin di UI)
    // ======================================================

    // State Loading Global
    var isLoading = mutableStateOf(false)

    // State untuk Dashboard / Analytics
    var analyticsData = mutableStateOf(AnalyticsData())

    // State untuk Streak
    var streakDays = mutableStateOf(0)
    var isWorkoutToday = mutableStateOf(false)


    // ======================================================
    // HELPER
    // ======================================================
    private fun getToken(): String {
        val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
        val savedToken = sharedPref.getString("ACCESS_TOKEN", "") ?: ""
        return if (savedToken.startsWith("Bearer ")) savedToken else "Bearer $savedToken"
    }


    // ======================================================
    // FUNCTIONS (Fungsi nembak API)
    // ======================================================

    // 1. Ambil data Analytics / Dashboard
    fun loadDashboardSummary() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = apiService.getAnalyticsSummary(getToken())
                if (response.isSuccessful) {
                    response.body()?.data?.let { summary ->
                        analyticsData.value = summary
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    // 2. Ambil data Streak
    fun loadUserStreak() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = apiService.getUserStreak(getToken())
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    streakDays.value = data.streakDays
                    isWorkoutToday.value = data.hasWorkoutToday
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    fun loadAllHomeData() {
        loadDashboardSummary()
        loadUserStreak()
    }
}