package com.aplikasi.fittrack.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasi.fittrack.model.WorkoutResponse
import com.aplikasi.fittrack.network.ApiService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    var searchResults = mutableStateOf<List<WorkoutResponse>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    private var searchJob: Job? = null

    private fun getToken(): String {
        val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
        val savedToken = sharedPref.getString("ACCESS_TOKEN", "") ?: ""
        return if (savedToken.startsWith("Bearer ")) savedToken else "Bearer $savedToken"
    }

    // Fungsi pencarian dinamis
    fun performSearch(query: String) {
        // Batalkan pencarian sebelumnya kalau user ngetik lagi dengan cepat
        searchJob?.cancel()

        if (query.isBlank()) {
            searchResults.value = emptyList()
            isLoading.value = false
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // Efek debounce: Tunggu 0.5 detik setelah ngetik biar gak spam API
            isLoading.value = true
            errorMessage.value = null

            try {
                val token = getToken()
                val response = apiService.searchWorkouts(token, query)

                if (response.status) {
                    searchResults.value = response.data
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = "Gagal mencari data"
            } finally {
                isLoading.value = false
            }
        }
    }
}