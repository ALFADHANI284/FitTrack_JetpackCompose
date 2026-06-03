package com.aplikasi.fittrack.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasi.fittrack.model.Progress
import com.aplikasi.fittrack.model.ProgressRequest
import com.aplikasi.fittrack.network.ApiService
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    var progressList = mutableStateOf<List<Progress>>(emptyList())
    var isLoading = mutableStateOf(false)

    private fun getToken(): String {
        val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
        val savedToken = sharedPref.getString("ACCESS_TOKEN", "") ?: ""
        return if (savedToken.startsWith("Bearer ")) savedToken else "Bearer $savedToken"
    }

    // Ambil data dari Laravel untuk dilempar ke UI Grafik
    fun fetchProgressData() {
        viewModelScope.launch {
            isLoading.value = true
            val token = getToken()
            try {
                val response = apiService.getProgressHistory(token)
                if (response.isSuccessful && response.body()?.status == true) {
                    // Simpan list datanya, urutkan berdasarkan tanggal biar grafiknya bener
                    progressList.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    // Fungsi pas user klik tombol "Simpan Berat Badan"
    fun addNewProgress(weight: Double, height: Double, bodyFat: Double?, date: String) {
        viewModelScope.launch {
            val token = getToken()
            val request = ProgressRequest(
                measuredAt = date,
                weightKg = weight,
                bodyFatPercentage = bodyFat,
                muscleMassKg = null,
                notes = null
            )
            try {
                val response = apiService.createProgress(token, request)
                if (response.isSuccessful) {
                    // Refresh data biar grafik langsung update otomatis setelah input sukses
                    fetchProgressData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}