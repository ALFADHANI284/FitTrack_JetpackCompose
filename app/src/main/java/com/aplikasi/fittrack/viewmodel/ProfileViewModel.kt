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

    var isGuest = mutableStateOf(false)
        private set

    fun fetchProfile() {
        viewModelScope.launch {
            isLoading.value = true

            try {
                val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
                val token = sharedPref.getString("ACCESS_TOKEN", "") ?: ""

                if (token.isNotEmpty()) {
                    println("DEBUG_PROFIL: Token ada -> $token") // Cek token

                    val response = apiService.getProfile("Bearer $token")

                    println("DEBUG_PROFIL: Sukses nembak API! Data User -> ${response.data}") // Cek isi datanya

                    profileData.value = response.data
                    isGuest.value = false
                } else {
                    println("DEBUG_PROFIL: Token kosong woy!")
                    isGuest.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 👇 Ini penting banget biar kelihatan error aslinya!
                println("DEBUG_PROFIL: ERROR BANG -> ${e.localizedMessage}")

                if (e is retrofit2.HttpException && e.code() == 401) {
                    isGuest.value = true
                }
            } finally {
                isLoading.value = false
            }
        }
    }
}