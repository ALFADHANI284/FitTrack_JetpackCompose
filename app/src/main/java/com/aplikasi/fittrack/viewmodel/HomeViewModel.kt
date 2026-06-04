package com.aplikasi.fittrack.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasi.fittrack.model.AnalyticsData
import com.aplikasi.fittrack.model.ReviewRequest
import com.aplikasi.fittrack.model.ReviewUIModel
import com.aplikasi.fittrack.network.ApiService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class HomeViewModel(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    // ======================================================
    // STATE (Data yang bakal ditampilin di UI)
    // ======================================================

    var isLoading = mutableStateOf(false)
    var analyticsData = mutableStateOf(AnalyticsData())
    var streakDays = mutableStateOf(0)
    var isWorkoutToday = mutableStateOf(false)

    // Cukup 1 kali aja deklarasinya di sini
    var weeklyWorkoutCounts = mutableStateOf<List<Int>>(listOf(0, 0, 0, 0, 0, 0, 0))
        private set

    // Menyimpan list pasangan (Nama Workout, Durasi)
    var favoriteWorkouts = mutableStateOf<List<Pair<String, String>>>(listOf(
        Pair("Push Up Master", "15 Min"),
        Pair("Morning Yoga", "20 Min"),
        Pair("Dumbbell Press", "10 Min")
    ))
        private set

    // State untuk Your Progress
    var currentWeight = mutableStateOf("0.0 KG")
        private set
    var weightDifferenceText = mutableStateOf("No data yet")
        private set

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

    // 3. Ambil data buat Grafik Batang (Nama fungsi diubah biar gak bentrok)
    fun loadWeeklyHistory() {
        viewModelScope.launch {
            val token = getToken()
            try {
                val response = apiService.getWorkoutHistory(token)

                if (response.isSuccessful) {
                    // Langsung ambil list-nya dari response body
                    // Asumsi di dalam HistoryListResponse ada variabel list, misalnya 'data'
                    val historyList = response.body()?.data ?: emptyList()

                    val dailyCounts = IntArray(7) { 0 }
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val calendar = Calendar.getInstance()

                    // Looping langsung dari objek Kotlin lu
                    for (item in historyList) {
                        // Asumsi nama variabel tanggal di data class lu itu 'createdAt' atau 'created_at'
                        val dateString = item.completedAt ?: ""

                        if (dateString.isNotBlank()) {
                            try {
                                val date = dateFormat.parse(dateString.take(10))
                                if (date != null) {
                                    calendar.time = date
                                    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                                    dailyCounts[dayOfWeek - 1] += 1
                                }
                            } catch (e: Exception) {
                                // Lewatin aja kalau format tanggalnya aneh
                            }
                        }
                    }

                    weeklyWorkoutCounts.value = dailyCounts.toList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Kalau internet mati, dummy nyala
                weeklyWorkoutCounts.value = listOf(0, 2, 5, 1, 3, 0, 4)
            }
        }
    }

    fun loadFavoriteWorkouts() {
        viewModelScope.launch {
            val token = getToken()
            try {
                val response = apiService.getFavoriteWorkouts(token)
                if (response.isSuccessful) {
                    val jsonString = response.body()?.string() ?: ""
                    val tempList = mutableListOf<Pair<String, String>>()

                    // Bongkar JSON-nya secara dinamis
                    val dataArray = if (jsonString.trim().startsWith("{")) {
                        org.json.JSONObject(jsonString).optJSONArray("data") ?: org.json.JSONArray()
                    } else {
                        org.json.JSONArray(jsonString)
                    }

                    for (i in 0 until dataArray.length()) {
                        val item = dataArray.getJSONObject(i)

                        // Hubungkan ke relasi workout-nya di JSON Laravel lu
                        // Biasanya strukturnya: item.workout.name ATAU langsung di item-nya
                        val workoutObj = item.optJSONObject("workout")

                        val title = workoutObj?.optString("name") ?: item.optString("title", "Workout")
                        val durationNum = workoutObj?.optInt("duration") ?: item.optInt("duration", 10)

                        tempList.add(Pair(title, "$durationNum Min"))
                    }

                    // Jika di database beneran ada data favorit, ganti pakai data asli
                    if (tempList.isNotEmpty()) {
                        favoriteWorkouts.value = tempList
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Kalau error/jaringan mati, biarin dia pake data cadangan di atas
            }
        }
    }

    fun loadProgressHistory() {
        viewModelScope.launch {
            val token = getToken()
            try {
                val response = apiService.getProgressHistory(token)

                if (response.isSuccessful) {
                    val progressList = response.body()?.data ?: emptyList()

                    if (progressList.isNotEmpty()) {
                        // 1. Urutkan dari tanggal yang paling baru ke paling lama
                        val sortedList = progressList.sortedByDescending { it.measuredAt }

                        // 2. Ambil data paling baru (indeks 0)
                        val latestProgress = sortedList[0]

                        // Update angka berat yang gede
                        // Cek kalau desimalnya 0 (misal 67.0), tampilkan 67 aja. Kalau 67.5 tetep 67.5
                        val formattedWeight = if (latestProgress.weightKg % 1.0 == 0.0) {
                            latestProgress.weightKg.toInt().toString()
                        } else {
                            latestProgress.weightKg.toString()
                        }
                        currentWeight.value = "$formattedWeight KG"

                        // 3. Bandingin sama data sebelumnya (indeks 1) kalau ada
                        if (sortedList.size > 1) {
                            val previousProgress = sortedList[1]
                            val diff = latestProgress.weightKg - previousProgress.weightKg

                            // Atur teks panah naik/turun
                            weightDifferenceText.value = when {
                                diff < 0 -> "↓ ${abs(diff)}kg dari rekaman terakhir"
                                diff > 0 -> "↑ ${abs(diff)}kg dari rekaman terakhir"
                                else -> "Tidak ada perubahan berat"
                            }
                        } else {
                            // Kalau baru punya 1 data di database
                            weightDifferenceText.value = "Berat badan pertamamu"
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                currentWeight.value = "67.5 KG"
                weightDifferenceText.value = "↓ 2.5kg minggu ini"
            }
        }
    }

    // ======================================================
    // STATE REVIEW
    // ======================================================

    // Nampung list ulasan buat ditampilin di UI
    var userReviews = mutableStateOf<List<ReviewUIModel>>(emptyList())
        private set

    // Buat ngatur pop-up tambah review muncul atau nggak
    var showReviewDialog = mutableStateOf(false)

    // Buat notifikasi berhasil/gagal
    var postReviewMessage = mutableStateOf("")


    // ======================================================
    // FUNCTIONS REVIEW (GET & POST)
    // ======================================================

    // 1. Narik data 3 review terbaru (GET)
    fun loadUserReviews() {
        viewModelScope.launch {
            val token = getToken()
            try {
                val response = apiService.getUserReviews(token)
                if (response.isSuccessful) {
                    val jsonString = response.body()?.string() ?: ""
                    // Pake org.json buat bongkar body dinamis
                    val dataArray = org.json.JSONObject(jsonString).optJSONArray("data") ?: org.json.JSONArray()

                    val tempList = mutableListOf<ReviewUIModel>()
                    for (i in 0 until dataArray.length()) {
                        val item = dataArray.getJSONObject(i)

                        val rating = item.optInt("rating", 5)
                        val text = item.optString("review", "")

                        tempList.add(ReviewUIModel(rating, text))
                    }
                    userReviews.value = tempList
                }
            } catch (e: Exception) {
                e.printStackTrace()
                userReviews.value = listOf(
                    ReviewUIModel(5, "Aplikasi ini ngebantu banget buat tracking progress harian!"),
                    ReviewUIModel(4, "Fiturnya keren, UI minimalisnya juga clean banget.")
                )
            }
        }
    }

    // 2. Ngirim review baru ke database (POST)
    fun submitReview(rating: Int, reviewText: String) {
        viewModelScope.launch {
            isLoading.value = true
            val token = getToken()
            try {
                // Sesuaiin sama ReviewRequest di ApiService lu
                val requestBody = ReviewRequest(rating = rating, review = reviewText)
                val response = apiService.postReview(token, requestBody)

                if (response.isSuccessful) {
                    postReviewMessage.value = "Review berhasil dikirim! 🚀"
                    showReviewDialog.value = false // Tutup pop-up

                    // Langsung tarik data lagi biar review barunya otomatis muncul di layar!
                    loadUserReviews()
                } else {
                    postReviewMessage.value = "Gagal kirim review."
                }
            } catch (e: Exception) {
                e.printStackTrace()
                postReviewMessage.value = "Error koneksi."
                showReviewDialog.value = false // Tetep tutup pop-up biar gak nyangkut
            } finally {
                isLoading.value = false
            }
        }
    }


    // 4. Panggil semua fungsi sekaligus pas Home dibuka
    fun loadAllHomeData() {
        loadDashboardSummary()
        loadUserStreak()
        loadWeeklyHistory()
        loadFavoriteWorkouts()
        loadProgressHistory()
        loadUserReviews()
    }
}