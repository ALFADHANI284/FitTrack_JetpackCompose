package com.aplikasi.fittrack.network

import com.aplikasi.fittrack.model.Achievement
import com.aplikasi.fittrack.model.AiChatHistoryResponse
import com.aplikasi.fittrack.model.AiChatRequest
import com.aplikasi.fittrack.model.AiChatStoreResponse
import com.aplikasi.fittrack.model.AnalyticsSummaryResponse
import com.aplikasi.fittrack.model.AuthResponse
import com.aplikasi.fittrack.model.BaseResponse
import com.aplikasi.fittrack.model.CategoryResponse
import com.aplikasi.fittrack.model.DefaultResponse
import com.aplikasi.fittrack.model.FavoriteResponse
import com.aplikasi.fittrack.model.GenericResponse
import com.aplikasi.fittrack.model.HistoryListResponse
import com.aplikasi.fittrack.model.LoginRequest
import com.aplikasi.fittrack.model.NotificationListResponse
import com.aplikasi.fittrack.model.OnboardingRequest
import com.aplikasi.fittrack.model.OnboardingResponse
import com.aplikasi.fittrack.model.ProfileResponse
import com.aplikasi.fittrack.model.ProgressListResponse
import com.aplikasi.fittrack.model.ProgressRequest
import com.aplikasi.fittrack.model.ProgressSingleResponse
import com.aplikasi.fittrack.model.ReferralRequest
import com.aplikasi.fittrack.model.ReferralResponse
import com.aplikasi.fittrack.model.RegisterRequest
import com.aplikasi.fittrack.model.ReminderListResponse
import com.aplikasi.fittrack.model.ReminderRequest
import com.aplikasi.fittrack.model.ReminderSingleResponse
import com.aplikasi.fittrack.model.ScheduleListResponse
import com.aplikasi.fittrack.model.ScheduleRequest
import com.aplikasi.fittrack.model.ScheduleResponse
import com.aplikasi.fittrack.model.StreakResponse
import com.aplikasi.fittrack.model.TdeeRequest
import com.aplikasi.fittrack.model.TdeeResponse
import com.aplikasi.fittrack.model.UserPointsResponse
import com.aplikasi.fittrack.model.WorkoutRequest
import com.aplikasi.fittrack.model.WorkoutResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    // ======================================================
    // AUTHENTICATION
    // ======================================================
    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): AuthResponse

    @POST("register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): AuthResponse


    // ======================================================
    // PROFILE & GOALS
    // ======================================================
    @Headers("Accept: application/json")
    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): ProfileResponse

    @Headers("Accept: application/json")
    @POST("profile/onboarding")
    suspend fun saveOnboarding(
        @Header("Authorization") token: String,
        @Body request: OnboardingRequest
    ): OnboardingResponse


    // ======================================================
    // WORKOUTS & CATEGORIES
    // ======================================================
    @GET("categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): BaseResponse<List<CategoryResponse>>

    @GET("workouts")
    suspend fun getWorkouts(
        @Header("Authorization") token: String
    ): BaseResponse<List<WorkoutResponse>>

    // Tambah Data Workout (Hanya Admin)
    @POST("workouts")
    suspend fun createWorkout(
        @Header("Authorization") token: String,
        @Body request: WorkoutRequest
    ): DefaultResponse

    // Endpoint Search Workout
    @GET("workouts/search")
    suspend fun searchWorkouts(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): BaseResponse<List<WorkoutResponse>>


    // ======================================================
    // SCHEDULES
    // ======================================================
    @POST("schedules")
    suspend fun createSchedule(
        @Header("Authorization") token: String,
        @Body request: ScheduleRequest
    ): Response<ScheduleResponse>

    @GET("workout-schedules")
    suspend fun getMySchedules(
        @Header("Authorization") token: String
    ): ScheduleListResponse


    // ======================================================
    // FAVORITES
    // ======================================================
    @POST("favorites/{workoutId}")
    suspend fun addToFavorite(
        @Header("Authorization") token: String,
        @Path("workoutId") workoutId: Int
    ): Response<FavoriteResponse>

    @DELETE("favorites/{workoutId}")
    suspend fun removeFromFavorite(
        @Header("Authorization") token: String,
        @Path("workoutId") workoutId: Int
    ): Response<FavoriteResponse>


    // ======================================================
    // ACHIEVEMENTS & POINTS
    // ======================================================
    @GET("achievements")
    suspend fun getAchievements(
        @Header("Authorization") token: String
    ): Response<List<Achievement>>

    @POST("achievements/claim/{id}")
    suspend fun claimAchievement(
        @Header("Authorization") token: String,
        @Path("id") achievementId: Int
    ): Response<GenericResponse>

    @GET("achievements/points")
    suspend fun getUserPoints(
        @Header("Authorization") token: String
    ): Response<UserPointsResponse>


    // ======================================================
    // REFERRALS
    // ======================================================
    @POST("referrals/redeem")
    suspend fun redeemReferral(
        @Header("Authorization") token: String,
        @Body request: ReferralRequest
    ): Response<ReferralResponse>


    // ======================================================
    // AI (FIT-AI)
    // ======================================================
    @GET("ai/chat")
    suspend fun getAiChatHistory(
        @Header("Authorization") token: String
    ): AiChatHistoryResponse

    @POST("ai/chat")
    suspend fun sendAiChatMessage(
        @Header("Authorization") token: String,
        @Body request: AiChatRequest
    ): AiChatStoreResponse

    @GET("ai/personalization")
    suspend fun getAiPersonalization(
        @Header("Authorization") token: String
    ): DefaultResponse

    @POST("ai/personalization")
    suspend fun saveAiPersonalization(
        @Header("Authorization") token: String,
        @Body request: Any
    ): DefaultResponse

    @DELETE("ai/personalization")
    suspend fun deleteAiPersonalization(
        @Header("Authorization") token: String
    ): DefaultResponse

    // ======================================================
    // USERS & TDEE
    // ======================================================

    // 1. Hitung Target Nutrisi / TDEE
    @POST("users/calculate-tdee")
    suspend fun calculateTdee(
        @Header("Authorization") token: String,
        @Body request: TdeeRequest
    ): Response<TdeeResponse>

    // 2. Upload Foto Profil (Avatar)
    @Multipart
    @POST("users/upload-avatar")
    suspend fun uploadAvatar(
        @Header("Authorization") token: String,
        @Part avatar: MultipartBody.Part
    ): Response<GenericResponse>

    // ======================================================
    // WORKOUT HISTORY
    // ======================================================

    // 1. Ambil daftar history (Buat di halaman Profile/History)
    @GET("workout-history")
    suspend fun getWorkoutHistory(
        @Header("Authorization") token: String
    ): Response<HistoryListResponse>

    // 2. Catat history baru SAAT SELESAI LATIHAN
    @POST("workout-history/{id}")
    suspend fun saveWorkoutHistory(
        @Header("Authorization") token: String,
        @Path("id") workoutId: Int
    ): Response<GenericResponse> // Pakai GenericResponse yang tadi kita bikin

    // 3. Hapus history (kalau salah pencet)
    @DELETE("workout-history/{id}")
    suspend fun deleteWorkoutHistory(
        @Header("Authorization") token: String,
        @Path("id") historyId: Int
    ): Response<GenericResponse>

    // ======================================================
    // REMINDERS
    // ======================================================

    // 1. Ambil semua settingan alarm user
    @GET("reminders")
    suspend fun getReminders(
        @Header("Authorization") token: String
    ): Response<ReminderListResponse>

    // 2. Ambil detail 1 alarm (opsional, jarang dipake kalau datanya udah dapet semua di list)
    @GET("reminders/{id}")
    suspend fun getReminderDetail(
        @Header("Authorization") token: String,
        @Path("id") reminderId: Int
    ): Response<ReminderSingleResponse>

    // 3. Bikin alarm baru
    @POST("reminders")
    suspend fun createReminder(
        @Header("Authorization") token: String,
        @Body request: ReminderRequest
    ): Response<ReminderSingleResponse>

    // 4. Update alarm (Paling sering dipake buat toggle ON/OFF alarm di UI)
    @PUT("reminders/{id}")
    suspend fun updateReminder(
        @Header("Authorization") token: String,
        @Path("id") reminderId: Int,
        @Body request: ReminderRequest
    ): Response<ReminderSingleResponse>

    // 5. Hapus alarm
    @DELETE("reminders/{id}")
    suspend fun deleteReminder(
        @Header("Authorization") token: String,
        @Path("id") reminderId: Int
    ): Response<GenericResponse>

    // ======================================================
    // PROGRESS TRACKER
    // ======================================================

    // 1. Ambil semua riwayat perkembangan untuk grafik
    @GET("progress")
    suspend fun getProgressHistory(
        @Header("Authorization") token: String
    ): Response<ProgressListResponse>

    // 2. Catat berat & tinggi badan baru
    @POST("progress")
    suspend fun createProgress(
        @Header("Authorization") token: String,
        @Body request: ProgressRequest
    ): Response<ProgressSingleResponse>

    // 3. Update data progres berdasarkan ID
    @PUT("progress/{id}")
    suspend fun updateProgress(
        @Header("Authorization") token: String,
        @Path("id") progressId: Int,
        @Body request: ProgressRequest
    ): Response<ProgressSingleResponse>

    // 4. Hapus log progres
    @DELETE("progress/{id}")
    suspend fun deleteProgress(
        @Header("Authorization") token: String,
        @Path("id") progressId: Int
    ): Response<GenericResponse>

    // ======================================================
    // ANALYTICS & DASHBOARD
    // ======================================================

    // Ambil rangkuman data untuk di halaman Home / Dashboard
    @GET("analytics/summary")
    suspend fun getAnalyticsSummary(
        @Header("Authorization") token: String
    ): Response<AnalyticsSummaryResponse>

    // ======================================================
    // NOTIFICATIONS
    // ======================================================

    // 1. Ambil daftar kotak masuk notifikasi user
    @GET("notifications")
    suspend fun getNotifications(
        @Header("Authorization") token: String
    ): Response<NotificationListResponse>

    // 2. Tandai pesan sudah dibaca
    @PUT("notifications/{id}/read")
    suspend fun markNotificationAsRead(
        @Header("Authorization") token: String,
        @Path("id") notificationId: Int
    ): Response<GenericResponse>

    // ======================================================
    // STREAK
    // ======================================================
    @GET("user/streak")
    suspend fun getUserStreak(
        @Header("Authorization") token: String
    ): Response<StreakResponse>
}

