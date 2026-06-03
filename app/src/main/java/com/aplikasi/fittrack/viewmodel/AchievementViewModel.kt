package com.aplikasi.fittrack.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasi.fittrack.model.Achievement
import com.aplikasi.fittrack.network.ApiService
import kotlinx.coroutines.launch

class AchievementViewModel(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    var achievements = mutableStateOf<List<Achievement>>(emptyList())
    var totalPoints = mutableStateOf(0)
    var isLoading = mutableStateOf(false)

    private fun getToken(): String {
        val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
        return "Bearer ${sharedPref.getString("ACCESS_TOKEN", "") ?: ""}"
    }

    fun loadAchievementData() {
        viewModelScope.launch {
            isLoading.value = true
            val token = getToken()
            try {
                // Tarik data point & list badge secara pararel
                val pointsRoute = apiService.getUserPoints(token)
                val listRoute = apiService.getAchievements(token)

                if (pointsRoute.isSuccessful) {
                    totalPoints.value = pointsRoute.body()?.totalPoints ?: 0
                }
                if (listRoute.isSuccessful) {
                    achievements.value = listRoute.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    fun claimBadge(id: Int) {
        viewModelScope.launch {
            val token = getToken()
            try {
                val response = apiService.claimAchievement(token, id)
                if (response.isSuccessful) {
                    // Refresh data setelah berhasil klaim biar poin bertambah di UI
                    loadAchievementData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}