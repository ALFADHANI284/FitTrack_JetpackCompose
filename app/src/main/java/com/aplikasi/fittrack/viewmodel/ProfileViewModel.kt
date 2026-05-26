package com.aplikasi.fittrack.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasi.fittrack.model.UserData
import com.aplikasi.fittrack.network.ApiService
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    var profileData = mutableStateOf<UserData?>(null)
        private set

    var isLoading = mutableStateOf(true)
        private set

    fun fetchProfile() {
        // viewModelScope.launch digunakan agar proses ambil data berjalan di background (tidak bikin UI nge-freeze)
        viewModelScope.launch {
            isLoading.value = true // Nyalakan animasi loading

            try {
                // 1. Ambil token dari SharedPreferences yang sudah disimpan saat Login
                val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
                val token = sharedPref.getString("ACCESS_TOKEN", "") ?: ""

                if (token.isNotEmpty()) {
                    // 2. Tembak API Laravel menggunakan Bearer Token
                    val response = apiService.getProfile("Bearer $token")

                    // 3. Masukkan data asli (UserData) dari Laravel ke dalam State
                    profileData.value = response.data
                } else {
                    println("Token kosong! User mungkin belum login atau session habis.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Masuk ke sini kalau internet mati atau token ditolak (Unauthorized)
                println("Gagal mengambil data profil: ${e.message}")
            } finally {
                // 4. Matikan animasi loading, entah prosesnya tadi sukses ataupun gagal
                isLoading.value = false
            }
        }
    }
}