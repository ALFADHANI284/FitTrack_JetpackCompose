package com.aplikasi.fittrack.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasi.fittrack.AlarmReceiver
import com.aplikasi.fittrack.model.FinishWorkoutRequest
import com.aplikasi.fittrack.model.ScheduleRequest
import com.aplikasi.fittrack.network.ApiService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
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

            // ==========================================
            // 1. SETTING WAKTU (Cerdas deteksi besok/hari ini)
            // ==========================================
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }

            // Kalau jam yang dipilih udah lewat hari ini, otomatis pasang buat besoknya!
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            // Format tanggal sesuai permintaan API Laravel
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = sdf.format(calendar.time)

            try {
                // ==========================================
                // 2. PASANG ALARM LOKAL DI HP
                // ==========================================
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                // Bawa pesan buat dikirim ke AlarmReceiver
                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    putExtra("EXTRA_TITLE", "Waktunya Latihan! ⏰")
                    putExtra("EXTRA_MESSAGE", "Jadwal workout jam $hour:$minute udah tiba. Yuk gerak!")
                }

                // Pake workoutId sebagai requestCode biar tiap jadwal punya alarm beda-beda
                val pendingIntent = PendingIntent.getBroadcast(
                    context, workoutId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                // Pasang bom waktu lokal!
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                println("DEBUG_ALARM: Alarm dipasang untuk $formattedDate")

                // ==========================================
                // 3. TEMBAK DATA KE LARAVEL
                // ==========================================
                val request = ScheduleRequest(
                    workoutId = workoutId,
                    scheduleTime = formattedDate
                )
                val response = apiService.createSchedule(token, request)

                if (response.isSuccessful) {
                    // Toast sukses diganti biar ngasih tau alarm juga nyala
                    Toast.makeText(context, "Jadwal & Pengingat berhasil dipasang!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Gagal membuat jadwal: ${response.code()}", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error Koneksi: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isLoadingSchedule.value = false
            }
        }
    }
    fun finishWorkout(workoutId: Int, duration: Int, calories: Int) {
        viewModelScope.launch {
            val token = getToken() // Pastikan fungsi getToken() udah ada

            try {
                // 1. Ambil waktu sekarang buat ngisi 'completed_at'
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentTime = sdf.format(Date())

                // 2. Bungkus datanya
                val requestBody = FinishWorkoutRequest(
                    workoutId = workoutId,
                    durationMinutes = duration,
                    caloriesBurned = calories,
                    completedAt = currentTime
                )

                // 3. Kirim ke Laravel
                val response = apiService.saveWorkoutHistory(token, requestBody)

                if (response.isSuccessful) {
                    // Berhasil!
                    // Di sini lu bisa kasih notif "Selesai!" atau navigasi balik ke Home
                    println("Mantap! Sejarah workout berhasil masuk ke Database!")
                } else {
                    println("Gagal nyimpen data: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}