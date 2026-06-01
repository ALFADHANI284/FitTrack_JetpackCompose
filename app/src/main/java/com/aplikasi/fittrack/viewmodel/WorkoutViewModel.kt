package com.aplikasi.fittrack.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasi.fittrack.model.ScheduleRequest
import com.aplikasi.fittrack.network.ApiService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WorkoutDetailViewModel(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    // State untuk memantau apakah gerakan ini sudah di-favoritkan oleh user atau belum
    var isFavorite = mutableStateOf(false)
        private set

    var isLoadingFavorite = mutableStateOf(false)
        private set

    private fun getToken(): String {
        val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
        val savedToken = sharedPref.getString("ACCESS_TOKEN", "") ?: ""
        return if (savedToken.startsWith("Bearer ")) savedToken else "Bearer $savedToken"
    }

    // Fungsi utama untuk toggle (pencet tombol love)
    fun toggleFavorite(workoutId: Int) {
        viewModelScope.launch {
            isLoadingFavorite.value = true
            val token = getToken()

            try {
                if (isFavorite.value) {
                    // Kalau awalnya true, berarti user mau UNFAVORITE (hapus)
                    val response = apiService.removeFromFavorite(token, workoutId)
                    if (response.isSuccessful) {
                        isFavorite.value = false
                    }
                } else {
                    // Kalau awalnya false, berarti user mau FAVORITE (tambah)
                    val response = apiService.addToFavorite(token, workoutId)
                    if (response.isSuccessful) {
                        isFavorite.value = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Opsional: tampilin Toast error jika koneksi gagal
            } finally {
                isLoadingFavorite.value = false
            }
        }
    }

    // Jangan lupa set state awal pas detail workout berhasil diload dari API
    fun setInitialFavoriteStatus(status: Boolean) {
        isFavorite.value = status
    }

    var isLoadingSchedule = mutableStateOf(false)
        private set

    fun addWorkoutSchedule(workoutId: Int, hour: Int, minute: Int) {
        viewModelScope.launch {
            isLoadingSchedule.value = true
            val token = getToken()

            // 1. Format tanggal hari ini + jam menit pilihan user menjadi "yyyy-MM-dd HH:mm:ss"
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = sdf.format(calendar.time)

            try {
                val request = ScheduleRequest(
                    workoutId = workoutId,
                    scheduleTime = formattedDate // Mengisi field schedule_time
                )
                val response = apiService.createSchedule(token, request)

                if (response.isSuccessful && response.body()?.status == true) {
                    Toast.makeText(context, "Jadwal berhasil disimpan! ⏰", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Gagal membuat jadwal: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error Koneksi: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isLoadingSchedule.value = false
            }
        }
    }
}