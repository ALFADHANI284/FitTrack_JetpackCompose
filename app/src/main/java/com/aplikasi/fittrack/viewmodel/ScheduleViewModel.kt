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
import com.aplikasi.fittrack.model.ScheduleRequest
import com.aplikasi.fittrack.network.ApiService
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Model UI untuk menampung data Reminder dari Laravel
data class ReminderUIModel(
    val id: Int,
    val workoutName: String,
    val scheduleTime: String // Format: "yyyy-MM-dd HH:mm:ss"
)

class ScheduleViewModel(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    var remindersList = mutableStateOf<List<ReminderUIModel>>(emptyList())
        private set
    var isLoading = mutableStateOf(false)
        private set

    private fun getToken(): String {
        val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
        val savedToken = sharedPref.getString("ACCESS_TOKEN", "") ?: ""
        return "Bearer $savedToken"
    }

    // 1. Ambil Semua Jadwal (GET)
    fun loadAllReminders() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getReminders(getToken())
                if (response.isSuccessful) {
                    val jsonString = (response.body() as? okhttp3.ResponseBody)?.string() ?: ""
                    val dataArray = JSONObject(jsonString).optJSONArray("data") ?: org.json.JSONArray()

                    val tempList = mutableListOf<ReminderUIModel>()
                    for (i in 0 until dataArray.length()) {
                        val item = dataArray.getJSONObject(i)
                        // Mengambil relasi nama workout jika ada di json, kalau ga ada default "Workout"
                        val workoutObj = item.optJSONObject("workout")
                        val wName = workoutObj?.optString("name") ?: "Workout Session"

                        tempList.add(
                            ReminderUIModel(
                                id = item.getInt("id"),
                                workoutName = wName,
                                scheduleTime = item.getString("schedule_time")
                            )
                        )
                    }
                    remindersList.value = tempList
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    // 2. Update & Pasang Ulang Alarm Baru (PUT)
    fun updateScheduleTime(reminderId: Int, hour: Int, minute: Int) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = sdf.format(calendar.time)

            try {
                // Sesuaikan dengan data class ScheduleRequest lu
                val request = ScheduleRequest(
                    workoutId = 0, // Atau ambil dari data sebelumnya
                    scheduleTime = formattedDate,
                    title = "Jadwal Baru Diupdate" // Opsional karena lu udah set default
                )
                val response = apiService.updateReminder(getToken(), reminderId, request)

                if (response.isSuccessful) {
                    // Update Alarm Lokal di HP
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(context, AlarmReceiver::class.java).apply {
                        putExtra("EXTRA_TITLE", "Jadwal Latihan Diperbarui! ⏰")
                        putExtra("EXTRA_MESSAGE", "Sesi latihan kamu dijadwalkan ulang ke jam $hour:$minute.")
                    }
                    val pendingIntent = PendingIntent.getBroadcast(
                        context, reminderId, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

                    Toast.makeText(context, "Jadwal berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    loadAllReminders() // Refresh list UI
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}